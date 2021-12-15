/* (C)2017-2020 */
package ru.mail.jira.plugins.commons;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class PluginInitializer implements InitializingBean, DisposableBean {
  private final JiraAuthenticationContext jiraAuthenticationContext;
  private final ApplicationProperties applicationProperties;
  private final PluginProperties pluginProperties;

  public PluginInitializer(
      @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
      PluginProperties pluginProperties) {
    this.jiraAuthenticationContext = jiraAuthenticationContext;
    this.applicationProperties = ComponentAccessor.getApplicationProperties();
    this.pluginProperties = pluginProperties;
  }

  @Override
  public void destroy() throws Exception {
    SentryClient.close();
    HttpClient.getPrimaryClient().shutDown();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    HttpClient.init();
    SentryClient.init(
        applicationProperties.getString(APKeys.JIRA_BASEURL),
        jiraAuthenticationContext,
        pluginProperties);
  }
}
