package site.pushy.landlords.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.UserService;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Optional<User> getUserById(String id) {
        if (!StringUtils.hasLength(id)) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }
        return Optional.ofNullable(userMapper.selectByPrimaryKey(id));
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateByPrimaryKey(user) == 1;
    }
}
