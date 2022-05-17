package site.pushy.landlords.core.processor.impl;

import org.springframework.stereotype.Component;
import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Collections;
import java.util.List;

@Component
public class SinglePromptCardsProcessor extends AbstractPromptCardsProcessor {

    @Override
    protected List<Card> process0(List<Card> curCards, List<Card> carsOfPrev) {
        Card prevCard = carsOfPrev.get(0);
        if (prevCard.getNumber() == CardNumberEnum.BIG_JOKER) {
            return PASS;
        }
        if (prevCard.getNumber() == CardNumberEnum.SMALL_JOKER) {
            return processBigJoker(curCards);
        }
        return PASS;
    }

    private List<Card> processBigJoker(List<Card> curCards) {
        return curCards.stream()
                .filter(c -> c.getNumber() == CardNumberEnum.BIG_JOKER)
                .findFirst()
                .map(Collections::singletonList)
                .orElse(PASS);  // 没有找到大王
    }

    @Override
    public TypeEnum canPrompt() {
        return TypeEnum.SINGLE;
    }
}
