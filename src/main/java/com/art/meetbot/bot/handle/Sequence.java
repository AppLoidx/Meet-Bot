package com.art.meetbot.bot.handle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Sequence {
    /**
     *
     * @return name of sequence
     */
    String value();
}
