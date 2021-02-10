package ru.mail.jira.plugins.commons;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSender {
  private static final Logger log = LoggerFactory.getLogger(HttpSender.class);

  private final String url;
  private final Map<String, String> headers = new HashMap<>();
  private String user;
  private String password;

  public HttpSender(String url, Object... params) {
    this.url = CommonUtils.formatUrl(url, params);
  }

  public HttpSender setAuthenticationInfo(String user, String password) {
    this.user = user;
    this.password = password;
    return this;
  }

  public HttpSender setHeader(String header, String value) {
    headers.put(header, value);
    return this;
  }

  public HttpSender setContentTypeJson() {
    setHeader("Content-Type", "application/json; charset=utf-8");
    return this;
  }

  private String getAuthRealm() {
    return DatatypeConverter.printBase64Binary(user.concat(":").concat(password).getBytes());
  }

  private String send(String method, String body, Integer connectTimeout, Integer readTimeout) throws IOException {
    log.info(String.format("Sending HTTP request: %s", url));

    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    try {
      connection.setDoInput(true);
      connection.setDoOutput(StringUtils.isNotEmpty(body));
      connection.setAllowUserInteraction(true);
      connection.setRequestMethod(method);
      connection.setConnectTimeout(connectTimeout != null ? connectTimeout : 30000);
      connection.setReadTimeout(readTimeout != null ? readTimeout : 30000);
      if (StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password)) {
        connection.setRequestProperty("Authorization", "Basic " + getAuthRealm());
      }
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        connection.setRequestProperty(entry.getKey(), entry.getValue());
      }
      if (StringUtils.isNotEmpty(body)) {
        IOUtils.write(body, connection.getOutputStream());
      }

      int rc = connection.getResponseCode();
      if (rc == HttpURLConnection.HTTP_OK) {
        String response = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
        log.debug(String.format("HTTP response body:\n %s", response));
        return response;
      } else {
        String response = connection.getErrorStream() != null ? IOUtils.toString(connection.getErrorStream(),
            StandardCharsets.UTF_8) : "";
        log.debug(String.format("HTTP error stream:\n %s", response));
        throw new HttpSenderException(rc, body, response);
      }
    } finally {
      connection.disconnect();
    }
  }

  public String sendGet() throws IOException {
    return send("GET", null, null, null);
  }

  public String sendGet(Integer connectTimeout, Integer readTimeout) throws IOException {
    return send("GET", null, connectTimeout, readTimeout);
  }

  public String sendGet(String body) throws IOException {
    return send("GET", body, null, null);
  }

  public String sendGet(String body, Integer connectTimeout, Integer readTimeout) throws IOException {
    return send("GET", body, connectTimeout, readTimeout);
  }

  public String sendPost(String body) throws IOException {
    return send("POST", body, null, null);
  }

  public String sendPost(String body, Integer connectTimeout, Integer readTimeout) throws IOException {
    return send("POST", body, connectTimeout, readTimeout);
  }
}
