package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 回合结果
 *
 * @author Pushy
 * @since 2019/2/11 18:17
 */
@Data
public class RoundResult {

    /**
     * 胜利方的身份
     */
    private IdentityEnum winIdentity;

    /**
     * 地主的玩家 ID
     */
    private int landlord;

    /**
     * 倍数
     */
    private int multiple;

    public RoundResult(IdentityEnum winIdentity, int multiple) {
        this.winIdentity = winIdentity;
        this.multiple = multiple;
    }
}
