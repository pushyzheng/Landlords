package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.PlayerService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/1 18:21
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private RoomComponent roomComponent;

    @Override
    public List<Card> getPlayerCards(User curUser) {
        return roomComponent.getUserCards(curUser.getId());
    }

    @Override
    public boolean isPlayerRound(User curUser) {
        Room room = roomComponent.getUserRoom(curUser.getId());
        if (room.getStatus() == RoomStatusEnum.PREPARING) {
            throw new BadRequestException("游戏还未开始");
        }
        int prePlayerId = room.getPrePlayerId();
        int next = prePlayerId == 3 ? 1 : prePlayerId + 1;
        return room.getPlayerByUserId(curUser.getId()).getId() == next;
    }

    @Override
    public boolean isPlayerReady(User curUser) {
        Room room = roomComponent.getUserRoom(curUser.getId());
        if (room.getStatus() == RoomStatusEnum.PLAYING) {
            throw new BadRequestException("游戏已经开始");
        }
        Player player = room.getPlayerByUserId(curUser.getId());
        return player.isReady();
    }
}
