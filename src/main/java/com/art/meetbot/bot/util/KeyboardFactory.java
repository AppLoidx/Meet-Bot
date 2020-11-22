package com.art.meetbot.bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
public final class KeyboardFactory {
    private KeyboardFactory() {}

    public static ReplyKeyboard yesNo() {

        return simpleMarkupFrom(List.of(
                button("да", "yes"),
                button("нет", "no")
                )
        );
    }

    public static ReplyKeyboard selectGender() {
        return simpleMarkupFrom(List.of(
                button("male", "male"),
                button("female", "female"))
        );
    }

    public static ReplyKeyboard no() {
        return simpleMarkupFrom(List.of(
                button("no", "no")
        ));
    }

    public static ReplyKeyboard nextAccept() {
        return simpleMarkupFrom(List.of(
                button("next", "next"),
                button("accept", "accept"))
        );
    }

    private static InlineKeyboardMarkup simpleMarkupFrom(List<InlineKeyboardButton> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(createRowListFrom(buttons));
        return markup;
    }



    private static List<List<InlineKeyboardButton>> createRowListFrom(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttons);
        return rowList;
    }
    private static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
