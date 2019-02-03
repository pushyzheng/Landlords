package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/1 18:20
 */
public interface PlayerService {

    List<Card> getPlayerCards(User curUser);

}
