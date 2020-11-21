package com.art.meetbot.entity.cache;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Entity
@Data
@NoArgsConstructor
public class NameSeqCache {

    @Id
    private Long chatId;

    private String ans1;
    private String ans2;
    private String ans3;
    private String ans4;
    private String ans5;

    public NameSeqCache(Long chatId) {
        this.chatId = chatId;
    }



}
