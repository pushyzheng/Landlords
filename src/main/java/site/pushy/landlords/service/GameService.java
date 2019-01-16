package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;

/**
 * @author Pushy
 * @since 2019/1/9 21:19
 */
public interface GameService {

    boolean readyGame(User user, ReadyGameDTO readyGameDTO);

    void startGame(String roomId);

}
