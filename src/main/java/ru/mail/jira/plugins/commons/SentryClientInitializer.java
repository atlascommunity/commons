/* (C)2017-2020 */
package ru.mail.jira.plugins.commons;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class SentryClientInitializer implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(SentryClientInitializer.class);

    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PluginProperties pluginProperties;

    public SentryClientInitializer(
            @ComponentImport JiraAuthenticationContext jiraAuthenticationContext,
            PluginProperties pluginProperties) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.pluginProperties = pluginProperties;
    }


    @Override
    public void destroy() throws Exception {
        log.info("Stop SentryClient");
        SentryClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Init SentryClient");
        SentryClient.init(jiraAuthenticationContext, pluginProperties);
    }
}
