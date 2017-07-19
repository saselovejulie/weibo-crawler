package com.leo.weibo.script.script;

import com.aliyun.mns.client.CloudQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.script.config.WeiboConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/3/3.
 */
@Component
public abstract class BaseScript {

    protected static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    protected CloudQueue userQueue;
    protected CloudQueue userResultQueue;
    protected CloudQueue ipQueue;
    protected CloudQueue userRelationQueue;
    protected CloudQueue userRelationResultQueue;

    @Autowired
    protected WeiboConfig weiboConfig;

    protected void initCloudQueue() {
        userQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserQueue());
        userResultQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserResultQueue());
        ipQueue = AliSDKUtils.getQueueUrl(weiboConfig.getIpQueue());
        userRelationQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserRelationQueue());
        userRelationResultQueue = AliSDKUtils.getQueueUrl(weiboConfig.getUserRelationResultQueue());
    }

}
