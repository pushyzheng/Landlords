package site.pushy.landlords.core;

import site.pushy.landlords.pojo.Card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 每局开始重新发牌、分牌、获取每个玩家的牌、分配玩家身份。
 *
 * @author Pushy
 * @since 2018/12/30 21:49
 */
public class Main {

    private static List<Card> allCardList;  // 所有牌

    private static int landowner = 0;  // 地主的玩家序号

    private static List<Card> player1Cards = new LinkedList<>();  // 玩家1的牌
    private static List<Card> player2Cards = new LinkedList<>();  // 玩家2的牌
    private static List<Card> player3Cards = new LinkedList<>();  // 玩家3的牌

    static {
        allCardList = new LinkedList<>();
        createCard();
    }

    /**
     * 刷新牌，重新洗牌，分牌
     */
    public static void refresh() {
        shuffle();
        deal();
    }

    /**
     * 创建牌
     */
    private static void createCard() {
        for (int i = 0; i < 54; i++) {
            int id = i + 1;
            Card card = new Card(id);
            card.setType(ConstructCard.getTypeById(id));  // 设置花色
            card.setNumber(ConstructCard.getNumberById(id));  // 设置牌的数值
            card.setGrade(ConstructCard.getGradeById(id));  // 设置牌的等级
            allCardList.add(card);
        }
    }

    /**
     * 洗牌
     */
    private static void shuffle() {
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(allCardList);
        }
    }

    /**
     * 分牌
     */
    private static void deal() {
        /* 分派给1号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3);
            player1Cards.add(card);
            /* 判断牌是不是地主牌，即id=1，如果是地主则将地主设置为当前发牌的玩家 */
            if (card.getId() == 1) {
                landowner = 1;
            }
        }
        /* 分派给2号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3 + 1);
            player2Cards.add(card);
            if (card.getId() == 1) {
                landowner = 2;
            }
        }
        /* 分派给3号玩家17张牌 */
        for (int i = 0; i < 17; i++) {
            Card card = allCardList.get(i * 3 + 2);
            player3Cards.add(card);
            if (card.getId() == 1) {
                landowner = 3;
            }
        }

        /* 将剩余的三张牌添加到地主的牌当中 */
        List<Card> topCards = allCardList.subList(51, 53);
        switch (landowner) {
            case 1:
                player1Cards.addAll(topCards);
                break;
            case 2:
                player2Cards.addAll(topCards);
                break;
            case 3:
                player3Cards.addAll(topCards);
                break;
        }

        /* 将玩家的牌通过等级由小到大的排序 */
        Collections.sort(player1Cards);
        Collections.sort(player2Cards);
        Collections.sort(player3Cards);
    }

    /**
     * 获取对应玩家该局的牌
     * @param number 玩家在房间中的序号
     */
    public static List<Card> getCards(int number) {
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

    /**
     * 获得地主的玩家的序号
     */
    public static int getLandowner() {
        return landowner;
    }

    public static void main(String[] args) {

//        System.out.println(allCardList);

        refresh();

        System.out.println("地主玩家是：" + getLandowner());

        System.out.println("玩家1的牌是：" +getCards(1));
        System.out.println("玩家2的牌是：" + getCards(2));
        System.out.println("玩家3的牌是：" + getCards(3));
    }

}
