package ru.mail.jira.plugins.commons.spring;

import org.aopalliance.intercept.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;

public class ProxiedBeanPostProcessor implements BeanPostProcessor {
  private final Logger logger = LoggerFactory.getLogger(ProxiedBeanPostProcessor.class);

  private final Class<? extends Annotation> annotationClass;
  private final Interceptor interceptor;

  public ProxiedBeanPostProcessor(
      Class<? extends Annotation> annotationClass, Interceptor interceptor) {
    this.annotationClass = annotationClass;
    this.interceptor = interceptor;
  }

  public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
    return o;
  }

  public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
    try {
      Class<?> beanClass = bean.getClass();

      // ignore 3rd party beans
      if (beanClass.getCanonicalName().startsWith("ru.mail.jira.plugins")) {
        if (AnnotationUtil.isAnnotated(annotationClass, beanClass)) {
          ProxyFactory proxyFactory = new ProxyFactory(bean);
          proxyFactory.addAdvice(interceptor);

          return proxyFactory.getProxy(ProxiedBeanPostProcessor.class.getClassLoader());
        } else return bean;
      }
    } catch (Exception e) {
      logger.warn("Unable to process bean {} - {}", name, bean, e);

      if (e instanceof BeansException) {
        throw e;
      }
    }
    return bean;
  }
}
