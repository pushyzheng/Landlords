package site.pushy.landlords.core.component;

import org.springframework.stereotype.Component;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxing
 * @since 2018/12/29 20:59
 */
@Component
public class RoomComponent {

    private ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<>();

    /**
     * 创建房间
     *
     * @param user
     * @return
     */
    public Room createRoom(User user, String roomPassword) {
        Room room = new Room();
        String roomid = newRoomid();
        Player player = new Player();
        player.setUser(user);
        player.setId(1);  //创建房间的人座位顺序为1
        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        room.setId(roomid);
        room.setUserList(userList);
        room.setPlayerList(playerList);
        //是否设置密码
        if (roomPassword != null && !roomPassword.isEmpty()) {
            //设置了
            room.setLocked(true);
            room.setPassword(roomPassword);
        } else {
            //没设置
            room.setLocked(false);
        }
        roomMap.put(roomid, room);
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
        Room room = roomMap.get(id);
        //检查房间是否存在
        if (room == null) {
            //房间不存在,返回消息
            throw new NotFoundException("该房间不存在,请核实您输入的房间号!");
        } else {
            //判断用户是否已经在房间内
            if (room.getUserList().contains(user)) {
                throw new ForbiddenException("您已经加入此房间,无法重复加入");
            } else {
                //检查房间中人数是否小于三人
                int roomsize = room.getPlayerList().size();
                if (roomsize >= 3) {
                    //房间人数大于三人不能加入,返回消息
                    throw new ForbiddenException("该房间已满,请寻找其他房间!");
                } else {
                    //房间人数小于三人可以加入
                    if (room.isLocked()) {
                        if (roomPassword == null)
                            throw new ForbiddenException("对不起,您输入的房间密码有误!");
                        //有密码
                        if (roomPassword.equals(room.getPassword())) {
                            //分配座位顺序
                            Player player = new Player();
                            List<Player> playerlist = room.getPlayerList();
                            List<Integer> idList = new ArrayList();
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
                            return "加入成功!";

                        } else {
                            //密码错误,返回消息
                            throw new ForbiddenException("对不起,您输入的房间密码有误!");
                        }

                    } else {
                        //没密码
                        Player player = new Player();
                        room.getUserList().add(user);
                        room.getPlayerList().add(player);
                        return "加入成功!";
                    }
                }
            }

        }

    }

    /**
     * 退出房间
     * @param id
     * @param user
     */
    public String exitRoom(String id, User user) {
        Room room = roomMap.get(id);
        if (room == null)
            throw new NotFoundException("该房间不存在");
        // Todo 房间内其他玩家退出房间失败
        room.getUserList().remove(user);
        for (Player player : room.getPlayerList()) {
            if (player.getUser().equals(user)) {
                room.getPlayerList().remove(player);
                break;
            }
        }
        int roomSize = room.getPlayerList().size();
        //检查房间内剩余人数是否为0,为0则解散
        if (roomSize == 0) {
            roomMap.remove(id);
        }
        return "退出房间成功";
    }

    /**
     * 发送消息给所有房间中的玩家
     */
    public boolean sendMessageToRoom(String id, String message) {
        return false;
    }


    /**
     * 列出所有已创建的房间
     */
    public List<Room> getRooms() {
        List<Room> rooms = new LinkedList<>();
        for (Map.Entry<String, Room> entry : roomMap.entrySet()) {
            rooms.add(entry.getValue());
            Collections.sort(entry.getValue().getPlayerList());
        }
        return rooms;
    }

    public Room getRoom(String roomId) {
        Room room = roomMap.get(roomId);
        if (room == null)
            throw new NotFoundException("该房间不存在");
        return room;
    }

    public void updateRoom(Room newRoom) {
        String id = newRoom.getId();
        if (roomMap.get(id) == null) {
            throw new NotFoundException("该房间不存在");
        }
        roomMap.put(id, newRoom);
    }

    /**
     * 获得下个玩家
     */
    public Player getNextPlayer(String id, User user) {
        Room room = roomMap.get(id);
        Map<Integer, Player> playerIdMap = new HashMap<>();
        for (Player player : room.getPlayerList()
        ) {
            playerIdMap.put(player.getId(), player);
        }
        for (Player player1 : room.getPlayerList()
        ) {
            if (player1.getUser().equals(user)) {
                int playerId = player1.getId();
                switch (playerId) {
                    case 1:
                        return playerIdMap.get(2);
                    case 2:
                        return playerIdMap.get(3);
                    case 3:
                        return playerIdMap.get(1);
                }
                break;
            }
            continue;
        }
        return null;
    }

    /**
     * 列出房间内每个玩家的牌
     */
    public Map<Player, List<Card>> getPlayerCards(String id) {
        Room room = roomMap.get(id);
        Map<Player, List<Card>> cardMap = new HashMap<>();
        for (Player player : room.getPlayerList()
        ) {
            List<Card> cardList = player.getCards();
            cardMap.put(player, cardList);
        }
        return cardMap;
    }

    /**
     * 生成不同的roomid
     */
    public String newRoomid() {
        //随机生成房间id
        int a = (int) ((Math.random() * 9 + 1) * 100000);
        String roomid = String.valueOf(a);
        //获取当前所有房间id的集合
        Iterator<String> iter = roomMap.keySet().iterator();
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

}
