package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/15 23:43
 */
@Data
public class PlayerOutDTO {

    /**
     * 玩家序号
     */
    private Integer id;

    /**
     * 牌的大小
     */
    private int cardSize;

    /**
     * 身份
     */
    private IdentityEnum identity;

    /**
     * 最近出的牌
     */
    private List<Card> recentCards;

    /**
     * 是否准备
     */
    private boolean ready;

    /**
     * 是否在线
     */
    private boolean online;

    /**
     * 用户身份
     */
    private UserOutDTO user;

    public String getIdentityName() {
        if (identity != null) {
            return identity.getName();
        }
        return " ";
    }

    public PlayerOutDTO(Player player) {
        id = player.getId();
        cardSize = player.getCards().size();
        identity = player.getIdentity();
        ready = player.isReady();
        user = UserOutDTO.fromUser(player.getUser());
        recentCards = player.getRecentCards();
    }
}
