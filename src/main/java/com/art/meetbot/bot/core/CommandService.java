package com.art.meetbot.bot.core;

import com.art.meetbot.bot.core.loader.CommandLoader;
import com.art.meetbot.bot.core.loader.LogLoader;
import com.art.meetbot.bot.handle.ExecutionTime;
import com.art.meetbot.bot.handle.Log;
import com.art.meetbot.bot.handle.RequestHandler;
import com.art.meetbot.bot.handle.RequestLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Service
@Slf4j
public class CommandService {
    private static final Map<String, RequestHandler> commandsMap = new HashMap<>();
    private static final Map<String, Set<RequestLogger>> loggersMap = new HashMap<>();
    private final CommandLoader commandLoader;
    private final LogLoader logLoader;

    private CommandService(CommandLoader commandLoader, LogLoader logLoader) {
        this.commandLoader = commandLoader;
        this.logLoader = logLoader;
    }

    @PostConstruct
    public void init() {
        initCommands();
        initLoggers();
    }

    private void initCommands() {
        commandsMap.putAll(commandLoader.readCommands());
        log.info("Total commands : " + commandsMap.toString());
    }

    private void initLoggers() {
        loggersMap.putAll(logLoader.loadLoggers());
    }

    public RequestHandler serve(String message) {
        for (Map.Entry<String, RequestHandler> entry : commandsMap.entrySet()) {
            if (entry.getKey().equals(message)) {
                return entry.getValue();
            }
        }

        return msg -> SendMessage.builder()
                .text("Команда не найдена")
                .chatId(String.valueOf(msg.getChatId()))
                .build();
    }

    public Set<RequestLogger> findLoggers(String message, ExecutionTime executionTime) {
        final Set<RequestLogger> matchedLoggers = new HashSet<>();
        for (Map.Entry<String, Set<RequestLogger>> entry : loggersMap.entrySet()) {
            for (RequestLogger logger : entry.getValue()) {

                if (containsExecutionTime(extractExecutionTimes(logger), executionTime) ) {
                    if (message.matches(entry.getKey()))
                        matchedLoggers.add(logger);
                }
            }

        }

        return matchedLoggers;
    }

    private static ExecutionTime[] extractExecutionTimes(RequestLogger logger) {
        return logger.getClass().getAnnotation(Log.class).executionTime();
    }

    private static boolean containsExecutionTime(ExecutionTime[] times, ExecutionTime executionTime) {
        for (ExecutionTime et : times) {
            if (et == executionTime) return true;
        }

        return false;
    }

}
