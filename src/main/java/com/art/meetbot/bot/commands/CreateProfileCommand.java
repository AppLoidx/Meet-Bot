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

@Handler("/createprofile")
@Component
@Slf4j
public class CreateProfileCommand implements RequestHandler {
    private final CommandRegRepo commandRegRepo;
    private final UserRepo userRepo;

    public CreateProfileCommand(CommandRegRepo commandRegRepo, UserRepo userRepo) {
        this.commandRegRepo = commandRegRepo;
        this.userRepo = userRepo;
    }

    @Override
    public BotApiMethod<Message> execute(Message message) {
        log.info("Start creating profile for user with chatId" + message.getChatId());
        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElse(new CommandReg(message.getChatId()));

        commandReg.setState(0);
        commandReg.setSeqName("create-profile-seq");
        commandRegRepo.save(commandReg);

        // remove old user data
        userRepo.findByTelegramId(String.valueOf(message.getChatId()))
                .ifPresent(userRepo::delete);

        return MessageUtils.sendText("Let's start creating a profile. \nAnswer a series of questions. \n\nRemember that this data will be seen by other users \n Old profile deleted \n Enter your date of birth", message);
    }
}
