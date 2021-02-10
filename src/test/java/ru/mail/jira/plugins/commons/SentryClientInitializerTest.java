package ru.mail.jira.plugins.commons;

import static org.junit.jupiter.api.Assertions.*;

class SentryClientInitializerTest {

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
  }

  @org.junit.jupiter.api.Test
  void destroy() {

  }

  @org.junit.jupiter.api.Test
  void afterPropertiesSet() throws Exception {
    SentryClientInitializer sentryClientInitializer = new SentryClientInitializer();
    sentryClientInitializer.afterPropertiesSet();

    SentryClient.capture("test");
  }
}