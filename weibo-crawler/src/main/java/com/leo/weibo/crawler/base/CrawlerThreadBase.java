package com.leo.weibo.crawler.base;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.leo.weibo.commons.entity.ProxyIpEntity;
import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.exception.RequestFailedException;
import com.leo.weibo.commons.exception.UserNotFoundException;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.commons.util.WeiboUtils;
import com.leo.weibo.commons.vo.AliQueueSupport;
import com.leo.weibo.commons.vo.CommonVOSupport;
import com.leo.weibo.commons.vo.UserDetailVO;
import com.leo.weibo.commons.vo.UserVO;
import com.leo.weibo.crawler.config.WeiboConfig;
import com.leo.weibo.crawler.entity.RequestParamEntity;
import com.leo.weibo.crawler.util.NetworkUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leo on 2017/2/28.
 */
@Component
public abstract class CrawlerThreadBase implements Runnable {

    private static Log logger = LogFactory.getLog(CrawlerThreadBase.class);
    protected static final int ERROR_HIDDEN_TIME = 600;

    protected static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 项目的运行路径
     */
    protected static final String CLASS_PATH = CrawlerThreadBase.class.getClassLoader().getResource("").getPath();
    private File localUserFile = new File(CLASS_PATH+"/"+"crawler_user_info.txt");
    private File localUserDetailFile = new File(CLASS_PATH+"/"+"crawler_user_detail_info.txt");

    @Autowired
    protected WeiboConfig weiboConfig;

    /**
     * 初始化IPQueue
     */
    protected CloudQueue ipQueue;
    protected CloudQueue userQueue;
    protected CloudQueue userResultQueue;
    protected CloudQueue userRelationQueue;
    protected CloudQueue userRelationResultQueue;


    protected String getRandomUserAgent() {
        if (CollectionUtils.isEmpty(weiboConfig.getUserAgent()))
            return NetworkUtils.DEFAULT_USER_AGENT;

        return WeiboUtils.getRandomElement(weiboConfig.getUserAgent());
    }

    protected List<Cookie> getAllCookie(CookieStore cookieStore) {
        return cookieStore.getCookies();
    }

    protected Optional<Cookie> getCookieByName(CookieStore cookieStore, String cookieName) {
        return cookieStore.getCookies()
                .stream()
                .filter(cookie -> StringUtils.equalsIgnoreCase(cookie.getName(), cookieName))
                .findFirst();
    }

    protected static final String WEIBO_HOST = "http://m.weibo.cn";
    protected static final String WEIBO_FANS_INFO_URI = "/container/getSecond?containerid=%s_-_FANS&jumpfrom=wapv4&tip=1&page=%d";
    protected static final String WEIBO_BLOG_URI = "/container/getIndex?type=uid&value=%d&containerid=%s&page=%d";

    @Retryable(value = {RequestFailedException.class, IOException.class},
            maxAttempts = 5, backoff = @Backoff(value = 5000l))
    protected CommonVOSupport queryUrlInfo(CloseableHttpClient httpClient, RequestParamEntity paramEntity, CommonVOSupport tClass) throws IOException, UserNotFoundException {
        String response = null;
        try {
            response = NetworkUtils.sendGetRequest(httpClient, paramEntity);
            response = WeiboUtils.decodeUnicode(response);
            //WeiboUtils.removeJsonIllegalChar(response)
            tClass = gson.fromJson(response, tClass.getClass());
            if (tClass.getOk() <= 0) {
                throw new RequestFailedException("Weibo result JSON OK is :"+tClass.getOk() + "json : "+response);
            }
            return tClass;
        } catch (JsonSyntaxException e) {
            logger.error("error on parser : "+response);
            if (StringUtils.containsIgnoreCase(response, "DEFAULT_ERROR")) {
                logger.error("DEFAULT_ERROR : User not find, will delete userID and continue.");
                throw new UserNotFoundException();
            } else if (StringUtils.containsIgnoreCase(e.getMessage(), "path $.tabsInfo.tabs")) {
                logger.error("Error user result json, will delete userID and continue.");
                throw new UserNotFoundException();
            }
            throw e;
        } catch (IOException e) {
            logger.error("can't query for : "+paramEntity.getRequestUrl());
            throw e;
        }
    }

    protected RequestParamEntity getRequestParamEntityFromProxyIP(ProxyIpEntity ipEntity) {
        Assert.notNull(ipEntity);
        String[] ipAndPort = StringUtils.split(ipEntity.getIp(), ":");
        Assert.isTrue(ipAndPort.length == 2);

        RequestParamEntity paramEntity = new RequestParamEntity();
        paramEntity.setAuthentication(ipEntity.getAuthentication());
        paramEntity.setViaProxy(true);
        paramEntity.setIp(ipAndPort[0]);
        paramEntity.setPort(NumberUtils.toInt(ipAndPort[1]));
        paramEntity.setUsername(ipEntity.getUname());
        paramEntity.setPassword(ipEntity.getPwd());
        paramEntity.setUserAgent(getRandomUserAgent());

        return paramEntity;
    }

    protected void writeResultToUserFile(String content, boolean appendBreak) {
        writeResultToLocalFile(localUserFile, content, appendBreak);
    }

    protected void writeResultToUserDetailFile(String content, boolean appendBreak) {
        writeResultToLocalFile(localUserDetailFile, content, appendBreak);
    }

    protected void writeResultToLocalFile(File file, String content, boolean appendBreak) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(content + (appendBreak ? "\r\n" : ""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initCloudQueue() {
        userQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserQueue());
        userResultQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserResultQueue());
        ipQueue = AliSDKUtils.getQueueUrl(weiboConfig.getIpQueue());
        userRelationQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserRelationQueue());
        userRelationResultQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserRelationResultQueue());
    }

}
