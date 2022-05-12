package site.pushy.landlords.common.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.pushy.landlords.common.util.JWTUtil;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
        // 返回false则不执行拦截
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("curUser") == null) {
            String token = httpServletRequest.getHeader("token");
            // [object Object] 是为了防止前端axios配置的本地无Token提交的情况
            if (token == null || token.equals("[object Object]")) {
                writeError(response);
                return false;
            }
            try {
                String id = JWTUtil.decode(token);
                User user = userService.getUserById(id);
                session.setAttribute("curUser", user);
                return true;
            } catch (Exception e) {
                writeError(response);
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

    private void writeError(HttpServletResponse response) {
        try {
            response.setStatus(401);
            PrintWriter writer = response.getWriter();
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            response.setContentType("application/json");
            writer.println(RespEntity.error(status.value(), status.getReasonPhrase()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
