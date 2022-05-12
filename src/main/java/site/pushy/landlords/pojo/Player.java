package site.pushy.landlords.pojo;

import lombok.Data;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.pojo.DO.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 玩家
 *
 * @author Pushy
 * @since 2019/1/1 17:23
 */
@Data
public class Player implements Comparable<Player> {

    /**
     * 玩家在当前房间的座位顺序
     */
    private Integer id;

    /**
     * 当前局的身份（地主、农民）
     */
    private IdentityEnum identity;

    /**
     * 玩家当前手中的牌
     */
    private List<Card> cards;

    /**
     * 玩家最近出的牌列表
     */
    private List<Card> recentCards;

    /**
     * 玩家的用户对象
     */
    private User user;

    /**
     * 玩家是否准备
     */
    private boolean ready;

    public Player() {
        cards = new ArrayList<>();
        recentCards = new ArrayList<>();
        ready = false;
    }

    public Player(Integer id) {
        this();
        this.id = id;
    }

    public String getIdentityName() {
        if (identity != null) {
            return identity.getName();
        }
        return "";
    }

    /**
     * 获取当前玩家的下一家id
     */
    public int getNextPlayerId() {
        return id == 3 ? 1 : id + 1;
    }

    public void addCards(List<Card> cardList) {
        cards.addAll(cardList);
        // 添加地主牌后重新按照等级排序
        Collections.sort(cards);
    }

    public void removeCards(List<Card> cardList) {
        for (Card card : cardList) {
            cards.remove(card);
        }
        Collections.sort(cards);
    }

    public void clearRecentCards() {
        recentCards.clear();
    }

    /**
     * 开局重置Player对象中的值
     */
    public void reset() {
        cards.clear();
        ready = false;
        identity = null;
        recentCards.clear();
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(id, other.id);
    }
}
