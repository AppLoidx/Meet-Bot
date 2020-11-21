package com.art.meetbot.entity.repo.register;

import com.art.meetbot.entity.register.CommandReg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
public interface CommandRegRepo extends JpaRepository<CommandReg, UUID> {
    Optional<CommandReg> findByChatId(Long chatId);
}
