package ru.mail.jira.plugins.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class PluginPropertyReader {
  private static final Logger log = LoggerFactory.getLogger(PluginPropertyReader.class);
  private Properties props;

  public PluginPropertyReader() {
    try (InputStream is = PluginPropertyReader.class.getResourceAsStream("/plugin.properties")) {
      if (is != null) {
        props = new Properties();
        props.load(is);
      }
    } catch (IOException e) {
      log.error("Unable to load plugin.properties", e);
    }
  }

  public Optional<String> getString(String key) {
    if (props != null) {
      return Optional.ofNullable(props.getProperty(key));
    }
    return Optional.empty();
  }

  public Optional<Integer> getInt(String key) {
    return getString(key)
        .map(
            (val) -> {
              try {
                return Integer.parseInt(val);
              } catch (Exception e) {
                return null;
              }
            });
  }

  public Optional<Double> getDouble(String key) {
    return getString(key)
        .map(
            (val) -> {
              try {
                return Double.parseDouble(val);
              } catch (Exception e) {
                return null;
              }
            });
  }

  public Optional<Long> getLong(String key) {
    return getString(key)
        .map(
            (val) -> {
              try {
                return Long.parseLong(val);
              } catch (Exception e) {
                return null;
              }
            });
  }

  public Optional<Boolean> getBoolean(String key) {
    return getString(key).map(Boolean::parseBoolean);
  }
}
