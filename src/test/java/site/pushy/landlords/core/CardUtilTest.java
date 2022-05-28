package site.pushy.landlords.core;

import org.junit.Assert;
import org.junit.Test;
import site.pushy.landlords.common.util.TestUtils;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.List;

import static site.pushy.landlords.core.enums.CardGradeEnum.*;

public class CardUtilTest {

    @Test
    public void compareGrade() {
        Card card = new Card(THIRD);
        Card card2 = new Card(CardGradeEnum.SECOND);

        Assert.assertEquals(1, CardUtils.compareGrade(card, card2));
        Assert.assertEquals(0, CardUtils.compareGrade(card, card));
        Assert.assertEquals(-1, CardUtils.compareGrade(card2, card));
    }

    @Test
    public void compareGradeTo() {
        Card card = new Card(THIRD);
        Card card2 = new Card(CardGradeEnum.SECOND);

        // card > card2
        Assert.assertTrue(CardUtils.compareGradeTo(card, card2));
        Assert.assertFalse(CardUtils.compareGradeTo(card2, card));

        // card == card
        Assert.assertFalse(CardUtils.compareGradeTo(card, card));
    }

    @Test
    public void sortCards() {
        List<Card> cards = TestUtils.buildCards(SECOND, THIRD);

        CardUtils.sortCards(cards);

        Assert.assertEquals(cards.get(0).getGrade(), SECOND);
        Assert.assertEquals(cards.get(1).getGrade(), THIRD);
    }
}
