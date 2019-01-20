package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.NotFoundException;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;

/**
 * @author Fuxing
 * @since 2019年1月19日11:29:18
 */
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/me")
    public String getMyUser(@SessionAttribute User curUser) {
        return RespEntity.success(curUser);
    }

    /**
     * 通过id获得用户
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getById(@PathVariable String id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null)
            throw new NotFoundException("未找到此用户");
        return RespEntity.success(user);
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null)
            throw new NotFoundException("用户不存在");
        userMapper.deleteByPrimaryKey(id);
        return RespEntity.success("删除成功");
    }

}
