package site.pushy.landlords.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import site.pushy.landlords.LandlordsApplication;
import site.pushy.landlords.pojo.DO.User;

import javax.annotation.Resource;

import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void getUserById() {
        String username = mockUser();
        User user = userService.getUserById(username);
        Assertions.assertNull(user);

        user = new User(username, "123");
        Assertions.assertTrue(userService.save(user));
    }

    @Test
    void getByCondition() {
        String openId = "abc";
        String username = mockUser();
        User user = new User(username, "123");
        user.setOpenid(openId);

        Assertions.assertTrue(userService.save(user));

        user = userService.getByCondition(UserService.Condition.builder()
                .name(username)
                .build());
        Assertions.assertNotNull(user);

        user = userService.getByCondition(UserService.Condition.builder()
                .openId(openId)
                .build());
        Assertions.assertNotNull(user);
    }

    @Test
    void save() {
        Assertions.assertTrue(userService.save(new User(mockUser(), "123")));
    }

    @Test
    void updateUser() {
        User user = new User(mockUser(), "123");
        Assertions.assertTrue(userService.save(user));

        String newPwd = "123456";
        user.setPassword(newPwd);
        Assertions.assertTrue(userService.updateUser(user));

        user = userService.getUserById(user.getId());
        Assertions.assertEquals(newPwd, user.getPassword());
    }

    private String mockUser() {
        return UUID.randomUUID().toString();
    }
}
