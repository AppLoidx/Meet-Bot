package com.art.meetbot.entity.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
public class MatchedPeople {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String telegramIdFirst;

    private String telegramIdSecond;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchedPeople that = (MatchedPeople) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(telegramIdFirst, that.telegramIdFirst) &&
                Objects.equals(telegramIdSecond, that.telegramIdSecond);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, telegramIdFirst, telegramIdSecond);
    }
}
