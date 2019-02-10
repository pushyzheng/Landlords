package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.Achievement;

/**
 * @author Pushy
 * @since 2019/2/10 19:18
 */
public interface AchievementService {

    /**
     * 获取某用户的战绩信息
     */
    Achievement getAchievementByUserId(String userId);

    /**
     * 通过userId更新某用户的战绩信息
     * @param isWinning 是否胜利
     */
    void updateAchievement(String userId, boolean isWinning);

}
