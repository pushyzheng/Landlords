package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.common.enums.CardNumberEnum;
import site.pushy.landlords.common.enums.CardTypeEnum;

/**
 * @author Pushy
 * @since 2018/12/28 21:41
 */
@Data
public class Card {

    // 牌的数字ID
    private int id;

    // 牌的类型
    private CardTypeEnum type;

    // 牌的数值
    private CardNumberEnum number;

    // 牌的等级
    private int grade;

    private String imgName;

}
