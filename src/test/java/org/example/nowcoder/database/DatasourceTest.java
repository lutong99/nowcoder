package org.example.nowcoder.database;

import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootTest
public class DatasourceTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Test
    public void testDataSource() {
        String dataSourceString = dataSource.toString();
        System.out.println("dataSource = " + dataSourceString);
    }

    @Test
    public void testDataSourceProperties() {
        System.out.println("dataSourceProperties.getPassword() = " + dataSourceProperties.getPassword());
//        System.out.println("dataSourceProperties.getPassword() = " + dataSourceProperties.getPassword());
        System.out.println("dataSourceProperties.getUsername() = " + dataSourceProperties.getUsername());
    }

    @Test
    public void testPasswordOfDatabase() {
        Configuration configuration = new Configuration();
        System.out.println("configuration = " + configuration);
    }

    @Autowired
    private Environment environment;

    @Test
    public void printDatabaseCredentials() {
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");

        System.out.println("Database Username: " + username);
        System.out.println("Database Password: " + password);
    }

}
