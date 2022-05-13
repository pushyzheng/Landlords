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
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
@Slf4j
public class RoomComponentTest {

    @Resource
    private RoomComponent roomComponent;

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
        // 房间不存在
        Assertions.assertThrows(NotFoundException.class, () -> roomComponent.joinRoom("unknown", mockUser(), ""));

        String title = "test";
        String pwd = "123";
        Room room = roomComponent.createRoom(mockUser(), title, pwd);

        // 密码错误
        Assertions.assertThrows(ForbiddenException.class, () -> roomComponent.joinRoom(room.getId(), mockUser(), ""));

        Assert.assertEquals("加入成功!", roomComponent.joinRoom(room.getId(), mockUser(), pwd));
    }

    @Test
    public void exitRoom() {
    }

    @Test
    public void getRooms() {
    }

    @Test
    public void getRoom() {
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
