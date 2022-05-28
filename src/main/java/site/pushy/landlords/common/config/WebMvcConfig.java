package site.pushy.landlords.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.pushy.landlords.common.interceptor.LoginInterceptor;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry
                .addInterceptor(loginInterceptor)
                .addPathPatterns("/**");

        interceptorRegistration.excludePathPatterns("/401");
        interceptorRegistration.excludePathPatterns("/auth/check");
        interceptorRegistration.excludePathPatterns("/login");
        interceptorRegistration.excludePathPatterns("/qqLogin");
        interceptorRegistration.excludePathPatterns("/qqLogin/callback");
        interceptorRegistration.excludePathPatterns("/ws-test.html");
        interceptorRegistration.excludePathPatterns("/actuator/prometheus");
    }

    /**
     * 跨域设置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH");
    }
}
