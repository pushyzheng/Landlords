package site.pushy.landlords.core;

import site.pushy.landlords.pojo.Card;

import java.util.*;

/**
 * 每局开始重新发牌、分牌、获取每个玩家的牌、分配玩家身份。
 *
 * @author Pushy
 * @since 2018/12/30 21:49
 */
public class CardDistribution {

    private List<Card> allCardList;  // 所有牌

    private List<Card> player1Cards = new Vector<>();  // 玩家1的牌
    private List<Card> player2Cards = new Vector<>();  // 玩家2的牌
    private List<Card> player3Cards = new Vector<>();  // 玩家3的牌

    private List<Card> topCards = new Vector<>();

    public CardDistribution() {
        allCardList = new Vector<>();
    }

    /**
     * 刷新牌，重新洗牌，分牌
     */
    public synchronized void refresh() {
        clear();
        createCard();
        shuffle();
        deal();
    }

    /**
     * 创建牌
     */
    private void createCard() {
        for (int i = 0; i < 54; i++) {
            int id = i + 1;
            Card card = new Card(id);
            card.setType(ConstructCard.getTypeById(id));  // 设置花色
            card.setNumber(ConstructCard.getNumberById(id));  // 设置牌的数值
            card.setGrade(ConstructCard.getGradeById(id));  // 设置牌的等级
            allCardList.add(card);
        }
    }

    private synchronized void clear() {
        allCardList.clear();
        player1Cards.clear();
        player2Cards.clear();
        player3Cards.clear();
        topCards.clear();
    }

    /**
     * 洗牌
     */
    private void shuffle() {
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(allCardList);
        }
    }

    /**
     * 分牌
     */
    private void deal() {
        /* 分派给1号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3);
            player1Cards.add(card);
        }
        /* 分派给2号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3 + 1);
            player2Cards.add(card);
        }
        /* 分派给3号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3 + 2);
            player3Cards.add(card);
        }

        /* 将剩余的三张牌添加到地主的牌当中 */
        topCards = allCardList.subList(51, 54);

        /* 将玩家的牌通过等级由小到大的排序 */
        Collections.sort(player1Cards);
        Collections.sort(player2Cards);
        Collections.sort(player3Cards);
    }

    /**
     * 获得地主的三张牌
     */
    public List<Card> getTopCards() {
        return topCards;
    }

    /**
     * 获取对应玩家该局的牌
     * @param number 玩家在房间中的序号
     */
    public List<Card> getCards(int number) {
        switch (number) {
            case 1:
                return player1Cards;
            case 2:
                return player2Cards;
            case 3:
                return player3Cards;
        }
        return null;
    }

}
