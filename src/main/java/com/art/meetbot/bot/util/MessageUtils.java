package com.art.meetbot.bot.util;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
public final class MessageUtils {
    private MessageUtils() {}

    public static SendMessage sendText(String text, @NotNull Message source) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(source.getChatId()))
                .build();
    }
}
