package com.art.meetbot.bot.core.loader;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Service
public class ObjectLoader {
    private final ApplicationContext applicationContext;

    public ObjectLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Collection<Object> loadObjectsWithAnnotation(Class<? extends Annotation> annotation) {
        return applicationContext.getBeansWithAnnotation(annotation).values();
    }
}
