package com.mhwang.sharding_implementation.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Integer.parseInt;

@Configuration
public class DataSourceConfig {

    @Value("${app.defaultShard}")
    private String defaultShard;

    @Bean
    public DataSourceContainer dataSourceContainer() {

        File shard1File = Paths.get("target/classes/shard1.properties").toFile();
        File shard2File = Paths.get("target/classes/shard2.properties").toFile();

        List<File> files = new ArrayList<>();
        files.add(shard1File);
        files.add(shard2File);

        List<DataSource> dataSources = new ArrayList<>();

        for (File file: files) {

            Properties properties = new Properties();

            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(properties.getProperty("datasource.url"));
            dataSource.setUsername(properties.getProperty("datasource.username"));
            dataSource.setPassword(properties.getProperty("datasource.password"));
            dataSource.setDriverClassName(properties.getProperty("datasource.driver-class-name"));
            dataSources.add(dataSource);
        }

        return new DataSourceContainer(dataSources);

    }

    @Bean
    @Primary
    public DataSource customDataSource(DataSourceContainer dataSourceContainer) {

        List<DataSource> dataSources = dataSourceContainer.getDataSourceList();

        int shardNum = 0;

        Map<Object, Object> dataSourceMap = new HashMap<>();

        for (DataSource dataSource: dataSources) {

            shardNum++;

            dataSourceMap.put(shardNum, dataSource);
        }

        return new ShardDataSource(dataSourceMap, dataSourceMap.get(parseInt(defaultShard)));
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(ShardDataSource actualDataSource) {
        return new DataSourceTransactionManager(actualDataSource);
    }

}
