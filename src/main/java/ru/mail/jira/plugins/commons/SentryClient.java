package ru.mail.jira.plugins.commons;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.sun.jersey.api.core.HttpRequestContext;
import io.sentry.Breadcrumb;
import io.sentry.Scope;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
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
    capture(null, null, throwable);
  }

  public static void capture(
      @Nullable Request request, @Nullable UriInfo uriInfo, Throwable throwable) {
    if (isInitialized()) {
      try {
        Sentry.withScope(
            scope -> {
              setScopeUser(scope);

              if (uriInfo != null && request != null) {
                scope.addBreadcrumb(
                    Breadcrumb.http(uriInfo.getAbsolutePath().toString(), request.getMethod()));
              } else if (uriInfo != null) {
                scope.setTag("url", uriInfo.getAbsolutePath().toString());
              } else if (request != null) {
                scope.setTag("method", request.getMethod());
              }

              if (request instanceof HttpRequestContext) {
                HttpRequestContext httpRequest = (HttpRequestContext) request;
                io.sentry.protocol.Request sentryRequest = new io.sentry.protocol.Request();
                sentryRequest.setMethod(httpRequest.getMethod());
                sentryRequest.setUrl(httpRequest.getRequestUri().toString());
                sentryRequest.setQueryString(toString(httpRequest.getQueryParameters()));
                sentryRequest.setHeaders(resolveHeadersMap(httpRequest));
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
  private static String toString(@Nullable List<String> values) {
    return values != null ? String.join(",", values) : null;
  }

  @Nullable
  private static String toString(@Nullable MultivaluedMap<String, String> values) {
    try {
      return values != null
          ? values.entrySet().stream()
              .map(e -> e.getKey() + "=" + e.getValue())
              .collect(Collectors.joining("&"))
          : null;
    } catch (Exception e) {
      return null;
    }
  }

  @NotNull
  private static Map<String, String> resolveHeadersMap(@NotNull HttpRequestContext request) {
    Map<String, String> headersMap = new HashMap<>();

    request
        .getRequestHeaders()
        .keySet()
        .forEach(
            headerName -> {
              if (!SENSITIVE_HEADERS.contains(headerName.toUpperCase(Locale.ROOT))) {
                headersMap.put(headerName, toString(request.getRequestHeader(headerName)));
              }
            });

    return headersMap;
  }
}
