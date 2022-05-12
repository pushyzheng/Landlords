package site.pushy.landlords.core.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.pushy.landlords.LandlordsApplication;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
public class RoomComponentTest {

    @Resource
    private RoomComponent roomComponent;

    @Test
    public void createRoom() {
        String title = "Hello";
        String pwd = "123";

        Room room = roomComponent.createRoom(mockUser(), pwd, title);

        Assert.assertEquals(title, room.getTitle());
        Assert.assertTrue(title, room.isLocked());
        Assert.assertEquals(pwd, room.getPassword());
    }

    @Test
    public void joinRoom() {
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
        user.setId("c91fb34896fa4e81aafffc155ba9c620");
        user.setUsername("Erica");
        return user;
    }
}
