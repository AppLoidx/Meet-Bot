package com.art.meetbot.entity.repo.cache;

import com.art.meetbot.entity.cache.NameSeqCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
public interface NameSeqCacheRepo extends JpaRepository<NameSeqCache, UUID> {
    Optional<NameSeqCache> findByChatId(Long chatId);
}
