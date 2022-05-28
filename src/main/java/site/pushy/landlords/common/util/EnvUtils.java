package site.pushy.landlords.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class EnvUtils implements ApplicationContextAware {

    private static String activeProfile;

    public static boolean isNotTest() {
        return !isTest();
    }

    public static boolean isTest() {
        return "test".equals(activeProfile);
    }

    public static boolean isDev() {
        return "dev".equals(activeProfile);
    }

    public static boolean isProd() {
        return "prod".equals(activeProfile);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        activeProfile = ctx.getEnvironment().getProperty("spring.profiles.active");
    }
}
