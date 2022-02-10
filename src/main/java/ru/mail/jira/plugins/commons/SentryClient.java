package ru.mail.jira.plugins.commons;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.ExecutingHttpRequest;
import io.sentry.Breadcrumb;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.protocol.Request;
import io.sentry.protocol.User;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.jetbrains.annotations.NotNull;

public class SentryClient {
  private static final List<String> SENSITIVE_HEADERS =
      Arrays.asList("X-FORWARDED-FOR", "AUTHORIZATION", "COOKIE");

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
      pluginKey = pluginProperties.getPluginKey().orElse(null);
      pluginVersion = pluginProperties.getPluginVersion().orElse(null);

      Sentry.init(
          options -> {
            options.setDsn(dsn);
            if (pluginKey != null && pluginVersion != null) {
              options.setTag("plugin", pluginKey);
              options.setTag("version", pluginVersion);
              options.setRelease(pluginKey + ":" + pluginVersion);
              try {
                options.setEnvironment(new URL(env).getHost());
              } catch (Exception e) {
                // ignored
              }
            }
          });
    }
  }

  public static void capture(Throwable throwable) {
    capture(ExecutingHttpRequest.get(), throwable);
  }

  public static void capture(@Nullable HttpServletRequest request, Throwable throwable) {
    if (isInitialized()) {
      try {
        Sentry.withScope(
            scope -> {
              setScopeUser(scope);

              if (request != null) {
                scope.addBreadcrumb(Breadcrumb.http(request.getRequestURI(), request.getMethod()));
                Request sentryRequest = new Request();
                sentryRequest.setMethod(request.getMethod());
                sentryRequest.setUrl(request.getRequestURI());
                sentryRequest.setQueryString(request.getQueryString());
                sentryRequest.setHeaders(resolveHeadersMap(request));
                scope.setRequest(sentryRequest);
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

  @Nullable
  private static String toString(@Nullable Enumeration<String> values) {
    return values != null ? String.join(",", Collections.list(values)) : null;
  }

  @NotNull
  private static Map<String, String> resolveHeadersMap(@NotNull HttpServletRequest request) {
    Map<String, String> headersMap = new HashMap<>();

    Enumeration<String> iter = request.getHeaderNames();

    while (iter.hasMoreElements()) {
      String headerName = iter.nextElement();
      if (!SENSITIVE_HEADERS.contains(headerName.toUpperCase(Locale.ROOT))) {
        headersMap.put(headerName, toString(request.getHeaders(headerName)));
      }
    }

    return headersMap;
  }
}
