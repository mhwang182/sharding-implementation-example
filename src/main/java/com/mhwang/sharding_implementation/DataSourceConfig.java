package com.mhwang.sharding_implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
    public DataSource customDataSource() {

        File shard1File = Paths.get("target/classes/shard1.properties").toFile();
        File shard2File = Paths.get("target/classes/shard2.properties").toFile();

        List<File> files = new ArrayList<>();
        files.add(shard1File);
        files.add(shard2File);

        int shardNum = 0;

        Map<Object, Object> dataSourceMap = new HashMap<>();

        for (File file: files) {
            shardNum++;

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

            dataSourceMap.put(shardNum, dataSource);
        }

        return new ShardDataSource(dataSourceMap, dataSourceMap.get(parseInt(defaultShard)));
    }

}
