package com.art.meetbot.entity.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Entity
@Data
public class SocialNetworks {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String vk;
    private String instagram;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialNetworks that = (SocialNetworks) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(vk, that.vk) &&
                Objects.equals(instagram, that.instagram);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, vk, instagram);
    }
}
