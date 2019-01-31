package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.Message;
import site.pushy.landlords.pojo.ws.ReadyGameMessage;
import site.pushy.landlords.pojo.ws.StartGameMessage;
import site.pushy.landlords.service.GameService;

import java.util.List;
import java.util.Random;

import static site.pushy.landlords.core.enums.IdentityEnum.FARMER;
import static site.pushy.landlords.core.enums.IdentityEnum.LANDLORD;

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

        /* 通知其他房间的所有玩家准备游戏 */
        Message readyGameMessage = new ReadyGameMessage(user.getId());
        notifyComponent.sendToAllUserOfRoom(room.getId(), readyGameMessage);
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

        /* 通知所有的玩家客户端开始游戏 */
        notifyComponent.sendToAllUserOfRoom(roomId, new StartGameMessage(roomId));
    }

    /**
     * 随机一家叫牌
     * @param roomId
     */
    @Override
    public void wantCardOrder(String roomId) {
        Room room = roomComponent.getRoom(roomId);
        int order = (int)(1+Math.random()*(3-1+1));//从1到3的int型随机数
        for (Player player : room.getPlayerList()) {
            if (player.getId().equals(order)) {
                notifyComponent.sendToUser(player.getUser().getId(),"请选择是否叫牌");
                break;
            }
            continue;
        }
    }

    /**
     * 轮到此人叫牌,选择不叫地主.将叫地主消息传递给下一家
     * @param roomId
     * @param user
     */
    @Override
    public void noWantCard(String roomId, User user) {
        Room room = roomComponent.getRoom(roomId);
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                int playerId = player.getId();
                if (playerId==1){
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId()==2) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(),"请选择是否叫牌");
                            break;
                        }
                        continue;
                    }
                }else if (playerId==2){
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId()==3) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(),"请选择是否叫牌");
                            break;
                        }
                        continue;
                    }
                }else {
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId()==1) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(),"请选择是否叫牌");
                            break;
                        }
                        continue;
                    }
                }
                break;
            }
            continue;
        }
    }

    /**
     * 叫牌,并分配身份
     * @param user
     * @param roomId
     */
    @Override
    public void wantCard(String roomId,User user) {
        Room room = roomComponent.getRoom(roomId);
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                player.setIdentity(LANDLORD);
            }else {
                player.setIdentity(FARMER);
            }
            continue;
        }
        notifyComponent.sendToAllUserOfRoom(roomId, user.getUsername()+"成为地主！！");
    }

    /**
     * 出牌
     * @param roomId
     * @param user
     */
    @Override
    public void outCard(String roomId, User user, List<Card> cardList) {
        Room room = roomComponent.getRoom(roomId);
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                for (Card card : cardList){
                    player.getCards().remove(card);
                }
                notifyComponent.sendToAllUserOfRoom(roomId,user.getUsername()+"出的牌是"+cardList);
                break;
            }
            continue;
        }

    }
}
