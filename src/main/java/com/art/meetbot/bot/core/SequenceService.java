package com.art.meetbot.bot.core;

import com.art.meetbot.bot.core.loader.SequenceLoader;
import com.art.meetbot.bot.handle.SequenceHandler;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Service
@Slf4j
public class SequenceService {
    private static final Map<String, SequenceHandler> seqMap = new HashMap<>();
    private final SequenceLoader sequenceLoader;
    private final CommandRegRepo commandRegRepo;

    public SequenceService(SequenceLoader sequenceLoader, CommandRegRepo commandRegRepo) {
        this.sequenceLoader = sequenceLoader;
        this.commandRegRepo = commandRegRepo;
    }

    @PostConstruct
    void init() {
        seqMap.putAll(sequenceLoader.load());
    }

    public Optional<BotApiMethod<? extends BotApiObject>> handle(Message message) {
        Optional<CommandReg> byChatId = commandRegRepo.findByChatId(message.getChatId());


        if (byChatId.isPresent()) {
            if (seqMap.containsKey(byChatId.get().getSeqName())) {
                return Optional.ofNullable(seqMap.get(byChatId.get().getSeqName()).handleCommand(message, byChatId.get().getState()));
            } else {
                log.warn("Sequence not found");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
