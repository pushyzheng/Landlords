package site.pushy.landlords.common.util;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import site.pushy.landlords.pojo.DO.User;

import static org.junit.Assert.*;

public class JsonUtilsTest {

    @Test
    public void toJson() {
        User user = new User();

        System.out.println(JsonUtils.toJson(user));
    }

    @Test
    public void parse() {
        Assertions.assertThrows(RuntimeException.class, () -> JsonUtils.parse("test", User.class));
    }
}
