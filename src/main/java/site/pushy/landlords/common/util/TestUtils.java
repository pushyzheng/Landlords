package site.pushy.landlords.common.util;

import lombok.experimental.UtilityClass;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class TestUtils {

    public List<Card> buildCards(CardGradeEnum... arr) {
        return Arrays.stream(arr).map(Card::new)
                .collect(Collectors.toList());
    }
}
