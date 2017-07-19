package com.leo.weibo.commons.dto;

import com.aliyun.mns.model.Message;
import com.leo.weibo.commons.vo.AliQueueSupport;

import java.util.List;

/**
 * Created by Leo on 2017/3/5.
 */
public class UserRelationDTO implements AliQueueSupport {

    private Long userId;
    private List<Long> followIds;

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

    public List<Long> getFollowIds() {
        return followIds;
    }

    public void setFollowIds(List<Long> followIds) {
        this.followIds = followIds;
    }
}
