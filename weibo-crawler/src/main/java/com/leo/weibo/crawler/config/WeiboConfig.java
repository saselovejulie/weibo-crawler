package com.leo.weibo.crawler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

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
    List<String> userAgent;
    private String userRelationQueue;
    private String userRelationResultQueue;

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

    public List<String> getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(List<String> userAgent) {
        this.userAgent = userAgent;
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
}
