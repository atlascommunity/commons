package ru.mail.jira.plugins.commons;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import javax.annotation.Nullable;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.jetbrains.annotations.NotNull;

public class SentryClient {

  private static JiraAuthenticationContext jiraAuthenticationContext;
  private static String pluginKey;
  private static String pluginVersion;

  public static void init(
      String env,
      JiraAuthenticationContext jiraAuthenticationContext,
      PluginProperties pluginProperties) {
    String dsn = pluginProperties.getSentryDsn().orElse(null);
    if (dsn != null && hasConnection(dsn)) {
      SentryClient.jiraAuthenticationContext = jiraAuthenticationContext;
      Sentry.init(dsn);
      Sentry.init(
          options -> {
            options.setDsn(dsn);
            if (pluginKey != null && pluginVersion != null) {
              options.setTag("plugin", pluginKey);
              options.setTag("version", pluginVersion);
              options.setRelease(pluginKey + ":" + pluginVersion);
              options.setEnvironment(env);
            }
          });

      pluginKey = pluginProperties.getPluginKey().orElse(null);
      pluginVersion = pluginProperties.getPluginVersion().orElse(null);
    }
  }

  public static void capture(Throwable throwable) {
    capture(null, null, throwable);
  }

  public static void capture(
      @Nullable Request request, @Nullable UriInfo uriInfo, Throwable throwable) {
    if (isInitialized()) {
      try {
        Sentry.withScope(
            scope -> {
              setScopeUser(scope);

              if (uriInfo != null) {
                scope.setTag("path", uriInfo.getPath());
                scope.setTag("url", uriInfo.getAbsolutePath().toString());
              }

              if (request != null) {
                scope.setTag("method", request.getMethod());
              }

              Sentry.captureException(throwable);
            });
      } catch (Throwable e) {
        // ignored
      }
    }
  }

  public static void capture(String message) {
    if (isInitialized()) {
      try {
        Sentry.captureMessage(message);
        Sentry.withScope(
            scope -> {
              setScopeUser(scope);
              Sentry.captureMessage(message);
            });
      } catch (Throwable e) {
        // ignored
      }
    }
  }

  public static void close() {
    Sentry.close();
  }

  private static boolean isInitialized() {
    return jiraAuthenticationContext != null;
  }

  private static void setScopeUser(@NotNull Scope scope) {
    if (jiraAuthenticationContext != null && jiraAuthenticationContext.isLoggedInUser()) {
      ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();
      User user = new User();
      user.setId(loggedInUser.getKey());
      user.setEmail(loggedInUser.getEmailAddress());
      user.setUsername(loggedInUser.getDisplayName());
      scope.setUser(user);
    }
  }

  private static boolean hasConnection(String dsn) {
    try (UnirestInstance client = Unirest.spawnInstance()) {
      client.config().connectTimeout(5_000).socketTimeout(5_000);
      return client.get(dsn).asEmpty().isSuccess();
    } catch (Exception e) {
      return Boolean.FALSE;
    }
  }
}
