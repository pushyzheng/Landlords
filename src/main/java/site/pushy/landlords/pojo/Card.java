package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.common.enums.CardNumberEnum;
import site.pushy.landlords.common.enums.CardTypeEnum;

import java.util.Objects;

/**
 * @author Pushy
 * @since 2018/12/28 21:41
 */
@Data
public class Card implements Comparable<Card> {

    // 牌的数字ID
    private int id;

    // 牌的类型
    private CardTypeEnum type;

    // 牌的数值
    private CardNumberEnum number;

    // 牌的等级
    private int grade;

    /**
     * 实现牌比大小的方法，可提供给卡牌数组排序使用
     * @param o 另一张对比的牌
     */
    public int compareTo(Card o) {
        return Integer.compare(this.getNumber().getValue(), o.number.getValue());
    }

    /**
     * 实现两张牌是否相等
     * @param o 另一张对比的牌
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //if (!super.equals(o)) return false;
        Card other = (Card) o;
        return number.getValue() == other.number.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number.getValue());
    }
}
