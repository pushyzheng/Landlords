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

    private Integer id;

    private int cardSize;

    private IdentityEnum identity;

    private List<Card> recentCards;

    private boolean ready;

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
        user = new UserOutDTO(player.getUser());
        recentCards = player.getRecentCards();
    }


}
