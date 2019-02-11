package site.pushy.landlords.service;

import site.pushy.landlords.pojo.DO.Achievement;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.RoundResult;

/**
 * @author Fuxing
 * @since 2019/2/10 19:18
 */
public interface AchievementService {

    /**
     * 计算得分
     */
    void countScore(User curUser, RoundResult result);

    /**
     * 获取某用户的战绩信息
     */
    Achievement getAchievementByUserId(String userId);

}
