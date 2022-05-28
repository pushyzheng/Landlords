package site.pushy.landlords.core.processor.impl;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import site.pushy.landlords.common.util.TestUtils;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.core.processor.PromptCardsProcessor;
import site.pushy.landlords.pojo.Card;

import java.util.List;

import static site.pushy.landlords.core.enums.CardGradeEnum.*;

public class SinglePromptCardsProcessorTest {

    PromptCardsProcessor processor = new SinglePromptCardsProcessor();

    @Test
    public void process0() {
        List<Card> prev = TestUtils.buildCards(FIRST);
        List<Card> cur = TestUtils.buildCards(SECOND, SECOND, THIRD);

        Assertions.assertEquals(TypeEnum.SINGLE, processor.canPrompt());

        List<Card> result = processor.process(cur, prev);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(SECOND, result.get(0).getGrade());
    }

    @Test
    public void testPass() {
        List<Card> prev = TestUtils.buildCards(SEVENTH);
        List<Card> cur = TestUtils.buildCards(SECOND, SECOND, THIRD);

        List<Card> result = processor.process(cur, prev);
        Assertions.assertEquals(0, result.size());
    }
}
