package ru.mail.jira.plugins.commons;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.ApplicationPropertiesImpl;
import com.atlassian.jira.mock.component.MockComponentWorker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginPropertiesTest {
    private static PluginProperties pluginProperties;

    @BeforeAll
    static void setup() {
        MockComponentWorker componentAccessorWorker = new MockComponentWorker();
        ComponentAccessor.initialiseWorker(componentAccessorWorker);
        ApplicationProperties applicationProperties = mock(ApplicationPropertiesImpl.class);
        when(applicationProperties.getString(anyString())).thenReturn(null);

        pluginProperties = new PluginProperties(applicationProperties);
    }

    @Test
    void getProperty_AssertionSuccess() {
        Optional<String> value = pluginProperties.getString("pluginName");
        assertTrue(value.isPresent());
        assertEquals("${project.name}", value.get());
    }

    @Test
    void getIntProperty_AssertionSuccess() {
        Optional<Integer> value = pluginProperties.getInt("int");
        assertTrue(value.isPresent());
        assertEquals(123, value.get());
    }

    @Test
    void getDoubleProperty_AssertionSuccess() {
        Optional<Double> value = pluginProperties.getDouble("double");
        assertTrue(value.isPresent());
        assertEquals(123.45, value.get());
    }

    @Test
    void getBooleanProperty_AssertionSuccess() {
        Optional<Boolean> value = pluginProperties.getBoolean("boolean");
        assertTrue(value.isPresent());
        assertTrue(value.get());
    }

    @Test
    void getNonExistProperty_AssertionEmptyOption() {
        assertFalse(pluginProperties.getString("nonExist").isPresent());
    }

    @Test
    void getNullKeyProperty_AssertionEmptyOption() {
        assertFalse(pluginProperties.getString(null).isPresent());
    }

    @Test
    void getNullKeyDefaultProperty_AssertionEmptyOption() {
        assertEquals("defaultValue", pluginProperties.getString(null,"defaultValue"));
    }

}
