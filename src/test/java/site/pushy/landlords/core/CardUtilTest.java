package site.pushy.landlords.core;

import org.junit.Assert;
import org.junit.Test;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.pojo.Card;

public class CardUtilTest {

    @Test
    public void compareGrade() {
        Card card = new Card(CardGradeEnum.THIRD);
        Card card2 = new Card(CardGradeEnum.SECOND);

        Assert.assertEquals(1, CardUtils.compareGrade(card, card2));
        Assert.assertEquals(0, CardUtils.compareGrade(card, card));
        Assert.assertEquals(-1, CardUtils.compareGrade(card2, card));
    }

    @Test
    public void compareGradeTo() {
        Card card = new Card(CardGradeEnum.THIRD);
        Card card2 = new Card(CardGradeEnum.SECOND);

        // card > card2
        Assert.assertTrue(CardUtils.compareGradeTo(card, card2));
        Assert.assertFalse(CardUtils.compareGradeTo(card2, card));

        // card == card
        Assert.assertFalse(CardUtils.compareGradeTo(card, card));
    }
}
