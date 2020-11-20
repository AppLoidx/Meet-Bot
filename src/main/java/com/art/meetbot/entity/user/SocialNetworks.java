package com.art.meetbot.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Entity
public class SocialNetworks {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String vk;
    private String discord;
    private String instagram;
}
