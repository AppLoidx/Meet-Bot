package com.art.meetbot.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Entity
@Data
public class UserInfo {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String description;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private int birthYear;
    private String photoId;

    @OneToOne(cascade = CascadeType.ALL)
    private SocialNetworks socialNetworks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(uuid, userInfo.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
