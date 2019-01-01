package site.pushy.landlords.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.common.exception.UnauthorizedException;
import site.pushy.landlords.common.util.RespEntity;

@ControllerAdvice
public class AppWideExceptionHandler {

    /**
     * 处理GET请求必要查询字符串缺少的错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String missingParameterException(MissingServletRequestParameterException e) {
        String errorMsg;
        if (e != null) {
            String parameterName = e.getParameterName();
            errorMsg = String.format("Required parameter '%s' is not present", parameterName);
        } else {
            errorMsg = "请求数据校验不合法";
        }
        return RespEntity.error(400, errorMsg);
    }

    /**
     * 处理POST表单校验不通过的错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String argumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg;
        if (e != null) {
            BindingResult result = e.getBindingResult();
            FieldError error = result.getFieldError();
            String field = error.getField();
            errorMsg = String.format("Form parameter '%s' is not present", field);
        } else {
            errorMsg = "请求表单参数不合法";
        }
        return RespEntity.error(400, errorMsg);
    }

    /**
     * HTTP 400 错误
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String errorException(BadRequestException e) {
        return errorResponse(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * HTTP 404 错误
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String errorException(NotFoundException e) {
        return errorResponse(e, HttpStatus.NOT_FOUND);
    }

    /**
     * HTTP 401 错误
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String errorException(UnauthorizedException e) {
        return errorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    /**
     * HTTP 403 错误
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String errorException(ForbiddenException e) {
        return errorResponse(e, HttpStatus.FORBIDDEN);
    }

    private String errorResponse(RuntimeException e, HttpStatus status) {
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = status.getReasonPhrase();
        }
        return RespEntity.error(status.value(), message);
    }



}
