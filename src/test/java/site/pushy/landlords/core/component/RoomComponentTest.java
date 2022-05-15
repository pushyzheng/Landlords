package site.pushy.landlords.core.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.pushy.landlords.LandlordsApplication;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
@Slf4j
public class RoomComponentTest {

    @Resource
    private RoomComponent roomComponent;

    private static final String SUCCESS = "加入成功!";

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
        Assertions.assertEquals(1, room.getPlayerByUserId(user1.getId()).getId());

        // 密码错误
        Assertions.assertThrows(ForbiddenException.class, () -> roomComponent.joinRoom(room.getId(), user2, ""));

        // 玩家 2 加入房间成功
        Assert.assertEquals(SUCCESS, roomComponent.joinRoom(room.getId(), user2, pwd));
        // 房间任务为 2 人
        Assertions.assertEquals(2, room.getPlayerList().size());
        // 两个用户成功加入
        Set<String> users = room.getPlayerList().stream().map(p -> p.getUser().getId())
                .collect(Collectors.toSet());
        Assertions.assertTrue(users.contains(user1.getId()));
        Assertions.assertTrue(users.contains(user2.getId()));

        Assertions.assertEquals(SUCCESS, roomComponent.joinRoom(room.getId(), user3, pwd));
        // 房间任务为 3 人
        Assertions.assertEquals(3, room.getPlayerList().size());
        // 两个用户成功加入
        users = room.getPlayerList().stream().map(p -> p.getUser().getId())
                .collect(Collectors.toSet());
        Assertions.assertTrue(users.contains(user3.getId()));

        // 第四个玩家加入失败
        Assertions.assertThrows(ForbiddenException.class, () -> roomComponent.joinRoom(room.getId(), mockUser(), pwd));
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
    }

    @Test
    public void getRooms() {
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
    }

    @Test
    public void getUserCards() {
    }

    @Test
    public void getUserRoom() {
    }

    private User mockUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("Erica");
        return user;
    }
}
