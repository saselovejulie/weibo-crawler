package com.leo.weibo.script.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/3/1.
 */
@ConfigurationProperties(prefix = "config")
@Component
public class WeiboConfig {

    private int threadCount;
    private String userQueue;
    private String userResultQueue;
    private String ipQueue;
    private int maxRetryTime;
    private String userRelationQueue;
    private String userRelationResultQueue;

    private DataSource datasource;

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getUserQueue() {
        return userQueue;
    }

    public void setUserQueue(String userQueue) {
        this.userQueue = userQueue;
    }

    public String getUserResultQueue() {
        return userResultQueue;
    }

    public void setUserResultQueue(String userResultQueue) {
        this.userResultQueue = userResultQueue;
    }

    public String getIpQueue() {
        return ipQueue;
    }

    public void setIpQueue(String ipQueue) {
        this.ipQueue = ipQueue;
    }

    public int getMaxRetryTime() {
        return maxRetryTime;
    }

    public void setMaxRetryTime(int maxRetryTime) {
        this.maxRetryTime = maxRetryTime;
    }

    public String getUserRelationQueue() {
        return userRelationQueue;
    }

    public void setUserRelationQueue(String userRelationQueue) {
        this.userRelationQueue = userRelationQueue;
    }

    public String getUserRelationResultQueue() {
        return userRelationResultQueue;
    }

    public void setUserRelationResultQueue(String userRelationResultQueue) {
        this.userRelationResultQueue = userRelationResultQueue;
    }

    public static class DataSource {

        private String name;
        private String url;
        private String username;
        private String password;
        private String type;
        private String driverClassName;
        private Integer maxActive;
        private Integer initialSize;
        private Integer maxWait;
        private Integer minIdle;
        private String validationQuery;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Integer maxWait) {
            this.maxWait = maxWait;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }
    }

}
