package site.pushy.landlords.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pushy
 * @since 2019/1/14 17:29
 */
@AllArgsConstructor
@Getter
public enum RoomStatusEnum {

    PLAYING("游戏中"),

    PREPARING("准备中");

    private final String value;
}
