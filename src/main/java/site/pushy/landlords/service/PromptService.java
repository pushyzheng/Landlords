package site.pushy.landlords.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.core.CardUtils;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.core.processor.PromptCardsProcessor;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PromptService implements ApplicationContextAware {

    private Map<String, PromptCardsProcessor> processorMap;

    public List<Card> prompt(Room room, Player curPlayer) {
        List<Card> prevCards = room.getPreCards();
        TypeEnum typeEnum = CardUtils.getCardsType(prevCards);
        if (!processorMap.containsKey(typeEnum.name())) {
            throw new BadRequestException("不支持该类型牌的提示: " + typeEnum.getName());
        }
        List<Card> result = processorMap.get(typeEnum.name()).process(curPlayer.getCards(), prevCards);
        log.info("[{}] 牌提示, playerId: {}, cardType: {}, result: {}", room.getId(), curPlayer.getId(),
                typeEnum, result);
        return result;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        Map<String, PromptCardsProcessor> beans = ctx.getBeansOfType(PromptCardsProcessor.class);
        processorMap = new HashMap<>();
        beans.values().forEach(bean -> processorMap.put(bean.canPrompt().name(), bean));
    }
}
