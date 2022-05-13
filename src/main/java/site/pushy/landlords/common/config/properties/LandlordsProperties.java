package site.pushy.landlords.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@ConfigurationProperties(prefix = "landlords")
@Service
public class LandlordsProperties {

    /**
     * 前端回调地址
     */
    private String frontendHost;

    /**
     * 用于生成 jwt 的秘钥
     */
    private String jwtSecret;
}
