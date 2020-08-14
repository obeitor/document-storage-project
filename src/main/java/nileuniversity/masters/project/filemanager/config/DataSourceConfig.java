package nileuniversity.masters.project.filemanager.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableJpaRepositories(
        basePackages = {
                "nileuniversity.masters.project.filemanager.**.repository",
        },
        transactionManagerRef = "tranzManager",
        entityManagerFactoryRef = "appManagerFactory"
)
public class DataSourceConfig {



    @Value("${app.db.url}")
    private String databaseUrl;

    @Value("${app.db.username}")
    private String databaseUsername;

    @Value("${app.db.password}")
    private String databasePassword;

    @Value("${app.db.driver}")
    private String databaseDriver;

    @Value("${app.db.max-active:100}")
    private Integer maxActive;

    @Value("${app.db.dialect:}")
    private String hibernateDialect;

    @Value("${app.db.hbbm2ddl:none}")
    private String hibernateHBM2DDL;

    @Value("${app.db.show_sql:false}")
    private String hibernateShowSql;


    @Value("${app.db.max-lifetime:60000}")
    private Long maxLifeTime;

    @Value("${app.db.connection.timeout:60000}")
    private Long connectionTimeOut;

    @Value("${app.db.connection-test:SELECT 1}")
    private String selectQuery;

    @Primary
    @Bean(name = "appManagerFactory")
    public LocalContainerEntityManagerFactoryBean dataEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaPropertyMap(additionalProperties());
        factoryBean.setPackagesToScan(
                "nileuniversity.masters.project.filemanager.**.models",
                "com.softobt.asgardian.control.models"
        );

        return factoryBean;
    }

    @Primary
    @Bean
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setConnectionTimeout(connectionTimeOut);
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setPassword(databasePassword);
        hikariConfig.setMaximumPoolSize(maxActive);
        hikariConfig.setDriverClassName(databaseDriver);
        hikariConfig.setConnectionTestQuery(selectQuery);

        return new HikariDataSource(hikariConfig);
    }

    private Map<String, String> additionalProperties() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", "com.softobt.jpa.helpers.impl.PhysicalNamingStrategyImpl");
        props.put("hibernate.hbm2ddl.auto", hibernateHBM2DDL);
        props.put("hibernate.show_sql", hibernateShowSql);
        props.put("hibernate.dialect", hibernateDialect);


        return props;
    }

    @Primary
    @Bean(name = "tranzManager")
    @DependsOn(value = "appManagerFactory")
    public PlatformTransactionManager cloudTransactionManager() {
        return new JpaTransactionManager(dataEntityManagerFactory().getObject());
    }

}
