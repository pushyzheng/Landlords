package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;

import java.util.Collections;
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

    private TypeEnum cardType;

    private CardNumberEnum number;

    public PlayCardMessage(User user, List<Card> cardList, TypeEnum cardType) {
        this.user = user;
        Collections.sort(cardList);
        this.cardList = cardList;
        this.cardType = cardType;
        if (cardType == TypeEnum.SINGLE || cardType == TypeEnum.PAIR) {
            this.number = cardList.get(0).getNumber();
        }
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLAY_CARD;
    }

    public String getCardTypeName() {
        return cardType == null ? "" : cardType.getName();
    }

}
