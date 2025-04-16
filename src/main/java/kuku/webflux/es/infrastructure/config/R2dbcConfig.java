package kuku.webflux.es.infrastructure.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
public class R2dbcConfig {

    @Value("${spring.r2dbc.driver}")
    private String driver;

    @Value("${spring.r2dbc.host}")
    private String host;

    @Value("${spring.r2dbc.port}")
    private Integer port;

    @Value("${spring.r2dbc.user}")
    private String user;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Value("${spring.r2dbc.database}")
    private String database;

    /**
     * R2DBC ConnectionFactory Bean 정의
     * docker-compose와 application.yml에 설정한 내용과 일치하도록 값을 입력하세요.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                builder()
                        .option(DRIVER, "pool")
                        .option(PROTOCOL, "postgresql")
                        .option(HOST, host)
                        .option(PORT, port)
                        .option(USER, user)
                        .option(PASSWORD, password)
                        .option(DATABASE, database)
                        .build()
        );

        ConnectionPoolConfiguration connectionPoolConfiguration
                = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(2)
                .minIdle(2)
                .maxSize(10)
                .maxLifeTime(Duration.ofSeconds(1800))
                .maxIdleTime(Duration.ofSeconds(1800))
                .build();

        return new ConnectionPool(connectionPoolConfiguration);
    }
}
