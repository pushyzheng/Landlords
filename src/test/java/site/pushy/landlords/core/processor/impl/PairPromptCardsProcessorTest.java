package site.pushy.landlords.core.processor.impl;

import org.junit.Test;
import site.pushy.landlords.common.util.TestUtils;
import site.pushy.landlords.core.processor.PromptCardsProcessor;
import site.pushy.landlords.pojo.Card;

import java.util.List;

import static org.junit.Assert.*;
import static site.pushy.landlords.core.enums.CardGradeEnum.FIRST;
import static site.pushy.landlords.core.enums.CardGradeEnum.SECOND;
import static site.pushy.landlords.core.enums.CardGradeEnum.THIRD;

public class PairPromptCardsProcessorTest {

    PromptCardsProcessor processor = new PairPromptCardsProcessor();

    @Test
    public void process0() {
        List<Card> prev = TestUtils.buildCards(FIRST, FIRST);
        List<Card> cur = TestUtils.buildCards(SECOND, SECOND, THIRD);

        System.out.println(processor.process(cur, prev));
    }
}
