package site.pushy.landlords.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import site.pushy.landlords.LandlordsApplication;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandlordsApplication.class)
public class JWTUtilTest {

    @Test
    public void encode() {
        String userId = UUID.randomUUID().toString();
        String token = JWTUtil.encode(userId);
        Assertions.assertTrue(StringUtils.hasLength(token));
    }

    @Test
    public void decode() {
        String userId = UUID.randomUUID().toString();
        String token = JWTUtil.encode(userId);

        Assertions.assertEquals(userId, JWTUtil.decode(token));
    }

    @Test
    public void decodeError() {
        Assertions.assertThrows(Exception.class, () -> JWTUtil.decode(""));
        Assertions.assertThrows(Exception.class, () -> JWTUtil.decode(null));
        Assertions.assertThrows(Exception.class, () -> JWTUtil.decode("123"));
    }
}
