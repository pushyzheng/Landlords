package site.pushy.landlords.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.pushy.landlords.common.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
        // 返回false则不执行拦截
        HttpSession session = httpServletRequest.getSession();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if(session.getAttribute("LOGIN_USER")==null) {
            String token = httpServletRequest.getHeader("token");
            System.out.println(token+"==============================================");
            if (token==null) {
                System.out.println("laile");
                httpServletResponse.sendRedirect("/users/401");
                return false;
            }
            Integer id = null;
            try {
                id = JWTUtil.decode(token);
            } catch (Exception e) {
                httpServletResponse.sendRedirect("/users/401");
                return false;
            }
            if (id==null){
                httpServletResponse.sendRedirect("/users/401");
                return false;
            }else{
                // 认证成功
                session.setAttribute("LOGIN_USER",id);
                return true;
            }
        }else {
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
