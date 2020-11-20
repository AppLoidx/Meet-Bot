package com.art.meetbot.bot.core.loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
public class ObjectCreator {
    public <T> T instantiateClass(Constructor<T> constructor) {
        T commandInstance = null;
        constructor.setAccessible(true);
        if (constructor.getParameterTypes().length == 0) {
            try {
                commandInstance = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return commandInstance;
    }
}
