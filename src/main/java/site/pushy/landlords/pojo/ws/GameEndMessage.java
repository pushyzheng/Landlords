package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @author Pushy
 * @since 2019/2/5 22:39
 */
@Data
public class GameEndMessage extends Message {

    private IdentityEnum winingIdentity;  // 胜利方的身份

    private boolean winning;            // 该玩家是否胜利

    private List<Map<String, Integer>> resList;

    public String getWiningIdentityName() {
        return winingIdentity.getName();
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.GAME_END;
    }
}
