package ru.mail.jira.plugins.commons;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.ApplicationPropertiesImpl;
import com.atlassian.jira.mock.component.MockComponentWorker;
import com.atlassian.jira.security.JiraAuthenticationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PluginInitializerTest {

  private PluginProperties pluginProperties;
  private JiraAuthenticationContext jiraAuthenticationContext;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    jiraAuthenticationContext = mock(JiraAuthenticationContext.class);

    MockComponentWorker componentAccessorWorker = new MockComponentWorker();
    ComponentAccessor.initialiseWorker(componentAccessorWorker);
    ApplicationProperties applicationProperties = mock(ApplicationPropertiesImpl.class);
    when(applicationProperties.getString(anyString())).thenReturn(null);

    pluginProperties = new PluginProperties();
  }

  @org.junit.jupiter.api.Test
  void destroy() {

  }

  @org.junit.jupiter.api.Test
  void afterPropertiesSet() throws Exception {
    PluginInitializer pluginInitializer = new PluginInitializer(jiraAuthenticationContext, pluginProperties);
    pluginInitializer.afterPropertiesSet();

    SentryClient.capture("test");
  }
}