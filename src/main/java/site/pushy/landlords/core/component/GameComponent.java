package site.pushy.landlords.core.component;

import site.pushy.landlords.pojo.Round;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pushy
 * @since 2019/1/24 18:11
 */
public class GameComponent {

    private Map<String, Round> gameRoundMap = new ConcurrentHashMap<>();

    public void addRound(String roomId) {
        Round round = new Round();
        gameRoundMap.put(roomId, round);
    }

    public void removeRound(String roomId) {
        gameRoundMap.remove(roomId);
    }

}
