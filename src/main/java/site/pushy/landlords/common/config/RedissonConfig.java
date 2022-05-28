package site.pushy.landlords.common.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import site.pushy.landlords.common.config.properties.RedisProperties;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class RedissonConfig {

    public static final String DEFAULT_REDIS_ADDRESS = "redis://127.0.0.1:6379";

    @Resource
    private RedisProperties properties;

    @Bean("default")
    public RedissonClient redissonClient() {
        log.info("Register redisson client: {}", properties);
        String address;
        if (!StringUtils.hasLength(properties.getHost()) && properties.getPort() == null) {
            address = DEFAULT_REDIS_ADDRESS;
        } else {
            address = String.format("redis://%s:%d", properties.getHost(), properties.getPort());
        }
        Config config = new Config();
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }
}
