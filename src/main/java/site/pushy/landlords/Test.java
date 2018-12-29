package site.pushy.landlords;

import site.pushy.landlords.common.enums.CardGradeEnum;
import site.pushy.landlords.core.Main;
import site.pushy.landlords.pojo.Card;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/28 23:31
 */
public class Test {
    public static void main(String[] args) {

        /*Card a = new Card();
        a.setNumber(CardNumberEnum.ONE);

        Card three = new Card();
        three.setNumber(CardNumberEnum.THREE);

        List<Card> list = new LinkedList<>();
        list.add(three);
        list.add(a);

        Collections.sort(list);
        System.out.println(list);*/

        /*Card a = new Card();
        a.setNumber(CardNumberEnum.ONE);
        Card b = new Card();
        b.setNumber(CardNumberEnum.TEN);
        Card c = new Card();
        c.setNumber(CardNumberEnum.ONE);
        Card d = new Card();
        d.setNumber(CardNumberEnum.ONE);

        List<Card> cards = Arrays.asList(a, b, c, d);
        System.out.println(Main.isThreeWithOne(cards));*/

        /*Card a = new Card();
        a.setNumber(CardNumberEnum.ONE);
        Card b = new Card();
        b.setNumber(CardNumberEnum.TWO);
        Card c = new Card();
        c.setNumber(CardNumberEnum.THREE);
        Card d = new Card();
        d.setNumber(CardNumberEnum.FOUR);
        Card e = new Card();
        e.setNumber(CardNumberEnum.FIVE);

        List<Card> cards = Arrays.asList(a, b, c, d, e);
        System.out.println(Main.isStraight(cards));*/

        /*Card a = new Card();
        a.setNumber(CardNumberEnum.ONE);
        Card b = new Card();
        b.setNumber(CardNumberEnum.ONE);
        Card c = new Card();
        c.setNumber(CardNumberEnum.TWO);
        Card d = new Card();
        d.setNumber(CardNumberEnum.TWO);
        Card e = new Card();
        e.setNumber(CardNumberEnum.THREE);
        Card f = new Card();
        f.setNumber(CardNumberEnum.THREE);

        List<Card> cards = Arrays.asList(a, b, c, d, e, f);
        System.out.println(Main.isStraightPair(cards));*/

        /*Card a = new Card();
        a.setGrade(CardGradeEnum.THIRD);
        Card b = new Card();
        b.setGrade(CardGradeEnum.THIRD);
        Card c = new Card();
        c.setGrade(CardGradeEnum.THIRD);
        Card d = new Card();
        d.setGrade(CardGradeEnum.FOURTH);
        Card e = new Card();
        e.setGrade(CardGradeEnum.FOURTH);
        Card f = new Card();
        f.setGrade(CardGradeEnum.FOURTH);

        Card g = new Card();
        g.setNumber(CardNumberEnum.SIX);
        Card h = new Card();
        h.setNumber(CardNumberEnum.EIGHT);

        List<Card> cards = Arrays.asList(a, b, c, d, e, f);
        System.out.println(Main.isFlyWithNone(cards));*/


        Card a = new Card();
        a.setGrade(CardGradeEnum.FIRST);
        Card b = new Card();
        b.setGrade(CardGradeEnum.THIRD);
        Card c = new Card();
        c.setGrade(CardGradeEnum.FOURTH);
        Card d = new Card();
        d.setGrade(CardGradeEnum.THIRD);

        Card e = new Card();
        e.setGrade(CardGradeEnum.THIRD);
        Card f = new Card();
        f.setGrade(CardGradeEnum.SIXTH);

        List<Card> cards = Arrays.asList(a, b, c, d, e, f);
        System.out.println(Main.isFourWithTwo(cards));

    }
}
