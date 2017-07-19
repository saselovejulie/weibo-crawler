package com.leo.weibo.commons.vo;

import com.aliyun.mns.model.Message;

/**
 * Created by Leo on 2017/3/10.
 */
public interface AliQueueSupport {

    void setMessage(Message message);

}
