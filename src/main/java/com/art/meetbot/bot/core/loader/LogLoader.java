package com.art.meetbot.bot.core.loader;

import com.art.meetbot.bot.handle.Log;
import com.art.meetbot.bot.handle.RequestLogger;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
public class LogLoader {

    private final static ObjectCreator OBJECT_CREATOR = new ObjectCreator();

    private LogLoader() {
    }

    public static Map<String, Set<RequestLogger>> loadLoggers() {

        Set<Class<?>> annotatedLoggers = ClassLoader.getClassesAnnotatedWith(Log.class);

        final Map<String, Set<RequestLogger>> commandsMap = new HashMap<>();
        final Class<RequestLogger> requiredInterface = RequestLogger.class;

        for (Class<?> clazz : annotatedLoggers) {
            if (LoaderUtils.isImplementedInterface(clazz, requiredInterface)) {
                for (Constructor<?> c : clazz.getDeclaredConstructors()) {
                    //noinspection unchecked
                    Constructor<RequestLogger> castedConstructor = (Constructor<RequestLogger>) c;
                    String name = extractCommandName(clazz);
                    commandsMap.computeIfAbsent(name, n -> new HashSet<>());
                    commandsMap.get(extractCommandName(clazz)).add(OBJECT_CREATOR.instantiateClass(castedConstructor));

                }

            } else {
                log.warn("Command didn't implemented: " + requiredInterface.getCanonicalName());
            }
        }

        return commandsMap;
    }

    private static String extractCommandName(Class<?> clazz) {
        Log handler = clazz.getAnnotation(Log.class);
        if (handler == null) {
            throw new IllegalArgumentException("Passed class without " + Log.class.getName() + " annotation");
        } else {
            return handler.value();
        }
    }
}
