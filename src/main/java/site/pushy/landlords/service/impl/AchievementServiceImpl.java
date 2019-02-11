package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.dao.AchievementMapper;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.Achievement;
import site.pushy.landlords.pojo.DO.AchievementExample;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.RoundResult;
import site.pushy.landlords.pojo.ws.GameEndMessage;
import site.pushy.landlords.service.AchievementService;

import static site.pushy.landlords.core.enums.IdentityEnum.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fuxing
 * @since 2019/2/10 19:22
 */
@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementMapper achievementMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private NotifyComponent notifyComponent;

    @Override
    public void countScore(User curUser, RoundResult result) {
        Room room = roomComponent.getUserRoom(curUser.getId());
        GameEndMessage gameEndMessage = new GameEndMessage();
        List<Map<String, Integer>> resList = new ArrayList<>();
        int multiple = result.getMultiple();

        for (User user : room.getUserList()) {
            Map<String, Integer> map = new HashMap<>();
            boolean isWinning = false;
            Player player = room.getPlayerByUserId(user.getId());
            // 地主获胜
            if (result.getWinIdentity() == LANDLORD) {
                if (player.getId() == result.getLandlord()) {
                    isWinning = true;
                    user.incrMoney(multiple * 2);
                    map.put(user.getUsername(), multiple * 2);
                } else {
                    user.descMoney(multiple);
                    map.put(user.getUsername(), -multiple);
                }
                gameEndMessage.setWiningIdentity(LANDLORD);
            }
            // 农民获胜
            else {
                if (player.getId() == result.getLandlord()) {
                    user.descMoney(multiple * 2);
                    map.put(user.getUsername(), -multiple * 2);
                } else {
                    isWinning = true;
                    user.incrMoney(multiple);
                    map.put(user.getUsername(), multiple);
                }
                gameEndMessage.setWiningIdentity(FARMER);
            }
            resList.add(map);
            userMapper.updateByPrimaryKeySelective(user);
            updateAchievement(user.getId(), isWinning);   // 更新战绩
        }
        // 游戏结束——计分消息通知
        gameEndMessage.setResList(resList);
        notifyComponent.sendToAllUserOfRoom(room.getId(), gameEndMessage);
    }

    @Override
    public Achievement getAchievementByUserId(String userId) {
        AchievementExample achievementExample = new AchievementExample();
        AchievementExample.Criteria criteria = achievementExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        return achievements.isEmpty() ? null : achievements.get(0);
    }

    /**
     * 通过userId更新某用户的战绩信息
     * @param isWinning 是否胜利
     */
    private void updateAchievement(String userId, boolean isWinning) {
        Achievement achievement = getAchievementByUserId(userId);
        if (achievement == null) {
            achievement = new Achievement(userId);
            achievementMapper.insert(achievement);
        }
        if (isWinning) {
            achievement.incrWinMatch();
        } else {
            achievement.incrFailureMatch();
        }
        achievementMapper.updateByPrimaryKeySelective(achievement);
    }
}
