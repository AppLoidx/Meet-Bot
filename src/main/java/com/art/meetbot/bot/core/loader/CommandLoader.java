package com.art.meetbot.bot.core.loader;

import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
@Service
public class CommandLoader {

    private final ObjectLoader objectLoader;
    public CommandLoader(ObjectLoader objectLoader) {
        this.objectLoader = objectLoader;
    }

    public Map<String, RequestHandler> readCommands() {

        final Map<String, RequestHandler> commandsMap = new HashMap<>();

        for (Object obj : objectLoader.loadObjectsWithAnnotation(Handler.class)) {
            if (obj instanceof RequestHandler) {
                RequestHandler handler = (RequestHandler) obj;
                commandsMap.put(extractCommandName(handler.getClass()), handler);
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
