package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;

import java.util.List;

/**
 * 有玩家出牌消息通知
 *
 * @author Pushy
 * @since 2019/2/3 19:26
 */
@Data
public class PlayCardMessage extends Message {

    private User user;

    private List<Card> cardList;

    public PlayCardMessage(User user, List<Card> cardList) {
        this.user = user;
        this.cardList = cardList;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLAY_CARD;
    }
}
