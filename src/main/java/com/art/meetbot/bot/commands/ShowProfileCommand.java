package com.art.meetbot.bot.commands;

import com.art.meetbot.bot.MeetBot;
import com.art.meetbot.bot.handle.Handler;
import com.art.meetbot.bot.handle.RequestHandler;
import com.art.meetbot.bot.util.MessageUtils;
import com.art.meetbot.entity.repo.user.UserRepo;
import com.art.meetbot.entity.user.User;
import com.art.meetbot.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

/**
 * @author Arthur Kupriyanov on 22.11.2020
 */
@Handler("/profile")
@Component
@Slf4j
public class ShowProfileCommand implements RequestHandler {

    private final UserRepo userRepo;

    public ShowProfileCommand(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public BotApiMethod<Message> execute(Message message) {
        Optional<User> byTelegramId = userRepo.findByTelegramId(String.valueOf(message.getChatId()));

        return byTelegramId.map(user -> {

                    try {
                        MeetBot.instance.execute(SendPhoto.builder()
                                .photo(new InputFile(user.getUserInfo().getPhotoId()))
                                .chatId(String.valueOf(message.getChatId()))
                                .build());
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    return SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .text("Your profile:\n\n" + profileInfo(user))
                    .build();
        } ).orElse(
                MessageUtils.sendText("You don't have a profile. Please, use command /createprofile", message)
        );

    }

    private String profileInfo(User user) {
        UserInfo userInfo = user.getUserInfo();
        String answer = "Birth year: " + userInfo.getBirthYear();
        answer += "\nSex: " + userInfo.getSex().toString();

        return answer;

    }
}
