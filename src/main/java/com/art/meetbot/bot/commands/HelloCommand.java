package com.art.meetbot.bot.commands;

import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Handler("hello")
public class HelloCommand implements RequestHandler {
    @Override
    public BotApiMethod<Message> execute(Message message) {
        return SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text("Yahoo!")
                .build();
    }
}
