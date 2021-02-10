package ru.mail.jira.plugins.commons.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class AnnotationUtil {
    private AnnotationUtil() {}

    public static boolean isAnnotated(Class<? extends Annotation> annotationClass, Method method) {
        return method != null && (isAnnotationPresent(annotationClass, method) || isAnnotationPresent(annotationClass, method.getDeclaringClass()));
    }

    public static boolean isAnnotated(Class<? extends Annotation> annotationClass, Class c) {
        if (c != null) {
            if (c.isInterface()) {
                if (isAnnotationPresent(annotationClass, c)) {
                    return true;
                }
                for (Method method : c.getMethods()) {
                    if (isAnnotated(annotationClass, method)) {
                        return true;
                    }
                }
            } else {
                if (isAnnotationPresent(annotationClass, c)) {
                    throw new InvalidAnnotationException("Transactional is not supported for concrete classes");
                }
                for (Method method : c.getMethods()) {
                    if (isAnnotated(annotationClass, method)) {
                        throw new InvalidAnnotationException("Transactional is not supported for concrete classes");
                    }
                }
            }

            for (Class ifce : c.getInterfaces()) {
                if (isAnnotated(annotationClass, ifce)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAnnotationPresent(Class<? extends Annotation> annotationClass, AnnotatedElement e) {
        return e.isAnnotationPresent(annotationClass);
    }
}
