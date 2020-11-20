package com.art.meetbot.bot.handle;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
public interface RequestHandler {
    BotApiMethod<Message> execute(Message message);
}
