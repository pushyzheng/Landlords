package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;

/**
 * 每局结束后结算结果对象
 *
 * @author Pushy
 * @since 2019/2/20 22:23
 */
@Data
public class ResultScoreDTO {

    private String username;

    private String moneyChange;

    private String identityName;

    public ResultScoreDTO(String username, String moneyChange, IdentityEnum identity) {
        this.username = username;
        this.moneyChange = moneyChange;
        this.identityName = identity.getName();
    }
}
