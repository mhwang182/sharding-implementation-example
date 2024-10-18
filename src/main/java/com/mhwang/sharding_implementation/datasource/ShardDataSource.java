package com.mhwang.sharding_implementation.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.Map;


public class ShardDataSource extends AbstractRoutingDataSource {

    public ShardDataSource(Map<Object, Object> dataSourceMap, Object defaultDataSource) {
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(defaultDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ShardContext.getCurrentShard();
    }

}
