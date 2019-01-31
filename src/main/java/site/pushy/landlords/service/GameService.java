package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/9 21:19
 */
public interface GameService {

    boolean readyGame(User user, ReadyGameDTO readyGameDTO);

    void startGame(String roomId);

    void wantCard(String roomId,User user);

    void noWantCard(String roomId,User user);

    void outCard(String roomId,User user, List<Card> cardList);

    void wantCardOrder(String roomId);
}
