package site.pushy.landlords.service.impl;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.core.CardDistribution;
import site.pushy.landlords.core.CardUtils;
import site.pushy.landlords.core.GradeComparison;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.PassMessage;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.RoundResult;
import site.pushy.landlords.pojo.ws.*;
import site.pushy.landlords.service.GameService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author Pushy
 * @since 2019/1/9 21:20
 */
@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final Random random = new Random();

    @Resource
    private RoomComponent roomComponent;

    @Resource
    private NotifyComponent notifyComponent;

    @Override
    public boolean readyGame(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        // 更改玩家的准备状态
        Player player = room.getPlayerByUserId(user.getId());
        player.setReady(true);

        roomComponent.updateRoom(room);
        // 玩家准备通知
        notifyComponent.sendToAllUserOfRoom(room.getId(), new ReadyGameMessage(user.getId()));

        // 检查是否房间内的人数等于3人，并且全部都处于准备中的状态
        boolean isAllReady = room.isAllReady();
        if (isAllReady) {
            room.getSync().lock();
            Try.run(() -> startGame(room))
                    .onFailure(throwable -> logger.error("[{}] 开始游戏异常", room.getId(), throwable))
                    .andFinally(() -> room.getSync().unlock());
        }
        return isAllReady;
    }

    @Override
    public void unReadyGame(User curUser) {
        Room room = roomComponent.getUserRoom(curUser.getId());
        Player player = room.getPlayerByUserId(curUser.getId());
        player.setReady(false);

        roomComponent.updateRoom(room);
        // 取消准备通知
        notifyComponent.sendToAllUserOfRoom(room.getId(), new UnReadyGameMessage(curUser.getId()));
    }

    @Override
    public void want(User user, int score) {
        Room room = roomComponent.getUserRoom(user.getId());
        logger.info("[{}] 玩家 {} 叫牌，分数为 {} 分", room.getId(), user.getUsername(), score);

        room.setMultiple(score);
        User landlordUser = null;
        for (Player player : room.getPlayerList()) {
            if (player.getUser().getId().equals(user.getId())) {
                if (player.getId() != room.getBiddingPlayer()) {
                    throw new ForbiddenException("不是当前用户的叫牌回合");
                }
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
        if (landlordUser == null) {
            throw new IllegalStateException("选取的地主玩家不能为空");
        }
        room.setPrePlayTime(System.currentTimeMillis());
        roomComponent.updateRoom(room);
        notifyComponent.sendToAllUserOfRoom(room.getId(), new BidEndMessage()); // 叫牌结束
        notifyComponent.sendToUser(landlordUser.getId(), new PleasePlayCardMessage());
        logger.info("[{}] 玩家 {} 成为地主", room.getId(), landlordUser.getUsername());
    }

    @Override
    public void noWant(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        room.incrBiddingPlayer();
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
                logger.info("[{}] 玩家 {} 选择不叫，由下家 {} 玩家叫牌", room.getId(),
                        playerId, player.getNextPlayerId());
                break;
            }
        }
        roomComponent.updateRoom(room);
    }

    @Override
    public RoundResult playCard(User user, List<Card> cardList) {
        Room room = roomComponent.getUserRoom(user.getId());
        logger.info("[{}] 玩家 {} 出牌: {}", room.getId(), user.getUsername(), cardList);

        Player player = room.getPlayerByUserId(user.getId());
        // 校验玩家出的牌是否符合斗地主规则规范
        TypeEnum myType = CardUtils.getCardsType(cardList);
        if (myType == null) {
            logger.warn("[{}] 玩家 {} 打出的牌不符合规则", room.getId(), user.getUsername());
            throw new ForbiddenException("玩家打出的牌不符合规则");
        }
        if (room.getPreCards() != null && room.getPrePlayerId() != player.getId()) {
            // 判断该玩家打出的牌是否能比上家出的牌大
            TypeEnum preType = CardUtils.getCardsType(room.getPreCards());
            boolean canPlay = GradeComparison.canPlayCards(cardList, myType, room.getPreCards(), preType);
            logger.info("【{}】 myType：{}，preType：{}，canPlay：{}", user.getUsername(),
                    myType.getName(), preType.getName(), canPlay);
            if (!canPlay) {
                throw new ForbiddenException("该玩家出的牌管不了上家");
            }
        }
        removeNextPlayerRecentCards(room, player);   // 移除下一个玩家最近出的牌
        player.setRecentCards(cardList);
        // 移除玩家列表中打出的牌
        player.removeCards(cardList);
        Message message = new PlayCardMessage(user, cardList, myType); // 有玩家出牌通知
        notifyComponent.sendToAllUserOfRoom(room.getId(), message);
        // 判断出的牌是否是炸弹或者王炸，如果是，则底分加倍
        if (myType == TypeEnum.BOMB || myType == TypeEnum.JOKER_BOMB) {
            room.doubleMultiple();
        }
        RoundResult result = null;
        if (player.getCards().size() == 0) { // 判断该玩家已经出完牌
            if (isSpring(room, player)) {
                room.doubleMultiple();
            }
            logger.info("[{}] 游戏结束，{} 获胜！", room.getId(), player.getIdentityName());
            result = getResult(room, player);
            room.reset();
        } else {
            logger.info("[{}] 玩家 {} 出牌，类型为 {}，下一个出牌者序号为：{}", room.getId(),
                    player.getUser().getUsername(), myType.getName(), player.getNextPlayerId());
            room.setPreCards(cardList);
            room.setPrePlayerId(player.getId());
            room.incrStep();
            User nextUser = room.getUserByPlayerId(player.getNextPlayerId());  // 通知下一个玩家出牌
            notifyComponent.sendToUser(nextUser.getId(), new PleasePlayCardMessage());
        }
        room.setPrePlayTime(System.currentTimeMillis());
        roomComponent.updateRoom(room);
        return result;
    }

    @Override
    public void pass(User user) {
        Room room = roomComponent.getUserRoom(user.getId());
        Player player = room.getPlayerByUserId(user.getId());

        removeNextPlayerRecentCards(room, player);
        room.incrStep();
        room.setPrePlayTime(System.currentTimeMillis());
        roomComponent.updateRoom(room);

        logger.info("[{}] 玩家 {} 要不起，下一个出牌者序号为：{}", room.getId(),
                user.getUsername(), player.getNextPlayerId());
        User nextUser = room.getUserByPlayerId(player.getNextPlayerId()); // 通过下一个玩家出牌
        notifyComponent.sendToUser(nextUser.getId(), new PleasePlayCardMessage());
        notifyComponent.sendToAllUserOfRoom(room.getId(), new PassMessage(user));
    }

    private void startGame(Room room) {
        if (room.getStatus() == RoomStatusEnum.PLAYING) {
            throw new IllegalStateException("房间游戏已经开始了");
        }
        String roomId = room.getId();
        room.setStatus(RoomStatusEnum.PLAYING);  // 更新游戏状态为游戏中

        // 构造 CardDistribution类，进行发牌
        room.setDistribution(new CardDistribution());
        CardDistribution distribution = room.getDistribution();
        distribution.refresh();  // 洗牌

        List<Player> playerList = room.getPlayerList();
        for (Player player : playerList) {
            List<Card> cards = distribution.getCards(player.getId());
            player.setCards(cards);
            player.setReady(false);
        }
        // 通知房间内所有的玩家客户端开始游戏
        notifyComponent.sendToAllUserOfRoom(roomId, new StartGameMessage(roomId));

        // 在分牌之后随机分牌一个玩家进行叫地主
        int order = random.nextInt(3) + 1;  //从1到3的int型随机数
        room.setBiddingPlayer(order);
        Player player = room.getPlayerById(order);
        // 通知被选择叫牌的玩家客户端开始叫牌
        logger.info("[{}] 通知玩家 {} 叫牌", room.getId(), player.getUser().getUsername());
        notifyComponent.sendToUser(player.getUser().getId(), new BidMessage());
        roomComponent.updateRoom(room);
    }

    /**
     * 春天判断:
     * <p>
     * 1. 如果是农民, 地主剩 20 张牌
     * 2. 如果是地主, 则两个农民都必须剩 17 张牌
     */
    private boolean isSpring(Room room, Player winner) {
        if (winner.isLandlord()) {
            return room.getFarmers().stream().map(s -> s.getCards().size()).allMatch(s -> s == 17);
        } else {
            return room.getLandlord().getCards().size() == 20;
        }
    }

    private RoundResult getResult(Room room, Player player) {
        RoundResult result = new RoundResult(player.getIdentity(), room.getMultiple());
        for (Player each : room.getPlayerList()) {
            if (each.getIdentity() == IdentityEnum.LANDLORD) {
                result.setLandlord(each.getId());
            }
        }
        return result;
    }

    private void removeNextPlayerRecentCards(Room room, Player player) {
        Player nextPlayer = room.getPlayerById(player.getNextPlayerId());
        nextPlayer.clearRecentCards();
    }
}
