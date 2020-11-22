package com.art.meetbot.entity.repo.user;

import com.art.meetbot.entity.user.MatchedPeople;
import com.art.meetbot.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchedPeopleRepo extends JpaRepository<MatchedPeople, UUID> {
    List<MatchedPeople> findAllByTelegramIdFirstIsOrTelegramIdSecondIs(String id1, String id2);
}
