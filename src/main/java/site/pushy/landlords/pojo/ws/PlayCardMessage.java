package site.pushy.landlords.pojo.ws;

import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/3 19:26
 */
public class PlayCardMessage extends Message {

    private String userId;

    private List<Card> cardList;

    public PlayCardMessage(String userId, List<Card> cardList) {
        this.userId = userId;
        this.cardList = cardList;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLAY_CARD;
    }
}
