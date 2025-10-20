package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import test.TenantContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String defaultUsername;
    @Value("${spring.datasource.password}")
    private String defaultPassword;
    @Value("${spring.datasource.url}")
    private String defaultUrl;

    private final Map<String, DataSource> cache = new ConcurrentHashMap<>();

    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource() {
            @Override
            protected DataSource determineTargetDataSource() {
                String tenant = TenantContext.getTenant();
                if (tenant == null || tenant.equals("default")) {
                    return createDataSource(defaultUrl);
                }
                return cache.computeIfAbsent(tenant, this::createTenantDataSource);
            }

            private DataSource createTenantDataSource(String tenant) {
                String url = "jdbc:mysql://localhost:3306/" + tenant + "?useSSL=false&serverTimezone=Asia/Seoul";
                System.out.println("🔗 Create new DataSource for tenant: " + tenant);
                return createDataSource(url);
            }
        };

        // ✅ 빈 targetDataSources라도 반드시 초기화 필요
        routingDataSource.setTargetDataSources(new HashMap<>());

        // ✅ 기본 DB 설정
        routingDataSource.setDefaultTargetDataSource(createDataSource(defaultUrl));

        return routingDataSource;
    }
    // ✅ 외부에 위치한 공용 메서드
    private DataSource createDataSource(String url) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(defaultUsername);
        ds.setPassword(defaultPassword);
        return ds;
    }
}
