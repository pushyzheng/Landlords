package site.pushy.landlords.common.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.common.exception.UnauthorizedException;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        // 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
        // 返回false则不执行拦截
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("curUser") != null) {
            return true;
        }
        String token = httpServletRequest.getHeader("token");
        // [object Object] 是为了防止前端axios配置的本地无Token提交的情况
        if (token == null || token.equals("[object Object]")) {
            throw new BadRequestException("The token not in header");
        }
        try {
            String id = JWTUtil.decode(token);
            User user = userService.getUserById(id);
            session.setAttribute("curUser", user);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException("用户未登录");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
