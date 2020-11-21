package com.art.meetbot.bot.sequence;

import com.art.meetbot.bot.handle.Sequence;
import com.art.meetbot.bot.handle.SequenceHandler;
import com.art.meetbot.bot.util.KeyboardFactory;
import com.art.meetbot.bot.util.MessageUtils;
import com.art.meetbot.entity.cache.NameSeqCache;
import com.art.meetbot.entity.register.CommandReg;
import com.art.meetbot.entity.repo.cache.NameSeqCacheRepo;
import com.art.meetbot.entity.repo.register.CommandRegRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Slf4j
@Sequence("example-seq")
@Component
public class ExampleSequence implements SequenceHandler {
    private final CommandRegRepo commandRegRepo;
    private final NameSeqCacheRepo nameSeqCacheRepo;

    public ExampleSequence(CommandRegRepo commandRegRepo, NameSeqCacheRepo nameSeqCacheRepo) {
        this.commandRegRepo = commandRegRepo;
        this.nameSeqCacheRepo = nameSeqCacheRepo;
    }

    @Override
    public BotApiMethod<Message> handleCommand(Message message, int state) {
        log.debug("Received command " + message.getText() + " with state : " + state);

        CommandReg commandReg = commandRegRepo.findByChatId(message.getChatId())
                .orElseGet(() -> {
                    log.warn("Command reg is not found for chat " + message.getChatId());
                    return new CommandReg();
                });

        NameSeqCache nameSeqCache =
                nameSeqCacheRepo.findByChatId(message.getChatId()).orElse(new NameSeqCache(message.getChatId()));
        switch (state) {
            case 0 -> {
                nameSeqCache.setAns1(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(1, commandReg);
                return MessageUtils.sendText("Now you are on state 1", message);
            }
            case 1 -> {
                nameSeqCache.setAns2(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(2, commandReg);
                return MessageUtils.sendText("Wow, you are on state 2 now", message);
            }
            case 2 -> {
                nameSeqCache.setAns3(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(3, commandReg);
                return MessageUtils.sendText("Nah, it's boring...", message);
            }
            case 3 -> {
                nameSeqCache.setAns4(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(4, commandReg);
                return MessageUtils.sendText("This is the last state, y knw?", message);
            }
            case 4 -> {
                nameSeqCache.setAns5(message.getText());
                nameSeqCacheRepo.save(nameSeqCache);
                changeState(5, commandReg);
                final String msg = "And you last input is : " + message.getText() +
                                   ". Do you want to see another your inputs?";

                return SendMessage.builder()
                        .text(msg)
                        .chatId(String.valueOf(message.getChatId()))
                        .replyMarkup(KeyboardFactory.yesNo())
                        .build();
            }
            case 5 -> {
                if (message.getText().equals("yes")) {
                    final String msg = compileFinalAnswer(nameSeqCache);
                    nameSeqCacheRepo.delete(nameSeqCache);
                    commandRegRepo.delete(commandReg);

                    return MessageUtils.sendText(msg, message);
                } else {
                    nameSeqCacheRepo.delete(nameSeqCache);
                    commandRegRepo.delete(commandReg);
                    return MessageUtils.sendText("Okay", message);
                }
            }

        }

        return null;
    }

    private String compileFinalAnswer(NameSeqCache nameSeqCache) {
        return nameSeqCache.getAns1() + "\n" + nameSeqCache.getAns2() + "\n"
                + nameSeqCache.getAns3() + "\n" + nameSeqCache.getAns4() +
               "\n" + nameSeqCache.getAns5();
    }

    private void changeState(int newState, CommandReg commandReg) {
        commandReg.setState(newState);
        commandRegRepo.save(commandReg);
    }

}
