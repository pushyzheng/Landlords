package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;

/**
 * @author Pushy
 * @since 2019/2/15 23:43
 */
@Data
public class PlayerOutDTO {

    private Integer id;

    private int cardSize;

    private IdentityEnum identity;

    private boolean ready;

    private UserOutDTO user;

    public PlayerOutDTO(Player player) {
        id = player.getId();
        cardSize = player.getCards().size();
        identity = player.getIdentity();
        ready = player.isReady();
        user = new UserOutDTO(player.getUser());
    }


}
