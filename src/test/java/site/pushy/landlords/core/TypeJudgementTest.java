package site.pushy.landlords.core;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import site.pushy.landlords.common.util.TestUtils;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static site.pushy.landlords.core.enums.CardGradeEnum.*;

public class TypeJudgementTest {

    @Test
    public void isSingle() {
        Assert.assertTrue(TypeJudgement.isSingle(buildCards(SECOND)));

        Assert.assertFalse(TypeJudgement.isSingle(buildCards(SECOND, SECOND)));
    }

    @Test
    public void isPair() {
        Assert.assertTrue(TypeJudgement.isPair(buildCards(SECOND, SECOND)));

        Assert.assertFalse(TypeJudgement.isPair(buildCards(SECOND)));
    }

    @Test
    public void isThree() {
        Assert.assertTrue(TypeJudgement.isThree(Lists.newArrayList(buildCards(SECOND, SECOND, SECOND))));

        // not three
        Assert.assertFalse(TypeJudgement.isThree(Lists.newArrayList(buildCards(SECOND, SECOND, THIRD))));
        Assert.assertFalse(TypeJudgement.isThree(Lists.newArrayList(buildCards(SECOND, THIRD, THIRD))));
    }

    @Test
    public void isBomb() {
        Assert.assertTrue(TypeJudgement.isBomb(buildCards(SECOND, SECOND, SECOND, SECOND)));

        // not bomb
        Assert.assertFalse(TypeJudgement.isBomb(buildCards(SECOND, THIRD, SECOND, SECOND)));
        Assert.assertFalse(TypeJudgement.isBomb(buildCards(SECOND, THIRD, THIRD, SECOND)));
        Assert.assertFalse(TypeJudgement.isBomb(buildCards(SECOND, THIRD, THIRD, THIRD)));
    }

    @Test
    public void isJokerBomb() {
        Assert.assertTrue(TypeJudgement.isJokerBomb(buildCards(FOURTEENTH, FIFTEENTH)));

        Assert.assertFalse(TypeJudgement.isJokerBomb(buildCards(FOURTEENTH, FOURTEENTH)));
        Assert.assertFalse(TypeJudgement.isJokerBomb(buildCards(FIFTEENTH, FIFTEENTH)));
        Assert.assertFalse(TypeJudgement.isJokerBomb(buildCards(SECOND, SECOND)));
    }

    @Test
    public void isThreeWithOne() {
        // 3带1
        Assert.assertTrue(TypeJudgement.isThreeWithOne(buildCards(FIFTH, FIFTH, FIFTH, FIRST)));
        Assert.assertTrue(TypeJudgement.isThreeWithOne(buildCards(FIRST, FIRST, FIRST, FIFTH)));

        Assert.assertFalse(TypeJudgement.isThreeWithOne(buildCards(FIRST, FIRST, FIFTH, FIFTH)));
        Assert.assertFalse(TypeJudgement.isThreeWithOne(buildCards(FIRST, FIRST, FIFTH, FOURTH)));
        // 炸弹
        Assert.assertFalse(TypeJudgement.isThreeWithOne(buildCards(FIFTH, FIFTH, FIFTH, FIFTH)));
    }

    @Test
    public void isThreeWithPair() {
        // 3带1对
        Assert.assertTrue(TypeJudgement.isThreeWithPair(buildCards(SECOND, SECOND, SECOND, FIRST, FIRST)));

        Assert.assertFalse(TypeJudgement.isThreeWithPair(buildCards(SECOND, SECOND, SECOND, FIRST, SECOND)));
    }

    @Test
    public void isFourWithTwo() {
        Assert.assertTrue(TypeJudgement.isFourWithTwo(buildCards(SECOND, SECOND, SECOND, SECOND, FIRST, THIRD)));
        Assert.assertTrue(TypeJudgement.isFourWithTwo(buildCards(SECOND, SECOND, SECOND, SECOND, FIRST, FIRST)));
        Assert.assertTrue(TypeJudgement.isFourWithTwo(buildCards(SECOND, SECOND, SECOND, SECOND, THIRD, THIRD)));
    }

    @Test
    public void isStraight() {
        // 10 J Q K A
        Assert.assertTrue(TypeJudgement.isStraight(buildCards(EIGHTH, NINTH, TENTH, ELEVENTH, TWELFTH)));

        Assert.assertFalse(TypeJudgement.isStraight(buildCards(EIGHTH, NINTH, TENTH, ELEVENTH, SIXTH)));
        // 10 10 Q K A
        Assert.assertFalse(TypeJudgement.isStraight(buildCards(EIGHTH, EIGHTH, TENTH, ELEVENTH, TWELFTH)));
        // J Q K A 2
        Assert.assertFalse(TypeJudgement.isStraight(buildCards(NINTH, TENTH, ELEVENTH, TWELFTH, THIRTEENTH)));
    }

    @Test
    public void isStraightPair() {
        // 3 3 4 4 5 5
        Assert.assertTrue(TypeJudgement.isStraightPair(buildCards(FIRST, FIRST, SECOND, SECOND, THIRD, THIRD)));

        // 3 3 4 4
        Assert.assertFalse(TypeJudgement.isStraightPair(buildCards(FIRST, FIRST, SECOND, SECOND)));
        // 3 3 4 4 5 6
        Assert.assertFalse(TypeJudgement.isStraightPair(buildCards(FIRST, FIRST, SECOND, SECOND, THIRD, FOURTH)));
        // 3 3 4 4 6 6
        Assert.assertFalse(TypeJudgement.isStraightPair(buildCards(FIRST, FIRST, SECOND, SECOND, FOURTH, FOURTH)));
    }

    @Test
    public void isAircraft() {
        // 3 3 3 4 4 4
        Assert.assertTrue(TypeJudgement.isAircraft(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, SECOND)));

        // 3 3 3 4 4 5
        Assert.assertFalse(TypeJudgement.isAircraft(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, THIRD)));
        // 3 3 3 5 5 5
        Assert.assertFalse(TypeJudgement.isAircraft(buildCards(FIRST, FIRST, FIRST, THIRD, THIRD, THIRD)));
        // 5 5 5 7 7 7
        Assert.assertFalse(TypeJudgement.isAircraft(buildCards(THIRD, THIRD, THIRD, FIFTH, FIFTH, FIFTH)));
    }

    @Test
    public void isAircraftWithWing() {
        // 3 3 3 4 4 4 8 10
        Assert.assertTrue(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, SECOND, SIXTH, EIGHTH)));
        // 3 3 3 4 4 4 8 8 10 10
        Assert.assertTrue(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, SECOND, SIXTH, SIXTH, EIGHTH, EIGHTH)));

        // 3 3 3 4 4 4 8 8 10 J
        Assert.assertFalse(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, SECOND, SIXTH, SIXTH, EIGHTH, NINTH)));
        // 3 3 3 10
        Assert.assertFalse(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, EIGHTH)));
        // 3 3 3 5 5 5 8 10
        Assert.assertFalse(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, THIRD, THIRD, THIRD, SIXTH, EIGHTH)));
        // 3 3 3 4 4 4 8 8 8 8
        Assert.assertFalse(TypeJudgement.isAircraftWithWing(buildCards(FIRST, FIRST, FIRST, SECOND, SECOND, SECOND, SIXTH, SIXTH, SIXTH, SIXTH)));
    }

    @Test
    public void isAllGradeEqual() {
        Assert.assertTrue(TypeJudgement.isAllGradeEqual(buildCards(THIRD, FIRST, FIRST, THIRD), 1, 2));

        Assert.assertFalse(TypeJudgement.isAllGradeEqual(buildCards(THIRD, FIRST, FIRST, THIRD), 0, 1));
        Assert.assertFalse(TypeJudgement.isAllGradeEqual(buildCards(THIRD, FIRST, FIRST, THIRD), 1, 3));
        Assert.assertFalse(TypeJudgement.isAllGradeEqual(buildCards(THIRD, FIRST, FIRST, THIRD), 2, 3));
    }

    private List<Card> buildCards(CardGradeEnum... arr) {
        return TestUtils.buildCards(arr);
    }
}
