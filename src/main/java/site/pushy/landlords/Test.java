package site.pushy.landlords;

import site.pushy.landlords.common.enums.CardNumberEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/28 23:31
 */
public class Test {
    public static void main(String[] args) {

        /*Card a = new Card();
        a.setNumber(CardNumberEnum.A);

        Card three = new Card();
        three.setNumber(CardNumberEnum.THREE);

        List<Card> list = new LinkedList<>();
        list.add(three);
        list.add(a);

        Collections.sort(list);
        System.out.println(list);*/

        Card a = new Card();
        a.setNumber(CardNumberEnum.A);
        Card b = new Card();
        b.setNumber(CardNumberEnum.TEN);
        Card c = new Card();
        c.setNumber(CardNumberEnum.A);
        Card d = new Card();
        d.setNumber(CardNumberEnum.A);

        List<Card> cards = Arrays.asList(a, b, c, d);
        System.out.println(Main.isThreeWithOne(cards));
    }
}
