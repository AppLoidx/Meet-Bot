package com.art.meetbot.bot.sequence;

import com.art.meetbot.bot.handle.Sequence;
import com.art.meetbot.bot.handle.SequenceHandler;
import com.art.meetbot.bot.util.KeyboardFactory;
import com.art.meetbot.bot.util.MessageUtils;
import com.art.meetbot.entity.cache.NameSeqCache;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.cache.NameSeqCacheRepo;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import com.art.meetbot.entity.repo.user.UserRepo;
import com.art.meetbot.entity.user.Sex;
import com.art.meetbot.entity.user.User;
import com.art.meetbot.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Sequence("create-profile-seq")
@Component
public class CreateProfileSequence implements SequenceHandler {
    private final CommandRegRepo commandRegRepo;
    private final NameSeqCacheRepo nameSeqCacheRepo;
    private final UserRepo userRepo;

    public CreateProfileSequence(CommandRegRepo commandRegRepo, NameSeqCacheRepo nameSeqCacheRepo, UserRepo userRepo) {
        this.commandRegRepo = commandRegRepo;
        this.nameSeqCacheRepo = nameSeqCacheRepo;
        this.userRepo = userRepo;
    }

    //A lot of duplicate code at this method
    //TODO delete duplicate code
    @Override
    public BotApiMethod<? extends BotApiObject> handleCommand(Message message, int state) {
        log.debug("Received command " + message.getText() + " with state : " + state);

        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElseGet(() -> {
                    log.warn("Command reg is not found for chat " + message.getChatId());
                    return new CommandReg();
                });

        NameSeqCache nameSeqCache =
                nameSeqCacheRepo.findByChatId(message.getChatId()).orElse(new NameSeqCache(message.getChatId()));

        User user = userRepo.findByTelegramId(message.getChatId().toString())
                .orElse(new User(String.valueOf(message.getChatId())));

        switch (state) {
            case 0 -> {
                nameSeqCache.setAns1(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);

                user.setUserInfo(new UserInfo());

                try {
                    user.getUserInfo().setBirthYear(Integer.parseInt(message.getText()));
                    changeState(1, commandReg);
                } catch (NumberFormatException ignored) {
                    log.debug("user send not a year of birth with id " + message.getChatId());
                    return MessageUtils.sendText("Please, input a number!", message);
                }

                userRepo.save(user);
                return SendMessage.builder()
                        .text("Select gender")
                        .chatId(String.valueOf(message.getChatId()))
                        .replyMarkup(KeyboardFactory.selectGender())
                        .build();
            }
            case 1 -> {
                nameSeqCache.setAns2(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(2, commandReg);
                user.getUserInfo().setSex(Sex.getGender(message.getText()));

                userRepo.save(user);
                return MessageUtils.sendText("Send your photo", message);
            }
            case 2 -> {
                if (message.hasPhoto()) {
                    receivedPhoto(message);
                }

                userRepo.save(user);
                deleteSeq(commandReg, nameSeqCache);
                return SendMessage.builder()
                        .text("Profile ready")
                        .chatId(String.valueOf(message.getChatId()))
                        .build();
            }
        }

        log.warn("something strange");
        deleteSeq(commandReg, nameSeqCache);
        return SendMessage.builder()
                .text("Command not found")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }

    private void deleteSeq(CommandReg commandReg, NameSeqCache nameSeqCache) {
        nameSeqCacheRepo.delete(nameSeqCache);
        commandRegRepo.delete(commandReg);
    }

    private void receivedPhoto(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        // Get largest photo's file_id
        String photo_id = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElseThrow().getFileId();

        log.debug("Upload photo to database");
        User dbUser = userRepo.findByTelegramId(message.getChatId().toString()).orElseGet(() -> {
            log.warn("User with " + message.getChatId() + " not founded in db");
            User user = new User();
            user.setTelegramId(message.getChatId().toString());
            user.setUserInfo(new UserInfo());
            userRepo.save(user);
            return user;
        });

        dbUser.getUserInfo().setPhotoId(photo_id);
        userRepo.save(dbUser);
    }

    private void changeState(int newState, CommandReg commandReg) {
        commandReg.setState(newState);
        commandRegRepo.save(commandReg);
    }

}
