package ru.mail.jira.plugins.commons;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PluginProperties {
    private final ApplicationProperties applicationProperties;
    private final PluginPropertyReader pluginPropertyReader;

    private static PluginProperties instance;

    public PluginProperties(@ComponentImport ApplicationProperties applicationProperties) {
        this.pluginPropertyReader = new PluginPropertyReader();
        this.applicationProperties = applicationProperties;
    }

    public Optional<String> getString(String key) {
        if (key == null) return Optional.empty();
        String value = applicationProperties.getString(key);
        if (value == null || value.equals("")) {
            return pluginPropertyReader.getString(key);
        }
        return Optional.of(value);
    }

    public String getString(String key, String defaultValue) {
        return getString(key).orElse(defaultValue);
    }

    public Optional<Integer> getInt(String key) {
        if (key == null) return Optional.empty();
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

    public Integer getInt(String key, int defaultValue) {
        return getInt(key).orElse(defaultValue);
    }

    public Optional<Long> getLong(String key) {
        if (key == null) return Optional.empty();
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

    public Long getLong(String key, long defaultValue) {
        return getLong(key).orElse(defaultValue);
    }

    public Optional<Double> getDouble(String key) {
        if (key == null) return Optional.empty();
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

    public Double getDouble(String key, double defaultValue) {
        return getDouble(key).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String key) {
        if (key == null) return Optional.empty();
        String value = applicationProperties.getString(key);
        if (value == null || value.equals("")) {
            return pluginPropertyReader.getBoolean(key);
        }
        return Optional.of(Boolean.parseBoolean(value));
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key).orElse(defaultValue);
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
