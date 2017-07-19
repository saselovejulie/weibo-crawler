package com.leo.weibo.commons.dto;

import com.aliyun.mns.model.Message;
import com.leo.weibo.commons.vo.AliQueueSupport;

/**
 * Created by Leo on 2017/3/5.
 */
public class UserDTO implements AliQueueSupport {

    private Long userId;
    private String relationId;

    private transient Message message;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
