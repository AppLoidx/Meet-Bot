package com.art.meetbot.bot.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
public final class ClassLoader {
    private ClassLoader() {}

    public static Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> clazz) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(clazz));

        Set<Class<?>> annotatedLoggers = new HashSet<>();

        for (BeanDefinition bd : scanner.findCandidateComponents("com.art.meetbot")){
            try {
                log.debug("Found class " + bd.getBeanClassName());
                annotatedLoggers.add(Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                log.warn("While creating annotated loggers set", e);
            }
        }

        return annotatedLoggers;

    }
}
