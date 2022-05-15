package site.pushy.landlords.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.pushy.landlords.common.config.MetricsRegistry;

import javax.annotation.Resource;

/**
 * 指标服务, 抽象成服务, 屏蔽底层的技术细节
 */
@Service
public class MetricsService {

    @Resource
    private MetricsRegistry registry;

    public void recordOnlineNum(int num) {
        registry.getOnlineGauge().set(num);
    }

    public void recordWebSocketEvent(String evtName) {
        if (!StringUtils.hasLength(evtName)) {
            return;
        }
        registry.getWebsocketEventCounter().labels(evtName).inc();
    }
}
