package site.pushy.landlords.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.PageFans;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.PageFansBean;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.javabeans.weibo.Company;
import com.qq.connect.oauth.Oauth;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
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


    /**
     * qq登录
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/qqLogin",method = RequestMethod.GET)
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));//将页面重定向到qq第三方的登录页面
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
    }


    //获取登录者的基础信息
    @RequestMapping(value = "/afterlogin.do",method = RequestMethod.GET)
    public void QQAfterlogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("AfterLogin=======================================================");
        response.setContentType("text/html; charset=utf-8");  // 响应编码
        PrintWriter out = response.getWriter();

        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();//code
            System.out.println(parameterName+":"+request.getParameter(parameterName));//state
        }
        System.out.println("qq_connect_state:"+request.getSession().getAttribute("qq_connect_state"));

        try {
            // 获取AccessToken(AccessToken用于获取OppendID)
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

            System.out.println("accessTokenObj:"+accessTokenObj);
            // 用于接收AccessToken
            String accessToken   = null,
                    openID        = null;
            long tokenExpireIn = 0L; // AccessToken有效时长

            if (accessTokenObj.getAccessToken().equals("")) {
                //                我们的网站被CSRF攻击了或者用户取消了授权
                //                做一些数据统计工作
                System.out.print("没有获取到响应参数");
            } else {
                accessToken = accessTokenObj.getAccessToken();  // 获取AccessToken
                tokenExpireIn = accessTokenObj.getExpireIn();

                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj =  new OpenID(accessToken);
                // 通过对象获取[OpendId]（OpendID用于获取QQ登录用户的信息）
                openID = openIDObj.getUserOpenID();

                out.println("欢迎你，代号为 " + openID + " 的用户!");
                request.getSession().setAttribute("demo_openid", openID);
                // 利用获取到的accessToken 去获取当前用户的openid --------- end

                out.println("<p> start -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- start </p>");
                // 通过OpenID获取QQ用户登录信息对象(Oppen_ID代表着QQ用户的唯一标识)
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                // 获取用户信息对象(只获取nickename与Gender)
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                out.println("<br/>");
                if (userInfoBean.getRet() == 0) {
                    out.println(userInfoBean.getNickname() + "<br/>");
                    out.println(userInfoBean.getGender() + "<br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
                } else {
                    out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                }
                out.println("<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- end </p>");
            }
        } catch (QQConnectException e) {
        }
    }

}
