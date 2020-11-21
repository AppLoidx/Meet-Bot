package com.art.meetbot.bot.commands;

import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import com.art.meetbot.bot.util.MessageUtils;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Handler("seq")
@Component
@Slf4j
public class SequenceCommand implements RequestHandler {
    private final CommandRegRepo commandRegRepo;

    public SequenceCommand(CommandRegRepo commandRegRepo) {
        this.commandRegRepo = commandRegRepo;
    }

    @Override
    public BotApiMethod<Message> execute(Message message) {
        log.info("Start sequence");
        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElse(new CommandReg(message.getChatId()));

        commandReg.setState(0);
        commandReg.setSeqName("example-seq");
        commandRegRepo.save(commandReg);
        return MessageUtils.sendText("Let's start", message);
    }
}
