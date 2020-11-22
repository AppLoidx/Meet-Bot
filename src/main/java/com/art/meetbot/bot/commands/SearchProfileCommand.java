package com.art.meetbot.bot.commands;

import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import com.art.meetbot.bot.util.MessageUtils;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import com.art.meetbot.entity.repo.user.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Handler("/search")
@Component
@Slf4j
public class SearchProfileCommand implements RequestHandler {
    private final CommandRegRepo commandRegRepo;

    public SearchProfileCommand(CommandRegRepo commandRegRepo, UserRepo userRepo) {
        this.commandRegRepo = commandRegRepo;
    }

    @Override
    public BotApiMethod<Message> execute(Message message) {
        log.info("Start searching for user with chatId" + message.getChatId());
        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElse(new CommandReg(message.getChatId()));

        commandReg.setState(0);
        commandReg.setSeqName("searching-profiles-seq");
        commandRegRepo.save(commandReg);
        return MessageUtils.sendText("Let's start ", message);
    }
}
