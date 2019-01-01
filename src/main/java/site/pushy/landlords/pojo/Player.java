package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.pojo.DO.User;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/1 17:23
 */
@Data
public class Player {

    private Integer id;  // 玩家在当前房间的座位顺序

    private IdentityEnum identity;  // 当前局的身份（地主、农民）

    private List<Card> cards;  // 玩家当前手中的牌

    private User user;  // 玩家的用户对象

    public String getIdentityName() {
        if (identity != null) {
            return identity.getName();
        }
        return "";
    }

}
