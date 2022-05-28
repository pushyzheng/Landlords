package site.pushy.landlords.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private String host;

    private Integer port;
}
