package site.pushy.landlords.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pushy.landlords.common.config.properties.DatabaseProperties;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 注册 {@link DruidDataSource} 到 Bean 容器
 *
 * @author Pushy
 */
@Configuration
public class DatasourceConfig {

    @Resource
    private DatabaseProperties properties;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName("defaultDataSource");
        datasource.setUrl(properties.getUrl());
        datasource.setUsername(properties.getUrl());
        datasource.setPassword(properties.getPassword());
        if (properties.getEngine() == null) {
            throw new IllegalArgumentException("The engine cannot be null");
        }
        datasource.setDriverClassName(properties.getEngine().getClassName());
        return datasource;
    }
}
