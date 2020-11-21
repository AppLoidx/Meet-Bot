package com.art.meetbot.bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
public final class KeyboardFactory {
    private KeyboardFactory() {}

    public static ReplyKeyboard yesNo() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(List.of(
                button("да", "yes"),
                button("нет", "no"))
        );

        markup.setKeyboard(rowList);

        return markup;
    }

    public static ReplyKeyboard selectGender() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(List.of(
                button("male", "male"),
                button("female", "female"))
        );

        markup.setKeyboard(rowList);

        return markup;
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
