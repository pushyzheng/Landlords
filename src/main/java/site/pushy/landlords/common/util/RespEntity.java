package site.pushy.landlords.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pushy
 * @since 2019/1/1 16:45
 */
public class RespEntity {

    private static String toJsonString(Integer code, String message, Object data) {
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("message",message);
        jsonMap.put("code",code);
        jsonMap.put("data", data);
        return JSONObject.toJSONString(jsonMap, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String success(Object data) {
        return toJsonString(200, "", data);
    }

    public static String error(Integer code, String message) {
        return toJsonString(code, message, null);
    }

}
