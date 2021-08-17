package ru.mail.jira.plugins.commons;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PluginProperties {
  private final ApplicationProperties applicationProperties;
  private final PluginPropertyReader pluginPropertyReader;

  public PluginProperties() {
    this.pluginPropertyReader = new PluginPropertyReader();
    this.applicationProperties = ComponentAccessor.getApplicationProperties();
  }

  private String buildPropertyKey(String name) {
    return getPluginKey().map(pluginKey -> String.format("%s.%s", pluginKey, name)).orElse(name);
  }

  public String[] getStringArray(String name) {
    if (name == null) return new String[0];
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      value = pluginPropertyReader.getString(key).orElse("");
    }
    return StringUtils.split(value, ",");
  }

  public Optional<String> getString(String name) {
    if (name == null) return Optional.empty();
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      return pluginPropertyReader.getString(key);
    }
    return Optional.of(value);
  }

  public String getString(String name, String defaultValue) {
    return getString(name).orElse(defaultValue);
  }

  public Optional<Integer> getInt(String name) {
    if (name == null) return Optional.empty();
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      return pluginPropertyReader.getInt(key);
    }
    try {
      return Optional.of(Integer.parseInt(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public Integer getInt(String name, int defaultValue) {
    return getInt(name).orElse(defaultValue);
  }

  public Optional<Long> getLong(String name) {
    if (name == null) return Optional.empty();
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      return pluginPropertyReader.getLong(key);
    }
    try {
      return Optional.of(Long.parseLong(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public Long getLong(String name, long defaultValue) {
    return getLong(name).orElse(defaultValue);
  }

  public Optional<Double> getDouble(String name) {
    if (name == null) return Optional.empty();
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      return pluginPropertyReader.getDouble(key);
    }
    try {
      return Optional.of(Double.parseDouble(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public Double getDouble(String name, double defaultValue) {
    return getDouble(name).orElse(defaultValue);
  }

  public Optional<Boolean> getBoolean(String name) {
    if (name == null) return Optional.empty();
    String key = buildPropertyKey(name);
    String value = applicationProperties.getString(key);
    if (value == null || value.equals("")) {
      return pluginPropertyReader.getBoolean(key);
    }
    return Optional.of(Boolean.parseBoolean(value));
  }

  public Boolean getBoolean(String name, boolean defaultValue) {
    return getBoolean(name).orElse(defaultValue);
  }

  public Optional<String> getPluginKey() {
    return pluginPropertyReader.getString("pluginKey");
  }

  public Optional<String> getPluginName() {
    return pluginPropertyReader.getString("pluginName");
  }

  public Optional<String> getPluginVersion() {
    return pluginPropertyReader.getString("pluginVersion");
  }

  public Optional<String> getSentryDsn() {
    return pluginPropertyReader.getString("sentryDsn");
  }

  public void setString(String key, String val) {
    applicationProperties.setString(key, val);
  }

  public void setOption(String key, boolean val) {
    applicationProperties.setOption(key, val);
  }
}
