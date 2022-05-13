package site.pushy.landlords.common.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@ConfigurationProperties(prefix = "database")
@Service
public class DatabaseProperties {

    /**
     * 数据库引擎
     *
     * @see EngineType
     */
    private EngineType engine;

    /**
     * 连接 URL
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @AllArgsConstructor
    @Getter
    public enum EngineType {

        MySQL("com.mysql.cj.jdbc.Driver"),

        SQLite("org.sqlite.JDBC");

        private final String className;
    }
}
