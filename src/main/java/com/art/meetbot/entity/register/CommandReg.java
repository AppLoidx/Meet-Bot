package com.art.meetbot.entity.register;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Entity
@NoArgsConstructor
@Data
public class CommandReg {
    @Id
    private Long chatId;

    private Integer state;
    private String seqName;

    public CommandReg(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandReg that = (CommandReg) o;
        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
