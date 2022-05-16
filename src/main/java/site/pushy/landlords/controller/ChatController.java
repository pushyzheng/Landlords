package site.pushy.landlords.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import site.pushy.landlords.common.config.properties.LandlordsProperties;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ChatDTO;
import site.pushy.landlords.pojo.DTO.UserOutDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.ChatMessage;
import site.pushy.landlords.service.RoomService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天接口
 */
@RestController
@RequestMapping(value = "/chat", produces = "application/json")
public class ChatController {

    @Resource
    private NotifyComponent notifyComponent;

    @Resource
    private RoomService roomService;

    @Resource
    private LandlordsProperties landlordsProperties;

    @SuppressWarnings("UnstableApiUsage")
    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    /**
     * 向所在房间的用户或服务器的所有用户广播消息
     */
    @PostMapping
    public String chat(@SessionAttribute User curUser, @Valid @RequestBody ChatDTO body) {
        if (!checkLimit(curUser)) {
            throw new ForbiddenException("你说话太快啦~");
        }
        ChatMessage message = new ChatMessage(body.getContent());
        message.setTypeId(body.getType());
        message.setSender(UserOutDTO.fromUser(curUser));
        message.setDimension(body.getDimension());

        if (ChatDTO.DimensionType.ROOM.toString().equals(body.getDimension())) {
            Room room = roomService.getRoomForUser(curUser);
            message.setDimension(ChatDTO.DimensionType.ROOM.toString());
            boolean result = notifyComponent.sendToAllUserOfRoom(room.getId(), message);
            return RespEntity.success(result);
        } else if (ChatDTO.DimensionType.ALL.toString().equals(body.getDimension())) {
            return RespEntity.success(notifyComponent.sendToAllUser(message));
        } else {
            throw new BadRequestException("不支持的聊天范围: " + body.getDimension());
        }
    }

    /**
     * 聊天限流校验
     */
    @SuppressWarnings("UnstableApiUsage")
    private boolean checkLimit(User curUser) {
        RateLimiter rateLimiter = rateLimiterMap.get(curUser.getId());
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(landlordsProperties.getPermitsPerSecondOfChat());
            rateLimiterMap.put(curUser.getId(), rateLimiter);
            return true; // DO NOT acquire in first time
        }
        return rateLimiter.tryAcquire();
    }
}
