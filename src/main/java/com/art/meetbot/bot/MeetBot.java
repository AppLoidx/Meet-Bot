package com.art.meetbot.bot;

import com.art.meetbot.bot.core.CommandService;
import com.art.meetbot.bot.handle.ExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Slf4j
@Component
public class MeetBot extends TelegramLongPollingBot {

    @Value("${application.env.bot.token}")
    private String token;

    @Value("${application.env.bot.name}")
    private String name;

    private final CommandService commandService;

    public MeetBot(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {

                // run "before" loggers
                commandService.findLoggers(message.getText(), ExecutionTime.BEFORE)
                        .forEach(logger -> logger.execute(message));

                // command execution
                BotApiMethod<Message> response;
                this.execute(response = commandService.serve(message.getText()).execute(message));

                // run "after" loggers
                commandService.findLoggers(message.getText(), ExecutionTime.AFTER)
                        .forEach(logger -> logger.executeAfter(message, response));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
