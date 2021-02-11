/* (C)2017-2020 */
package ru.mail.jira.plugins.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class SentryClientInitializer implements InitializingBean, DisposableBean {

  private static final Logger log = LoggerFactory.getLogger(SentryClientInitializer.class);
  private static final String DSN =
      "http://488a7fa73b2343afb523a7139a7226fa:08f1454a8d514306a3cd2837cfc080a3@sentry.intdev.devmail.ru/19";

  @Override
  public void destroy() throws Exception {
    log.info("Stop SentryClient");
    SentryClient.close();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("Init SentryClient");
    SentryClient.init(DSN);
  }
}