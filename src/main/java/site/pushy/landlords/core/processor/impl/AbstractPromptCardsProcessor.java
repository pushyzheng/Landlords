package site.pushy.landlords.core.processor.impl;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.core.processor.PromptCardsProcessor;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPromptCardsProcessor implements PromptCardsProcessor {

    protected static final List<Card> PASS = Collections.emptyList();

    @Override
    public List<Card> process(List<Card> curCards, List<Card> carsOfPrev) {
        if (check(curCards, carsOfPrev)) {
            return PASS;
        }
        return process0(curCards, carsOfPrev);
    }

    protected abstract List<Card> process0(List<Card> curCards, List<Card> carsOfPrev);

    private boolean check(List<Card> curCards, List<Card> carsOfPrev) {
        if (curCards.size() < carsOfPrev.size()) {
            return false;
        }
        return true;
    }
}
