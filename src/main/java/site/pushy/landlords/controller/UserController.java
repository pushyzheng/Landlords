package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DO.UserExample;

/**
 * @author Fuxing
 * @since 2019年1月19日11:29:18
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;


    /**
     * 通过id获得用户
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public String getById(@PathVariable String id){
        User user = userMapper.selectByPrimaryKey(id);
        if (user!=null){
            return RespEntity.success(user);
        }else {
            return RespEntity.error(404,"未找到此用户");
        }
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody User body){
        String userId = body.getId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user!=null){
            if (user.getPassword().equals(body.getPassword())){
                String token = JWTUtil.encode(user.getId());
                return RespEntity.success(token);
            }
            return RespEntity.error(401,"账号或密码错误");
        }
        return RespEntity.error(404,"用户不存在");
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String id){
        User user = userMapper.selectByPrimaryKey(id);
        if (user==null){
            return RespEntity.error(404,"用户不存在");
        }else{
            userMapper.deleteByPrimaryKey(id);
            return RespEntity.success("删除成功");
        }
    }

    /**
     * 报错
     */
    @RequestMapping("/401")
    public String nologin(){
        return RespEntity.error(401,"请先登录");
    }
}
