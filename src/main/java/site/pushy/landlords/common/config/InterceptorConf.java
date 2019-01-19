package site.pushy.landlords.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import site.pushy.landlords.common.interceptor.LoginInterceptor;

@Configuration
public class InterceptorConf extends WebMvcConfigurerAdapter {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
        interceptorRegistration.excludePathPatterns("/users/401");
        interceptorRegistration.excludePathPatterns("/users/login");
    }
}
