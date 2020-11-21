package com.art.meetbot.bot.core.loader;

import com.art.meetbot.bot.handle.RequestHandler;
import com.art.meetbot.bot.handle.Sequence;
import com.art.meetbot.bot.handle.SequenceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov on 21.11.2020
 */
@Slf4j
@Service
public class SequenceLoader {
    private final ApplicationContext applicationContext;

    public SequenceLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Map<String, SequenceHandler> load() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Sequence.class);

        Map<String, SequenceHandler> sequenceMap = new HashMap<>();

        for (Object obj : beans.values()) {
            if (obj instanceof SequenceHandler) {
                Sequence sequence = obj.getClass().getAnnotation(Sequence.class);
                sequenceMap.put(sequence.value(), (SequenceHandler) obj);
            } else {
                log.warn("Command not implemented SequenceHandler " + obj.getClass().getCanonicalName());
            }
        }
        log.info(sequenceMap.toString());
        return sequenceMap;
    }
}
