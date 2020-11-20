package com.art.meetbot.bot.handle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default ".*";

    ExecutionTime[] executionTime() default ExecutionTime.BEFORE;
}
