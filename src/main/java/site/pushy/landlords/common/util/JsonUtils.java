package site.pushy.landlords.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@UtilityClass
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return Try.of(() -> objectMapper.writeValueAsString(obj))
                .onFailure(throwable -> log.error("toJson error, className: {}", obj.getClass().getName(), throwable))
                .getOrElseThrow(throwable -> new RuntimeException("JSON 序列化失败", throwable));
    }

    public <T> T parse(String s, Class<T> clazz) {
        if (!StringUtils.hasLength(s)) {
            return null;
        }
        return Try.of(() -> objectMapper.readValue(s, clazz))
                .onFailure(throwable -> log.error("parse error, string: {}", s, throwable))
                .getOrElseThrow(throwable -> new RuntimeException("JSON 反序列化失败", throwable));
    }
}
