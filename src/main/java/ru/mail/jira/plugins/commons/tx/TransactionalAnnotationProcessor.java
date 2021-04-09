package ru.mail.jira.plugins.commons.tx;

import org.springframework.stereotype.Component;
import ru.mail.jira.plugins.commons.spring.ProxiedBeanPostProcessor;

/** Если в джире задана проперти jira.db.txns.disabled = true, транзакции работать не будут */
@Component
public final class TransactionalAnnotationProcessor extends ProxiedBeanPostProcessor {
  public TransactionalAnnotationProcessor() {
    super(TransactionalInterceptor.ANNOTATION_CLASS, new TransactionalInterceptor());
  }
}
