package com.mhwang.sharding_implementation.datasource;

import javax.sql.DataSource;
import java.util.List;

public class DataSourceContainer {

    private List<DataSource> dataSourceList;

    public DataSourceContainer(List<DataSource> dataSources) {
        dataSourceList = dataSources;
    }

    public List<DataSource> getDataSourceList() {
        return dataSourceList;
    }

    public void setDataSourceList(List<DataSource> dataSourceList) {
        this.dataSourceList = dataSourceList;
    }
}
