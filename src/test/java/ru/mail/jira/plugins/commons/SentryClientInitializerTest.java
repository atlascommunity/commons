package ru.mail.jira.plugins.commons;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.ApplicationPropertiesImpl;
import com.atlassian.jira.security.JiraAuthenticationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SentryClientInitializerTest {

  private PluginProperties pluginProperties;
  private JiraAuthenticationContext jiraAuthenticationContext;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    jiraAuthenticationContext = mock(JiraAuthenticationContext.class);

    ApplicationProperties applicationProperties = mock(ApplicationPropertiesImpl.class);
    when(applicationProperties.getString(anyString())).thenReturn(null);

    pluginProperties = new PluginProperties(applicationProperties);
  }

  @org.junit.jupiter.api.Test
  void destroy() {

  }

  @org.junit.jupiter.api.Test
  void afterPropertiesSet() throws Exception {
    SentryClientInitializer sentryClientInitializer = new SentryClientInitializer(jiraAuthenticationContext, pluginProperties);
    sentryClientInitializer.afterPropertiesSet();

    SentryClient.capture("test");
  }
}