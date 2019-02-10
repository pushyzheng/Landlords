package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.dao.AchievementMapper;
import site.pushy.landlords.pojo.DO.Achievement;
import site.pushy.landlords.service.AchievementService;

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
        return null;
    }

    @Override
    public void updateAchievement(String userId, boolean isWinning) {

    }
}
