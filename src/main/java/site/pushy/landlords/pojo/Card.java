package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.core.enums.CardTypeEnum;

import java.util.Objects;

/**
 * @author Pushy
 * @since 2018/12/28 21:41
 */
@Data
public class Card implements Comparable<Card> {

    /**
     * 牌的数字ID
     */
    private int id;

    /**
     * 牌的类型
     */
    private CardTypeEnum type;

    /**
     * 牌的数值
     */
    private CardNumberEnum number;

    /**
     * 牌的等级
     * <p>
     * 和数值差别在于A和2数值虽然比3~K小，但是等级却比3高
     */
    private CardGradeEnum grade;

    public Card() {
    }

    public Card(CardGradeEnum grade) {
        this.grade = grade;
    }

    public Card(int id) {
        this.id = id;
    }

    public int getNumberValue() {
        if (number != null) {
            return number.getValue();
        }
        return -1;
    }

    public int getGradeValue() {
        if (grade != null) {
            return grade.getValue();
        }
        return -1;
    }

    public String getTypeName() {
        if (type != null) {
            return type.getName();
        }
        return null;
    }

    /**
     * 实现牌比大小的方法，可提供给卡牌数组排序使用
     *
     * @param o 另一张对比的牌
     */
    @Override
    public int compareTo(Card o) {
//        return Integer.compare(this.getNumberValue(), o.getNumberValue());
        return -Integer.compare(this.getGradeValue(), o.getGradeValue());
    }

    /**
     * 两张牌等级比较方法
     *
     * @param o 另一张对比的牌
     */
    public int compareGradeTo(Card o) {
        return Integer.compare(this.getGradeValue(), o.getGradeValue());
    }

    public boolean equalsByGrade(Card other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return this.getGradeValue() == other.getGradeValue();
    }

    /**
     * 实现两张牌是否相等
     *
     * @param o 另一张对比的牌
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //if (!super.equals(o)) return false;

        Card other = (Card) o;
        if (number == null || other.number == null) {
            return false;
        }
        return number.getValue() == other.number.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number.getValue());
    }

    @Override
    public String toString() {
        return "（" +
                "类型=" + getTypeName() +
                ", 数值=" + getNumberValue() +
                ", 等级=" + getGradeValue() +
                "）";
    }
}
