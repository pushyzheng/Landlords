package site.pushy.landlords.pojo;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/29 20:44
 */
@Data
public class Room {

    private String id;  // 房间号

    private String password;  // 房间密码

    private List<Player> playerList;  // 当前玩家列表

    private List<WebSocketSession> userSessionList;  // 当家玩家客户端Session对象列表

}
