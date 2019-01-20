package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import site.pushy.landlords.common.exception.UnauthorizedException;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DO.UserExample;
import site.pushy.landlords.pojo.DTO.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Fuxing
 * @since 2019年1月19日11:29:18
 */
@RestController
@RequestMapping(value = "", produces = "application/json")
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid @RequestBody UserDTO body, HttpServletRequest request) {
        User user = getUserByName(body.getUsername());
        if (user == null) {  // 用户第一次登录，增加用户记录
            user = new User(body.getUsername(), body.getPassword());
            userMapper.insert(user);
        } else {  // 验证密码
            if (!user.getPassword().equals(body.getPassword())) {
                throw new UnauthorizedException("账号或密码错误");
            }
        }

        /* 保存用户登录对象到 Session，并返回客户端Token令牌 */
        HttpSession session = request.getSession();
        session.setAttribute("curUser", user);
        String token = JWTUtil.encode(user.getId());
        return RespEntity.success(token);
    }

    public User getUserByName(String username) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * 报错
     */
    @RequestMapping("/401")
    public String unauthorizedError() {
        throw new UnauthorizedException("请先登录");
    }

}
