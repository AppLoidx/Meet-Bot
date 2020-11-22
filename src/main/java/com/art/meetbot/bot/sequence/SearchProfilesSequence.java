package com.art.meetbot.bot.sequence;

import com.art.meetbot.bot.MeetBot;
import com.art.meetbot.bot.handle.Sequence;
import com.art.meetbot.bot.handle.SequenceHandler;
import com.art.meetbot.bot.util.KeyboardFactory;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import com.art.meetbot.entity.repo.user.UserRepo;
import com.art.meetbot.entity.user.User;
import com.art.meetbot.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Sequence("searching-profiles-seq")
@Component
public class SearchProfilesSequence implements SequenceHandler {
    private final CommandRegRepo commandRegRepo;
    private final UserRepo userRepo;

    public SearchProfilesSequence(CommandRegRepo commandRegRepo, UserRepo userRepo) {
        this.commandRegRepo = commandRegRepo;
        this.userRepo = userRepo;
    }

    @Override
    public BotApiMethod<? extends BotApiObject> handleCommand(Message message, int state) {
        log.debug("Received command " + message.getText() + " with state : " + state);

        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElseGet(() -> {
                    log.warn("Command reg is not found for chat " + message.getChatId());
                    return new CommandReg();
                });
        if (message.getText().equals("no") || userRepo.count() <= state) {
            commandRegRepo.delete(commandReg);
            return SendMessage.builder()
                    .text("search finished\nFounded:" + (userRepo.count() - 1) + " profile")
                    .chatId(String.valueOf(message.getChatId()))
                    .build();
        }
        List<User> users = userRepo.findAll();
        if (message.getText().equals("accept")) {
            if (state != 0) {
                acceptUser(message.getChatId(), users.get(state));
            } else {
                log.warn("user send accept on 1 question");
            }
        }
        if (users.get(state).getTelegramId().equals(message.getChatId().toString())) {
            state += 1;
            if (state >= users.size()) {
                commandRegRepo.delete(commandReg);
                return SendMessage.builder()
                        .text("search finished\nFounded:" + (users.size() - 1) + " profile")
                        .chatId(String.valueOf(message.getChatId()))
                        .build();
            }
        }

        changeState(state + 1, commandReg);

        UserInfo userInfo = users.get(state).getUserInfo();

        sendPhoto(message.getChatId(), userInfo.getPhotoId());
        String responseText = makeMessageFromUserInfo(userInfo);

        return SendMessage.builder()
                .text(responseText)
                .chatId(String.valueOf(message.getChatId()))
                .replyMarkup(KeyboardFactory.nextAccept())
                .build();

    }

    private void changeState(int newState, CommandReg commandReg) {
        commandReg.setState(newState);
        commandRegRepo.save(commandReg);
    }

    private void acceptUser(Long chatId, User user) {

    }

    private String makeMessageFromUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return "";
        }
        String description = userInfo.getDescription() == null ? "" : userInfo.getDescription();

        return "Next user. \nGender:" + userInfo.getSex() +
                "\nBirth year:" + userInfo.getBirthYear() +
                "\nDescription:" + description;
    }

    private void sendPhoto(Long chatId, String photoId) {
        if (photoId == null) {
            log.debug("Photo not found for user");
            return;
        }

        SendPhoto msg = SendPhoto.builder()
                .chatId(chatId.toString())
                .photo(new InputFile(photoId))
                .caption("Photo")
                .build();
        try {
            MeetBot.instance.execute(msg); // Call method to send the photo
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
