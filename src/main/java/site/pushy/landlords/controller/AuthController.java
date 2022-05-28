package site.pushy.landlords.controller;

import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.config.properties.LandlordsProperties;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.UnauthorizedException;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.ApiResponse;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DO.UserExample;
import site.pushy.landlords.pojo.DTO.LoginDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private LandlordsProperties properties;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<String> login(@Valid @RequestBody LoginDTO body, HttpServletRequest request) {
        User user = getUserByName(body.getUsername());
        if (user == null) {
            // 用户第一次登录，增加用户记录
            user = new User(body.getUsername(), body.getPassword());
            userMapper.insert(user);
        } else if (!user.getPassword().equals(body.getPassword())) {
            logger.warn("登录失败, 账号密码错误: {}", body.getUsername());
            throw new UnauthorizedException("账号或密码错误");
        }
        String token = saveSession(request, user);
        return ApiResponse.success(token);
    }

    /**
     * QQ 第三方登录
     * <p>
     * 将页面重定向到 QQ 第三方的登录页面
     */
    @GetMapping(value = "/qqLogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (Exception e) {
            logger.error("跳转 QQ 登录异常", e);
        }
    }

    /**
     * 用户授权成功之后回调的地址
     */
    @GetMapping("/qqLogin/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        try {
            AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
            String accessToken;
            if (accessTokenObj.getAccessToken().equals("")) {
                throw new BadRequestException("没有获取到QQ第三方平台的响应参数");
            }
            accessToken = accessTokenObj.getAccessToken();
            String openid = new OpenID(accessToken).getUserOpenID();
            User user = getUserByOpenId(openid);
            if (user == null) {
                /* 获取用户的QQ昵称、头像等信息 */
                UserInfoBean userInfo = new UserInfo(accessToken, openid).getUserInfo();
                logger.info("用户【" + userInfo.getNickname() + "】 通过第三方登录，openId => " + openid);
                if (userInfo == null) {
                    throw new BadRequestException("没有获取到QQ第三方平台的响应参数");
                }
                user = new User(userInfo.getNickname(), "", openid, userInfo.getAvatar().getAvatarURL100());
                user.setGender(userInfo.getGender());
                userMapper.insert(user);
            }
            String token = saveSession(request, user);
            response.sendRedirect(String.format("http://%s/#/oauth/%s", properties.getFrontendHost(), token));
        } catch (Exception e) {
            logger.error("QQ 登录回调异常", e);
            qqLogin(request, response);
        }
    }

    @GetMapping("/auth/check")
    public ApiResponse<Boolean> checkToken(@RequestParam String token) {
        boolean valid = Try.of(() -> JWTUtil.decode(token))
                .map(StringUtils::hasLength)
                .getOrElse(false);
        return ApiResponse.success(valid);
    }

    /**
     * 报错
     */
    @RequestMapping("/401")
    public String unauthorizedError() {
        throw new UnauthorizedException("请先登录");
    }

    /**
     * 保存用户登录对象到 Session，并返回客户端Token令牌
     */
    private String saveSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("curUser", user);
        return JWTUtil.encode(user.getId());
    }

    private User getUserByName(String username) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        return users.isEmpty() ? null : users.get(0);
    }

    private User getUserByOpenId(String openid) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andOpenidEqualTo(openid);
        List<User> users = userMapper.selectByExample(userExample);
        return users.isEmpty() ? null : users.get(0);
    }
}
