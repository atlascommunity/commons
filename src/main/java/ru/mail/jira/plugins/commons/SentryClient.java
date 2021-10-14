package ru.mail.jira.plugins.commons;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.UserBuilder;
import io.sentry.event.interfaces.ExceptionInterface;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

import javax.annotation.Nullable;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class SentryClient {

  private static JiraAuthenticationContext jiraAuthenticationContext;
  private static String pluginKey;
  private static String pluginVersion;

  public static void init(
      JiraAuthenticationContext jiraAuthenticationContext, PluginProperties pluginProperties) {
    String dsn = pluginProperties.getSentryDsn().orElse(null);
    if (dsn != null && hasConnection(dsn)) {
      SentryClient.jiraAuthenticationContext = jiraAuthenticationContext;
      Sentry.init(dsn);

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
        setContextUser();
        EventBuilder eventBuilder =
            new EventBuilder()
                .withMessage(throwable.getMessage())
                .withLevel(Event.Level.ERROR)
                .withSentryInterface(new ExceptionInterface(throwable));
        if (request != null) {
          eventBuilder.withTag("method", request.getMethod());
        }
        if (uriInfo != null) {
          eventBuilder
              .withTag("path", uriInfo.getPath())
              .withTag("url", uriInfo.getAbsolutePath().toString());
        }
        if (pluginKey != null && pluginVersion != null) {
          eventBuilder
              .withTag("plugin", pluginKey)
              .withTag("version", pluginVersion)
              .withRelease(pluginKey + ":" + pluginVersion);
        }

        Sentry.capture(eventBuilder);
      } catch (Throwable e) {
      } finally {
        Sentry.clearContext();
      }
    }
  }

  public static void capture(String message) {
    if (isInitialized()) {
      try {
        setContextUser();
        Sentry.capture(message);
      } catch (Throwable e) {
      } finally {
        Sentry.clearContext();
      }
    }
  }

  public static void close() {
    Sentry.close();
  }

  private static boolean isInitialized() {
    return jiraAuthenticationContext != null;
  }

  private static void setContextUser() {
    if (jiraAuthenticationContext != null && jiraAuthenticationContext.isLoggedInUser()) {
      ApplicationUser user = jiraAuthenticationContext.getLoggedInUser();
      Sentry.getStoredClient()
          .getContext()
          .setUser(
              new UserBuilder()
                  .setId(user.getKey())
                  .setEmail(user.getEmailAddress())
                  .setUsername(user.getDisplayName())
                  .build());
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
