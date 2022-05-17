package site.pushy.landlords.core.processor;

import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.List;

public interface PromptCardsProcessor {

    /**
     * 执行提示
     *
     * @param curCards   当前玩家所有的牌
     * @param carsOfPrev 上一个玩家出的牌
     * @return 提示出的牌, 如果为空则代表没有更大的牌了
     */
    List<Card> process(List<Card> curCards, List<Card> carsOfPrev);

    /**
     * 返回该处理器是否能处理牌的 {@link TypeEnum}
     *
     * @return 支持提示的牌类型
     */
    TypeEnum canPrompt();
}
