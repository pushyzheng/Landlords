package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/11 18:17
 */
@Data
public class RoundResult {

    private IdentityEnum winIdentity;  // 胜利方的身份

    private int landlord;

    private int multiple;

    public RoundResult(IdentityEnum winIdentity, int multiple) {
        this.winIdentity = winIdentity;
        this.multiple = multiple;
    }

}
