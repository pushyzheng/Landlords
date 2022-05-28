package site.pushy.landlords.core.component;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.common.util.EnvUtils;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fuxing
 * @since 2018/12/29 20:59
 */
@Component
public class RoomComponent {

    private static final String ROOM_MAP_KEY = "landlords::room:mapping";

    private static final String USER_ROOM_MAP_KEY = "landlords::room:user-mapping";

    @Resource
    private RedissonClient redissonClient;

    /**
     * 创建房间
     *
     * @param user         房主
     * @param title        房间标题
     * @param roomPassword 密码(可选)
     */
    public Room createRoom(User user, String title, String roomPassword) {
        RMap<String, String> userRoomRMap = getUserRoomRMap();
        String roomId = userRoomRMap.get(user.getId());
        if (StringUtils.hasLength(userRoomRMap.get(user.getId()))) {
            throw new ForbiddenException("用户已在房间号为 " + roomId + " 的房间");
        }

        roomId = newRoomid();
        Room room = new Room(roomId);
        room.setTitle(title);
        room.setOwner(user);
        // 默认情况下创建房间的人座位顺序为 1
        Player player = new Player(1);
        player.setUser(user);
        room.addUser(user);
        room.addPlayer(player);

        // 是否设置密码
        if (roomPassword != null && !roomPassword.isEmpty()) {
            room.setLocked(true);
            room.setPassword(roomPassword);
        }
        getRoomRMap().put(roomId, room);
        userRoomRMap.put(user.getId(), roomId);
        return room;
    }

    /**
     * 加入房间
     *
     * @param id
     * @param user
     * @author fuxing
     */
    public String joinRoom(String id, User user, String roomPassword) {
        RMap<String, String> userRoomRMap = getUserRoomRMap();
        String roomId = userRoomRMap.get(user.getId());
        if (StringUtils.hasLength(roomId)) {
            throw new ForbiddenException("用户已在房间号为 " + roomId + " 的房间");
        }
        RMap<String, Room> roomRMap = getRoomRMap();
        Room room = roomRMap.get(id);
        //检查房间是否存在
        if (room == null) {
            throw new NotFoundException("该房间不存在，请核实您输入的房间号!");
        }
        //判断用户是否已经在房间内
        if (room.getUserList().contains(user)) {
            throw new ForbiddenException("您已经加入此房间，无法重复加入");
        }
        //检查房间中人数是否小于三人
        if (room.getPlayerList().size() >= 3) {
            //房间人数大于三人不能加入,返回消息
            throw new ForbiddenException("该房间已满，请寻找其他房间!");
        }
        // 如果房间加锁, 检查密码是否正确
        if (room.isLocked() && !room.getPassword().equals(roomPassword)) {
            throw new ForbiddenException("对不起，您输入的房间密码有误!");
        }
        //分配座位顺序
        Player player = new Player();
        List<Player> playerlist = room.getPlayerList();
        List<Integer> idList = new ArrayList<>();
        if (playerlist.size() == 1) {
            Player player0 = playerlist.get(0);
            int id0 = player0.getId();
            idList.add(id0);
        } else {
            Player player0 = playerlist.get(0);
            Player player1 = playerlist.get(1);
            int id0 = player0.getId();
            int id1 = player1.getId();
            idList.add(id0);
            idList.add(id1);
        }
        if (!idList.contains(1)) {
            player.setId(1);
        } else if (!idList.contains(2)) {
            player.setId(2);
        } else {
            player.setId(3);
        }
        player.setUser(user);
        room.getUserList().add(user);
        room.getPlayerList().add(player);

        roomRMap.put(id, room);
        userRoomRMap.put(user.getId(), room.getId());
        return "加入成功!";
    }

    /**
     * 退出房间
     *
     * @return 房间是否被解散
     */
    public boolean exitRoom(String id, User curUser) {
        removeUserRoom(curUser.getId());

        RMap<String, Room> roomRMap = getRoomRMap();
        Room room = roomRMap.get(id);
        if (room == null) {
            throw new NotFoundException("该房间不存在");
        }
        room.removeUser(curUser.getId());
        room.removePlayer(curUser.getId());
        // 检查房间内剩余人数是否为 0，为 0 则解散
        if (room.getPlayerList().size() == 0) {
            roomRMap.remove(id);
            return true;
        }
        updateRoom(room);
        return false;
    }

    /**
     * 列出所有已创建的房间
     */
    public List<Room> getRooms() {
        List<Room> rooms = new LinkedList<>();
        for (Map.Entry<String, Room> entry : getRoomRMap().entrySet()) {
            rooms.add(entry.getValue());
            Collections.sort(entry.getValue().getPlayerList());
        }
        return rooms;
    }

    public Room getRoom(String roomId) {
        RMap<String, Room> roomRMap = getRoomRMap();
        if (!roomRMap.containsKey(roomId)) {
            throw new NotFoundException("该房间不存在");
        }
        return roomRMap.get(roomId);
    }

    public void updateRoom(Room newRoom) {
        RMap<String, Room> roomRMap = getRoomRMap();
        if (!roomRMap.containsKey(newRoom.getId())) {
            throw new NotFoundException("该房间不存在");
        }
        roomRMap.put(newRoom.getId(), newRoom);
    }

    /**
     * 通过 userId 获取当前用户在其房间内游戏内的牌列表
     *
     * @param userId 用户 ID
     */
    public List<Card> getUserCards(String userId) {
        Room room = getUserRoom(userId);
        Player player = room.getPlayerByUserId(userId);
        if (player == null) {
            throw new NotFoundException("未找到该玩家");
        }
        return player.getCards();
    }

    /**
     * 获取当前用户所在的房间对象
     */
    public Room getUserRoom(String userId) {
        RMap<String, String> userRoomRMap = getUserRoomRMap();
        if (!userRoomRMap.containsKey(userId)) {
            throw new BadRequestException("玩家还未加入房间内");
        }
        return getRoom(userRoomRMap.get(userId));
    }

    /**
     * 移除用户当前所在房间的映射关系
     */
    private void removeUserRoom(String userId) {
        getUserRoomRMap().remove(userId);
    }

    /**
     * 生成不同的roomid
     */
    private String newRoomid() {
        //随机生成房间id
        int a = (int) ((Math.random() * 9 + 1) * 100000);
        String roomid = String.valueOf(a);
        //获取当前所有房间id的集合
        Iterator<String> iter = getRoomRMap().keySet().iterator();
        List roomidList = new ArrayList();
        while (iter.hasNext()) {
            String key = iter.next();
            roomidList.add(key);
        }

        //保证每次随机生成的roomid不重复
        if (roomidList.contains(roomid)) {
            //房间号已存在则重新生成
            return newRoomid();
        } else {
            //不存在则生成roomid
            return roomid;
        }
    }

    /**
     * 房间号和与该房间所对应的 Room 对象映射 Map
     */
    private RMap<String, Room> getRoomRMap() {
        return redissonClient.getMap(buildKey(ROOM_MAP_KEY), JsonJacksonCodec.INSTANCE);
    }

    /**
     * 用户玩家当前所在的房间号映射 Map
     */
    private RMap<String, String> getUserRoomRMap() {
        return redissonClient.getMap(buildKey(USER_ROOM_MAP_KEY), StringCodec.INSTANCE);
    }

    public void clearAll() {
        if (EnvUtils.isNotTest()) {
            throw new UnsupportedOperationException("ONLY invoke this method in test env");
        }
        getRoomRMap().clear();
        getUserRoomRMap().clear();
    }

    private String buildKey(String original) {
        return EnvUtils.isTest() ? "test::" + original : original;
    }
}
