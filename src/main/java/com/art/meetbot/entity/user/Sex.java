package com.art.meetbot.entity.user;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
public enum Sex {
    MALE, FEMALE, ANOTHER;

    public static Sex getGender(String text) {
        return switch (text) {
            case ("male") -> MALE;
            case ("female") -> FEMALE;
            default -> ANOTHER;
        };
    }
}
