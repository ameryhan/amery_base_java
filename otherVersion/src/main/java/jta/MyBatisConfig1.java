package jta;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author ameryhan
 * @date 2019/8/21 15:06
 */


@Configuration
// basePackages 最好分开配置 如果放在同一个文件夹可能会报错
//@MapperScan(basePackages = "com.itmayiedu.test01", sqlSessionTemplateRef = "testSqlSessionTemplate")
@EnableConfigurationProperties(value = {DBConfig1.class})
public class MyBatisConfig1 {

    // 配置数据源
    @Primary
    @Bean(name = "testDataSource")
    public DataSource testDataSource(DBConfig1 testConfig) throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(testConfig.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(testConfig.getPassword());
        mysqlXaDataSource.setUser(testConfig.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("testDataSource");

        xaDataSource.setMinPoolSize(testConfig.getMinPoolSize());
        xaDataSource.setMaxPoolSize(testConfig.getMaxPoolSize());
        xaDataSource.setMaxLifetime(testConfig.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(testConfig.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(testConfig.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(testConfig.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(testConfig.getMaxIdleTime());
        xaDataSource.setTestQuery(testConfig.getTestQuery());
        return xaDataSource;
    }

//    @Primary
//    @Bean(name = "testSqlSessionFactory")
//    public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource)
//            throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        return bean.getObject();
//    }
//
//    @Primary
//    @Bean(name = "testSqlSessionTemplate")
//    public SqlSessionTemplate testSqlSessionTemplate(
//            @Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

}
