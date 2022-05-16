package site.pushy.landlords.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Service;
import site.pushy.landlords.core.enums.TimeoutStrategy;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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

    /**
     * 每一出牌回合的计时器, 单位: 秒
     */
    private int maxSecondsForEveryRound = 30;

    /**
     * 心跳检测超时时间, 默认单位: 秒
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration heartbeatTimeout = Duration.ofMinutes(3);

    /**
     * 聊天发送的最大频率, 默认单位: 秒
     */
    private Double permitsPerSecondOfChat = 0.5;

    /**
     * 玩家在超时没有出牌后, 处理的策略
     *
     * @see TimeoutStrategy
     */
    private TimeoutStrategy timeoutStrategy;
}
