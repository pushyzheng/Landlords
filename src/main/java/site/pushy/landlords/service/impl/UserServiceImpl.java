package site.pushy.landlords.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DO.UserExample;
import site.pushy.landlords.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserById(String id) {
        if (!StringUtils.hasLength(id)) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getByCondition(Condition condition) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if (StringUtils.hasLength(condition.getName())) {
            criteria.andUsernameEqualTo(condition.getName());
        }
        if (StringUtils.hasLength(condition.getOpenId())) {
            criteria.andOpenidEqualTo(condition.getOpenId());
        }
        List<User> users = userMapper.selectByExample(userExample);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public boolean save(User user) {
        return userMapper.insert(user) == 1;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateByPrimaryKey(user) == 1;
    }
}
