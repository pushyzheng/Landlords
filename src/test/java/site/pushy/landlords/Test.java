package site.pushy.landlords;

import site.pushy.landlords.core.CardDistribution;
import site.pushy.landlords.core.TypeJudgement;
import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.BidMessage;

import java.util.Arrays;
import java.util.List;

import static site.pushy.landlords.core.enums.CardGradeEnum.*;

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

//         3带1
//        Card a = new Card(FIFTH);
//        Card b = new Card(FIFTH);
//        Card c = new Card(FIFTH);
//        Card d = new Card(FIFTH);
//
//        List<Card> cards = Arrays.asList(a, b, c, d);
//        System.out.println(TypeJudgement.isThreeWithOne(cards));


//         3带1对
//        Card a = new Card(SECOND);
//        Card b = new Card(SECOND);
//        Card c = new Card(SECOND);
//        Card d = new Card(FIRST);
//        Card e = new Card(SEVENTH);
//
//        List<Card> cards = Arrays.asList(a, b, c, d, e);
//        System.out.println(TypeJudgement.isThreeWithPair(cards));

//        顺子
//        Card a = new Card(EIGHTH);
//        Card b = new Card(NINTH);
//        Card c = new Card(TENTH);
//        Card d = new Card(ELEVENTH);
//        Card e = new Card(TWELFTH);
//
//        List<Card> cards = Arrays.asList(a, b, c, d, e);
//        System.out.println(TypeJudgement.isStraight(cards));

//        连对
        Card aa = new Card(SEVENTH);
        Card ab = new Card(SEVENTH);

        Card a = new Card(EIGHTH);
        Card b = new Card(EIGHTH);
        Card c = new Card(NINTH);
        Card d = new Card(TWELFTH);
        Card e = new Card(TENTH);
        Card f = new Card(TENTH);
        Card g = new Card(ELEVENTH);
        Card h = new Card(ELEVENTH);

        Card i = new Card(TWELFTH);
        Card j = new Card(TWELFTH);
//
        List<Card> cards = Arrays.asList(aa, ab, a, b, c, d, e, f, g, h, i, j);
        System.out.println(TypeJudgement.isStraightPair(cards));

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
        System.out.println(TypeJudgement.isFlyWithNone(cards));*/


//        4带2
//        Card a = new Card(FIRST);
//        Card b = new Card(SECOND);
//        Card c = new Card(SECOND);
//        Card d = new Card(SECOND);
//
//        Card e = new Card(SECOND);
//        Card f = new Card(THIRD);
//
//        List<Card> cards = Arrays.asList(a, b, c, d, e, f);
//        System.out.println(TypeJudgement.isFourWithTwo(cards));

//        CardDistribution distribution = new CardDistribution();
//        distribution.refresh();
    }
}
