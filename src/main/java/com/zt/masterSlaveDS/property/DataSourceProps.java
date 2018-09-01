package com.zt.masterSlaveDS.property;


import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.Map;


/**
 * @Author: zhoutian
 * @Description: 多数据源配置属性
 * @Date: 2018/7/10
 */
@ConfigurationProperties(prefix = "zt.datasource")
public class DataSourceProps {
    /**
     * 数据源类型
     */
    private Class<? extends DataSource> type;
    /**
     * jdbc驱动加载类
     */
    private String driverClassName;
    /**
     * *mapper.xml扫描路径
     */
    private String mapperLocations="classpath:mapping/*.xml";
    /**
     * dao层扫描路径
     */
    private String basePackages;
    private Integer initialSize=20;
    private Integer maxActive=40;
    private Integer minIdle=5;
    private boolean defaultAutoCommit=true;
    private boolean testOnBorrow=true;
    private String validationQuery="SELECT 1";
    private String url;
    private String username;
    private String password;
    /**
     * 是否开启主从数据源
     */
    private boolean masterSlave;
    private Map<String,MyDataSource> slaves;
    private String txAopExpression;


    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMasterSlave() {
        return masterSlave;
    }

    public void setMasterSlave(boolean masterSlave) {
        this.masterSlave = masterSlave;
    }

    public Map<String, MyDataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(Map<String, MyDataSource> slaves) {
        this.slaves = slaves;
    }

    public String getTxAopExpression() {
        return txAopExpression;
    }

    public void setTxAopExpression(String txAopExpression) {
        this.txAopExpression = txAopExpression;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DataSourceProps{");
        sb.append("type=").append(type);
        sb.append(", driverClassName='").append(driverClassName).append('\'');
        sb.append(", mapperLocations='").append(mapperLocations).append('\'');
        sb.append(", basePackages='").append(basePackages).append('\'');
        sb.append(", initialSize=").append(initialSize);
        sb.append(", maxActive=").append(maxActive);
        sb.append(", minIdle=").append(minIdle);
        sb.append(", defaultAutoCommit=").append(defaultAutoCommit);
        sb.append(", testOnBorrow=").append(testOnBorrow);
        sb.append(", validationQuery='").append(validationQuery).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", masterSlave=").append(masterSlave);
        sb.append(", slaves=").append(slaves);
        sb.append(", txAopExpression='").append(txAopExpression).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class MyDataSource{
        private String url;
        private String username;
        private String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("MyDataSource{");
            sb.append("url='").append(url).append('\'');
            sb.append(", username='").append(username).append('\'');
            sb.append(", password='").append(password).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
