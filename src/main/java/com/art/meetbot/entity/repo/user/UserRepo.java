package com.art.meetbot.entity.repo.user;

import com.art.meetbot.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 20.11.2020
 */
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByTelegramId(String telegramId);
}
