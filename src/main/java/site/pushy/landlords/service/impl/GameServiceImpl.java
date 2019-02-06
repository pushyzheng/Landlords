package site.pushy.landlords.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.core.CardDistribution;
import site.pushy.landlords.core.CardUtil;
import site.pushy.landlords.core.GradeComparison;
import site.pushy.landlords.core.TypeJudgement;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.core.enums.TypeEnum;
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
        boolean res = true;
        if (playerList.size() != 3) {
            res = false;
        } else {
            for (Player player : room.getPlayerList()) {
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
    public void want(User user, int score) {
        logger.info(String.format("玩家【%s】叫牌，分数为：%d 分", user.getUsername(), score));

        Room room = roomComponent.getUserRoom(user.getId());
        room.setMultiple(score);
        User landlordUser = null;
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                landlordUser = player.getUser();
                room.setStepNum(player.getId());
                player.setIdentity(IdentityEnum.LANDLORD);
                // 将三张地主牌分配给地主
                CardDistribution distribution = room.getDistribution();
                player.addCards(distribution.getTopCards());
            } else {
                player.setIdentity(IdentityEnum.FARMER);
            }
        }
        roomComponent.updateRoom(room);
        notifyComponent.sendToAllUserOfRoom(room.getId(), new BidEndMessage()); // 叫牌结束
        // 通知地主玩家出牌
        if (landlordUser != null) {
            notifyComponent.sendToUser(landlordUser.getId(), new PleasePlayCardMessage());
        }
        logger.info(String.format("【%s】 玩家【%s】成为地主", room.getId(),
                landlordUser != null ? landlordUser.getUsername() : null));
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
                logger.info(String.format("玩家【%d】选择不叫，由下家【%d】玩家叫牌", playerId,
                        playerId == 3 ? 1 : playerId + 1));
                break;
            }
        }
    }

    @Override
    public void playCard(User user, List<Card> cardList) {
        Room room = roomComponent.getUserRoom(user.getId());
        TypeEnum myType = CardUtil.getCardType(cardList);
        if (myType == null) {
            throw new ForbiddenException("玩家打出的牌不符合规则");
        }
        if (room.getPreCards() != null) {
            /* 判断该玩家打出的牌是否能比上家出的牌大 */
            TypeEnum preType = CardUtil.getCardType(room.getPreCards());
            boolean canPlay = GradeComparison.canPlayCards(cardList, myType, room.getPreCards(), preType);
            logger.info(String.format("【%s】 myType：%s，preType：%s，canPlay：%b", user.getUsername(),
                    myType.getName(), preType.getName(), canPlay));
            if (!canPlay) {
                throw new ForbiddenException("该玩家出的牌管不了上家");
            }
        }
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                int remainder = room.getStepNum() % 3;
                if (remainder == 0) {
                    if (player.getId() != 3) throw new ForbiddenException("当前不是该玩家出牌回合");
                } else {
                    if (player.getId() != remainder) throw new ForbiddenException("当前不是该玩家出牌回合");
                }
                // 移除玩家列表中打出的牌
                player.removeCards(cardList);
                Message message = new PlayCardMessage(user.getId(), cardList); // 有玩家出牌通知
                notifyComponent.sendToAllUserOfRoom(room.getId(), message);
                if (player.getCards().size() == 0) { // 判断该玩家已经出完牌
                    logger.info(String.format("【%s】游戏结束，【%s】获胜！", room.getId(), player.getIdentityName()));
                    room.refresh();
                    // Todo 游戏结束，开始计分
                    Message gameEndMessage = new GameEndMessage(player.getIdentity());  // 游戏结束通知
                    notifyComponent.sendToAllUserOfRoom(room.getId(), gameEndMessage);
                }
                else {
                    logger.info(String.format("玩家【%s】出牌，下一个出牌者序号为：%s", player.getUser().getUsername(),
                            player.getNextPlayerId()));
                    room.setPreCards(cardList);
                    room.setStepNum(room.getStepNum() + 1);
                    User nextUser = room.getUserByPlayerId(player.getNextPlayerId());  // 通知下一个玩家出牌
                    notifyComponent.sendToUser(nextUser.getId(), new PleasePlayCardMessage());
                }
                break;
            }
        }
        roomComponent.updateRoom(room);
    }

    @Override
    public void pass(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        Player player = room.getPlayerByUserId(user.getId());
        room.setStepNum(room.getStepNum() + 1);

        logger.info(String.format("玩家【%s】选择不出，下一个出牌者序号为：%d", user.getUsername(), player.getId()));

        User nextUser = room.getUserByPlayerId(player.getNextPlayerId()); // 通过下一个玩家出牌
        notifyComponent.sendToUser(nextUser.getId(), new PleasePlayCardMessage());
    }
}
