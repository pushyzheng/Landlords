package site.pushy.landlords.common.config;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Prometheus 指标注册中心
 *
 * @author Pushy
 */
@Service
@Getter
public class MetricsRegistry {

    @Resource
    private PrometheusMeterRegistry prometheusMeterRegistry;

    private Gauge onlineGauge;

    private Counter websocketEventCounter;

    @PostConstruct
    public void init() {
        onlineGauge = Gauge.build("landlords_online_number", "在线人数")
                .create()
                .register(prometheusMeterRegistry.getPrometheusRegistry());

        websocketEventCounter = Counter.build("landlords_websocket_event_counter", "WebSocket 事件计数器")
                .labelNames("name")
                .create()
                .register(prometheusMeterRegistry.getPrometheusRegistry());
    }
}
