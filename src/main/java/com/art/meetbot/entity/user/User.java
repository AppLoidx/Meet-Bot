package com.art.meetbot.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
@Entity(name="users")
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue
    private UUID uuid;

    @Unique
    private String telegramId;

    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo = new UserInfo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
