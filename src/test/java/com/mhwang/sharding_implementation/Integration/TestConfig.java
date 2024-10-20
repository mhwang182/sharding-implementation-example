package com.mhwang.sharding_implementation.Integration;

import com.mhwang.sharding_implementation.datasource.ShardDataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.*;

@TestConfiguration
@Profile({"test"})
public class TestConfig {

    @Bean
    @Primary
    public DataSource customDataSource() {

        EmbeddedDatabase dataSource1 = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("DB1")
                .addScript("schema.sql")
                .build();

        EmbeddedDatabase dataSource2 = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("DB2")
                .addScript("schema.sql")
                .build();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(1, dataSource1);
        dataSourceMap.put(2, dataSource2);

        return new ShardDataSource(dataSourceMap, dataSource1);
    }
}
