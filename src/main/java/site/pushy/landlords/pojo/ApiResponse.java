package site.pushy.landlords.pojo;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 518351530832465500L;

    private static final int SUCCESS_CODE = 0;
    private static final int ERROR_CODE = -1;

    private int code;

    private int status;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        response.setStatus(SUCCESS_CODE);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    public static ApiResponse<Object> error(HttpStatus status) {
        return error(status, status.getReasonPhrase());
    }

    public static ApiResponse<Object> error(HttpStatus status, String message) {
        Assert.notNull(status, "The status cannot be null");

        ApiResponse<Object> response = new ApiResponse<>();
        response.setData(null);
        response.setMessage(StringUtils.hasLength(message) ? message : status.getReasonPhrase());
        response.setStatus(ERROR_CODE);
        response.setCode(status.value());
        return response;
    }
}
