package site.pushy.landlords.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.pushy.landlords.common.exception.BaseException;
import site.pushy.landlords.pojo.ApiResponse;

@ControllerAdvice
@Slf4j
public class AppWideExceptionHandler {

    /**
     * 处理GET请求必要查询字符串缺少的错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<Object> missingParameterException(MissingServletRequestParameterException e) {
        String errorMsg;
        if (e != null) {
            String parameterName = e.getParameterName();
            errorMsg = String.format("Required parameter '%s' is not present", parameterName);
        } else {
            errorMsg = "请求数据校验不合法";
        }
        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理POST表单校验不通过的错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<Object> argumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg;
        if (e != null) {
            BindingResult result = e.getBindingResult();
            FieldError error = result.getFieldError();
            String field = error.getField();
            errorMsg = String.format("Form parameter '%s' is not present", field);
        } else {
            errorMsg = "请求表单参数不合法";
        }
        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> errorException(BaseException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> errorException(Exception e) {
        log.error("未知异常", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误"));
    }
}
