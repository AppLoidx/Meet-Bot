package com.art.meetbot.bot.core.loader;

import com.art.meetbot.bot.handle.Log;
import com.art.meetbot.bot.handle.RequestLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
@Service
public class LogLoader {

    private final ObjectLoader objectLoader;

    public LogLoader(ObjectLoader objectLoader) {
        this.objectLoader = objectLoader;
    }

    public Map<String, Set<RequestLogger>> loadLoggers() {

        final Map<String, Set<RequestLogger>> commandsMap = new HashMap<>();

        for (Object obj : objectLoader.loadObjectsWithAnnotation(Log.class)) {
            if (obj instanceof RequestLogger) {
                RequestLogger logger = (RequestLogger) obj;
                final String loggerName = extractCommandName(logger.getClass());
                commandsMap.computeIfAbsent(loggerName, (n) -> new HashSet<>());
                commandsMap.get(loggerName).add(logger);
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
