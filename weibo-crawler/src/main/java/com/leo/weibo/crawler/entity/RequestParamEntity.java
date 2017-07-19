package com.leo.weibo.crawler.entity;

/**
 * Created by Leo on 2017/3/2.
 */
public class RequestParamEntity {

    private String requestUrl;
    private String ip;
    private Integer port;
    private String username;
    private String password;
    private boolean authentication;
    private boolean viaProxy;
    private String userAgent;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public boolean isViaProxy() {
        return viaProxy;
    }

    public void setViaProxy(boolean viaProxy) {
        this.viaProxy = viaProxy;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }
}
