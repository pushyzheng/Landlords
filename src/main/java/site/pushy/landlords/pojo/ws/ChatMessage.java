package site.pushy.landlords.pojo.ws;

import lombok.Getter;
import lombok.Setter;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.DTO.UserOutDTO;

import java.util.Date;

/**
 * 聊天消息
 */
@Getter
@Setter
public class ChatMessage extends Message {

    private UserOutDTO sender;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 预设类型 ID
     */
    private int typeId;

    private String dimension;

    /**
     * 创建时间
     */
    private Date createTime;

    public ChatMessage() {
    }

    public ChatMessage(String content) {
        this.content = content;
        this.createTime = new Date();
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.CHAT;
    }
}
