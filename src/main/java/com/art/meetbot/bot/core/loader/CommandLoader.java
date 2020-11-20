package com.art.meetbot.bot.core.loader;

import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
public final class CommandLoader {
    private final static ObjectCreator OBJECT_CREATOR = new ObjectCreator();


    private CommandLoader() {
    }

    public static Map<String, RequestHandler> readCommands() {
        Set<Class<?>> annotatedCommands = ClassLoader.getClassesAnnotatedWith(Handler.class);
        annotatedCommands.forEach(ac -> log.info(ac.getCanonicalName()));
        final Map<String, RequestHandler> commandsMap = new HashMap<>();

        final Class<RequestHandler> requiredInterface = RequestHandler.class;

        for (Class<?> clazz : annotatedCommands) {
            if (LoaderUtils.isImplementedInterface(clazz, requiredInterface)) {
                for (Constructor<?> c : clazz.getDeclaredConstructors()) {
                    //noinspection unchecked
                    Constructor<RequestHandler> castedConstructor = (Constructor<RequestHandler>) c;
                    commandsMap.put(extractCommandName(clazz), OBJECT_CREATOR.instantiateClass(castedConstructor));
                }


            } else {
                log.warn("Command didn't implemented: " + requiredInterface.getCanonicalName());

            }
        }

        return commandsMap;
    }

    private static String extractCommandName(Class<?> clazz) {
        Handler handler = clazz.getAnnotation(Handler.class);
        if (handler == null) {
            throw new IllegalArgumentException("Passed class without Handler annotation");
        } else {
            return handler.value();
        }
    }
}
