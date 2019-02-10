package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.dao.AchievementMapper;
import site.pushy.landlords.pojo.DO.Achievement;
import site.pushy.landlords.pojo.DO.AchievementExample;
import site.pushy.landlords.service.AchievementService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/10 19:22
 */
@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementMapper achievementMapper;

    @Override
    public Achievement getAchievementByUserId(String userId) {
        AchievementExample achievementExample = new AchievementExample();
        AchievementExample.Criteria criteria = achievementExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        return achievements.isEmpty() ? null : achievements.get(0);
    }

    @Override
    public void updateAchievement(String userId, boolean isWinning) {
        Achievement achievement = this.getAchievementByUserId(userId);
        if (isWinning==true){
            achievement.incrWinMatch();
        }else {
            achievement.decrFailureMatch();
        }
    }
}
