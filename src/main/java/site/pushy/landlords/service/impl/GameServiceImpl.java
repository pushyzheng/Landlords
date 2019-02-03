package site.pushy.landlords.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.core.CardDistribution;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.*;
import site.pushy.landlords.service.GameService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/9 21:20
 */
@Service
public class GameServiceImpl implements GameService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private NotifyComponent notifyComponent;

    /**
     * 玩家准备游戏
     * @return 是否开局，当房间的内的所有玩家都准备，并且人数已满3人，即为开局
     */
    @Override
    public boolean readyGame(User user) {
        Room room = roomComponent.getUserRoom(user.getId());

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
        room.setStatus(RoomStatusEnum.PLAYING);  // 更新游戏状态为游戏中

        /* 构造 CardDistribution类，进行发牌 */
        CardDistribution distribution = room.getDistribution();
        if (distribution == null) {
            distribution = new CardDistribution();
            room.setDistribution(distribution);
        }
        distribution.refresh();  // 洗牌

        List<Player> playerList = room.getPlayerList();
        for (Player player : playerList) {
            List<Card> cards = distribution.getCards(player.getId());
            player.setCards(cards);
        }

        roomComponent.updateRoom(room);
        /* 通知房间内所有的玩家客户端开始游戏 */
        notifyComponent.sendToAllUserOfRoom(roomId, new StartGameMessage(roomId));

        /* 在分牌之后随机分牌一个玩家进行叫地主 */
        int order = (int) (1 + Math.random() * (3 - 1 + 1));  //从1到3的int型随机数
        for (Player player : room.getPlayerList()) {
            if (player.getId().equals(order)) {
                // 通知被选择叫牌的玩家客户端开始叫牌
                logger.info(String.format("【%s】通知玩家 %s 叫牌", room.getId(), player.getUser().getUsername()));
                notifyComponent.sendToUser(player.getUser().getId(), new BidMessage());
                break;
            }
        }
    }

    @Override
    public void want(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                player.setIdentity(IdentityEnum.LANDLORD);
                // 将三张地主牌分配给地主
                CardDistribution distribution = room.getDistribution();
                player.addCards(distribution.getTopCards());
            } else {
                player.setIdentity(IdentityEnum.FARMER);
            }
        }
        roomComponent.updateRoom(room);
        /* 通知玩家内所有玩家，叫牌结束 */
        notifyComponent.sendToAllUserOfRoom(room.getId(), new BidEndMessage());
    }

    @Override
    public void noWant(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                int playerId = player.getId();
                if (playerId == 1) {
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId() == 2) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(), new BidMessage());
                            break;
                        }
                    }
                } else if (playerId == 2) {
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId() == 3) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(), new BidMessage());
                            break;
                        }
                    }
                } else {
                    for (Player nextPlayer : room.getPlayerList()) {
                        if (nextPlayer.getId() == 1) {
                            notifyComponent.sendToUser(nextPlayer.getUser().getId(), new BidMessage());
                            break;
                        }
                    }
                }
                logger.info(String.format("玩家 %d 选择不叫，由下家玩家 %d 叫牌", playerId,
                        playerId == 3 ? 1 : playerId + 1));
                break;
            }
        }
    }

    @Override
    public void playCard(User user, List<Card> cardList) {
        Room room = roomComponent.getUserRoom(user.getId());
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                player.removeCards(cardList);

                /* 通知房间内的所有玩家客户端有玩家出牌 */
                Message message = new PlayCardMessage(user.getId(), cardList);
                notifyComponent.sendToAllUserOfRoom(room.getId(), message);
                /* 通知下一个玩家出牌 */
                User nextUser = room.getUserByPlayerId(player.getNextPlayerId());
                notifyComponent.sendToUser(nextUser.getId(), new PleasePlayCardMessage());
                break;
            }
        }
    }

    @Override
    public void pass(User user) {

    }
}
