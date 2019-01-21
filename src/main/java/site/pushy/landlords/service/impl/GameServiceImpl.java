package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.GameService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/9 21:20
 */
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private NotifyComponent notifyComponent;

    /**
     * 玩家准备游戏
     * @return 是否开局，当房间的内的所有玩家都准备，并且人数已满3人，即为开局
     */
    @Override
    public boolean readyGame(User user, ReadyGameDTO readyGameDTO) {
        Room room = roomComponent.getRoom(readyGameDTO.getRoomId());

        /* 更改玩家的准备状态 */
        List<Player> playerList = room.getPlayerList();
        for (Player player : playerList) {
            if (player.getUser().getId().equals(user.getId())) {
                player.setReady(true);
            }
        }
        room.setPlayerList(playerList);
        roomComponent.updateRoom(room);

        /* 检查是否房间内的人数等于3人，并且全部都处于准备中的状态 */
        List<Player> players = room.getPlayerList();
        boolean res = true;
        if (playerList.size() != 3) {
            res = false;
        } else {
            for (Player player : players) {
                if (!player.isReady()) {  // 当房间内有某一个用户没有准备，则说明没有开局
                    res = false;
                }
            }
        }
        return res;
    }

    /**
     * 通知所有房间内的玩家开始游戏，并更新房间的状态
     * @param roomId
     */
    @Override
    public void startGame(String roomId) {
        Room room = roomComponent.getRoom(roomId);
        room.setStatus(RoomStatusEnum.PLAYING);
        roomComponent.updateRoom(room);

        /* 通过webSocket通知所有的玩家客户端开始游戏 */
        notifyComponent.sendToAllUserOfRoom(roomId, "开始游戏！！");
    }


}
