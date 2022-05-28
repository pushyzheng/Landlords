package site.pushy.landlords.core.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import site.pushy.landlords.LandlordsApplication;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
@Slf4j
@ActiveProfiles("test")
public class RoomComponentTest {

    @Resource
    private RoomComponent roomComponent;

    private static final String SUCCESS = "加入成功!";

    /**
     * Cleans all room data before every test method
     */
    @Before
    public void before() {
        roomComponent.clearAll();
    }

    @Test
    public void createRoom() {
        String title = "Hello";
        String pwd = "123";

        Room room = roomComponent.createRoom(mockUser(), title, pwd);

        Assert.assertEquals(title, room.getTitle());
        Assert.assertTrue(room.isLocked());
        Assert.assertEquals(pwd, room.getPassword());
    }

    @Test
    public void createRoomDuplicate() {
        createRoom();
    }

    @Test
    public void joinRoom() {
        User user1 = mockUser(), user2 = mockUser(), user3 = mockUser();
        String title = "test";
        String pwd = "123";

        // 房间不存在
        Assertions.assertThrows(NotFoundException.class, () -> roomComponent.joinRoom("unknown", user1, ""));

        Room room = roomComponent.createRoom(user1, title, pwd);
        final String roomId = room.getId();
        Assertions.assertEquals(1, room.getPlayerByUserId(user1.getId()).getId());

        // 密码错误
        Assertions.assertThrows(ForbiddenException.class, () -> roomComponent.joinRoom(roomId, user2, ""));

        // 玩家 2 加入房间成功
        Assert.assertEquals(SUCCESS, roomComponent.joinRoom(roomId, user2, pwd));

        room = roomComponent.getRoom(roomId);
        // 房间任务为 2 人
        Assertions.assertEquals(2, room.getPlayerList().size());
        // 两个用户成功加入
        Set<String> users = room.getPlayerList().stream().map(p -> p.getUser().getId())
                .collect(Collectors.toSet());
        Assertions.assertTrue(users.contains(user1.getId()));
        Assertions.assertTrue(users.contains(user2.getId()));

        Assertions.assertEquals(SUCCESS, roomComponent.joinRoom(roomId, user3, pwd));

        room = roomComponent.getRoom(roomId);
        // 房间任务为 3 人
        Assertions.assertEquals(3, room.getPlayerList().size());
        // 两个用户成功加入
        users = room.getPlayerList().stream().map(p -> p.getUser().getId())
                .collect(Collectors.toSet());
        Assertions.assertTrue(users.contains(user3.getId()));

        // 第四个玩家加入失败
        Assertions.assertThrows(ForbiddenException.class, () -> roomComponent.joinRoom(roomId, mockUser(), pwd));
    }

    @Test
    public void joinNoPasswordRoom() {
        User user1 = mockUser();
        User user2 = mockUser();

        Room room = roomComponent.createRoom(user1, "test", null);
        String result = roomComponent.joinRoom(room.getId(), user2, "");
        Assertions.assertEquals(SUCCESS, result);
    }

    @Test
    public void exitRoom() {
        User user = mockUser();
        User user2 = mockUser();

        Room room = roomComponent.createRoom(user, "123", "");
        roomComponent.joinRoom(room.getId(), user2, "");

        // 房间未解散
        Assertions.assertFalse(roomComponent.exitRoom(room.getId(), user));
        // 房间解散
        Assertions.assertTrue(roomComponent.exitRoom(room.getId(), user2));
    }

    @Test
    public void getRooms() {
        Assertions.assertEquals(0, roomComponent.getRooms().size());

        User user = mockUser();
        // create
        Room room = roomComponent.createRoom(user, "123", "");
        List<Room> rooms = roomComponent.getRooms();
        Assertions.assertEquals(1, rooms.size());
        Assertions.assertEquals(room, rooms.get(0));

        // exit
        Assertions.assertTrue(roomComponent.exitRoom(room.getId(), user));
        Assertions.assertEquals(0, roomComponent.getRooms().size());
    }

    @Test
    public void getRoom() {
        User user = mockUser();
        Room newRoom = roomComponent.createRoom(user, "test", "");

        Room room = roomComponent.getRoom(newRoom.getId());

        Assertions.assertEquals(newRoom.getId(), room.getId());
        Assertions.assertEquals(newRoom.getTitle(), room.getTitle());
        Assertions.assertEquals(newRoom.getStatus(), room.getStatus());
    }

    @Test
    public void updateRoom() {
        Room room = roomComponent.createRoom(mockUser(), "123", "");
        room.incrStep();
        room.incrStep();
        room.setStatus(RoomStatusEnum.PLAYING);
        roomComponent.updateRoom(room);

        // test
        room = roomComponent.getRoom(room.getId());
        Assertions.assertEquals(RoomStatusEnum.PLAYING, room.getStatus());
        Assertions.assertEquals(1, room.getStepNum());
    }

    @Test
    public void getUserCards() {
    }

    @Test
    public void getUserRoom() {
        // 成功加入房间
        User user = mockUser();
        Room room = roomComponent.createRoom(user, "123", "");
        Assertions.assertEquals(room, roomComponent.getUserRoom(user.getId()));

        // 未加入房间
        Assertions.assertThrows(BadRequestException.class, () -> {
            roomComponent.getUserRoom(mockUser().getId());
        });
    }

    private User mockUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("Erica");
        return user;
    }
}
