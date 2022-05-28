package site.pushy.landlords.core.processor.impl;

import org.springframework.stereotype.Component;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
public class PairPromptCardsProcessor extends AbstractPromptCardsProcessor {

    @Override
    protected List<Card> process0(List<Card> cur, List<Card> prev) {
        if (cur.size() < 2) return PASS;

        TreeMap<CardGradeEnum, List<Card>> multimap = new TreeMap<>();
        cur.forEach(card -> {
            List<Card> cards = multimap.get(card.getGrade());
            if (cards == null) {
                cards = new ArrayList<>();
            }
            cards.add(card);
            multimap.put(card.getGrade(), cards);
        });
        return null;
    }

    @Override
    public TypeEnum canPrompt() {
        return TypeEnum.PAIR;
    }
}
