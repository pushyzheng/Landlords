package site.pushy.landlords.pojo;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import site.pushy.landlords.common.util.JsonUtils;
import site.pushy.landlords.pojo.DO.User;

public class ApiResponseTest {

    @Test
    public void success() {
        ApiResponse<User> resp = ApiResponse.success(new User());
        System.out.println(JsonUtils.toJson(resp));
    }

    @Test
    public void error() {
        ApiResponse<Object> resp = ApiResponse.error(HttpStatus.NOT_FOUND);
        System.out.println(JsonUtils.toJson(resp));
    }

    @Test
    public void testError() {
        ApiResponse<Object> resp = ApiResponse.error(HttpStatus.NOT_FOUND, "不存在的页面");
        System.out.println(JsonUtils.toJson(resp));
    }
}
