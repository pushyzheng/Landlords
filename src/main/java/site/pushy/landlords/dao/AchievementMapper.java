package site.pushy.landlords.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.pushy.landlords.pojo.DO.Achievement;
import site.pushy.landlords.pojo.DO.AchievementExample;

import java.util.List;

@Repository
public interface AchievementMapper {

    long countByExample(AchievementExample example);

    int deleteByExample(AchievementExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Achievement record);

    int insertSelective(Achievement record);

    List<Achievement> selectByExample(AchievementExample example);

    Achievement selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Achievement record, @Param("example") AchievementExample example);

    int updateByExample(@Param("record") Achievement record, @Param("example") AchievementExample example);

    int updateByPrimaryKeySelective(Achievement record);

    int updateByPrimaryKey(Achievement record);
}