package site.pushy.landlords.core.processor;

import site.pushy.landlords.core.enums.TimeoutStrategy;
import site.pushy.landlords.pojo.Room;

/**
 * 出牌超时处理器
 */
public interface PlayTimeoutProcessor {

    void process(Room room);

    TimeoutStrategy getStrategy();
}
