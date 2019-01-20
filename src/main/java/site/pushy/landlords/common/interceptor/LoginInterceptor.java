package site.pushy.landlords.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.dao.UserMapper;
import site.pushy.landlords.pojo.DO.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
        // 返回false则不执行拦截
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("curUser") == null) {
            String token = httpServletRequest.getHeader("token");
            if (token == null) {
                httpServletResponse.sendRedirect("/401");
                return false;
            }
            try {
                String id = JWTUtil.decode(token);
                User user = userMapper.selectByPrimaryKey(id);
                logger.info(user.getUsername() + "：登录成功");
                session.setAttribute("curUser", user);
                return true;
            } catch (Exception e) {
                httpServletResponse.sendRedirect("/401");
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
