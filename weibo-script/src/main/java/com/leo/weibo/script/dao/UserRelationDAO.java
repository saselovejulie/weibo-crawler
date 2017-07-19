package com.leo.weibo.script.dao;

import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.entity.UserRelationEntity;
import com.leo.weibo.commons.util.WeiboUtils;
import com.leo.weibo.script.dao.base.BaseJdbcSupport;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2017/3/9.
 */
@Repository
public class UserRelationDAO extends BaseJdbcSupport<UserRelationEntity> {

    @Override
    public String getTableName() {
        return "w_user_relation";
    }

    public void insertEntity(Long userId, Long followId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("follow_user_id", followId);
        insert(params);
    }

}
