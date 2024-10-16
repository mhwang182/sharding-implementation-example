package com.mhwang.sharding_implementation;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Primary
public class CustomDataSource extends AbstractRoutingDataSource {

    public CustomDataSource() throws IOException {

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(1, getDataSourceOne());
        dataSourceMap.put(2, getDataSourceTwo());

        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(getDataSourceOne());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ShardContext.getCurrentShard();
    }

    public DataSource getDataSourceOne() throws IOException {

        File myFile = Paths.get("target/classes/shard1.properties").toFile();

        Properties properties = new Properties();
        properties.load(new FileInputStream(myFile));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(properties.getProperty("datasource.url"));
        dataSource.setUsername(properties.getProperty("datasource.username"));
        dataSource.setPassword(properties.getProperty("datasource.password"));
        dataSource.setDriverClassName(properties.getProperty("datasource.driver-class-name"));
        return dataSource;
    }

    public DataSource getDataSourceTwo() throws IOException {

        File myFile = Paths.get("target/classes/shard2.properties").toFile();

        Properties properties = new Properties();
        properties.load(new FileInputStream(myFile));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(properties.getProperty("datasource.url"));
        dataSource.setUsername(properties.getProperty("datasource.username"));
        dataSource.setPassword(properties.getProperty("datasource.password"));
        dataSource.setDriverClassName(properties.getProperty("datasource.driver-class-name"));
        return dataSource;
    }

}
