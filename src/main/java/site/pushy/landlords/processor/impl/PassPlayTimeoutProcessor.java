package site.pushy.landlords.processor.impl;

import org.springframework.stereotype.Service;
import site.pushy.landlords.core.enums.TimeoutStrategy;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.processor.PlayTimeoutProcessor;

@Service
public class PassPlayTimeoutProcessor implements PlayTimeoutProcessor {

    @Override
    public void process(Room room) {
        // TODO: 出牌超时处理
    }

    @Override
    public TimeoutStrategy getStrategy() {
        return TimeoutStrategy.PASS;
    }
}
