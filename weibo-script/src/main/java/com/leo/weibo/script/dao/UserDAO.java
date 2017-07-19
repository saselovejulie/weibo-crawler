package com.leo.weibo.script.dao;

import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.util.WeiboUtils;
import com.leo.weibo.script.dao.base.BaseJdbcSupport;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2017/3/9.
 */
@Repository
public class UserDAO extends BaseJdbcSupport<UserEntity> {

    @Override
    public String getTableName() {
        return "w_user";
    }

    public boolean insertEntity(UserEntity userEntity) {

        Assert.notNull(userEntity);

        Map<String, Object> maps = new HashMap<>();
        maps.put("id", userEntity.getId());
        maps.put("screen_name", userEntity.getScreenName());
        maps.put("profile_image_url", userEntity.getProfileImageUrl());
        maps.put("profile_url", userEntity.getProfileUrl());
        maps.put("statuses_count", userEntity.getStatusesCount());
        maps.put("verified", userEntity.getVerified());
        maps.put("verified_type", userEntity.getVerifiedType());
        maps.put("verified_type_ext", userEntity.getVerifiedTypeExt());
        maps.put("verified_reason", userEntity.getVerifiedReason());
        maps.put("description", userEntity.getDescription());
        maps.put("gender", userEntity.getGender());
        maps.put("mbtype", userEntity.getMbtype());
        maps.put("urank", userEntity.getUrank());
        maps.put("mbrank", userEntity.getMbrank());
        maps.put("followers_count", userEntity.getFollowersCount());
        maps.put("follow_count", userEntity.getFollowCount());
        maps.put("cover_image_phone", userEntity.getCoverImagePhone());
        maps.put("fans_scheme", userEntity.getFansScheme());
        maps.put("follow_scheme", userEntity.getFollowScheme());
        maps.put("relation_id", userEntity.getRelationId());
        maps.put("profile_id", userEntity.getProfileId());
        maps.put("weibo_id", userEntity.getWeiboId());
        maps.put("location", userEntity.getLocation());
        maps.put("school", userEntity.getSchool());
        maps.put("level_str", userEntity.getLevelStr());
        maps.put("credit_info", userEntity.getCreditInfo());
        maps.put("register_date", DateFormatUtils.format(userEntity.getRegisterDate(), WeiboUtils.DATE_PATTERN));
        maps.put("gmt_create", DateFormatUtils.format(new Date(), WeiboUtils.DATE_TIME_PATTERN));

        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            System.out.println(entry.getKey()+"#"+entry.getValue());
        }

        return insertWithoutAutoIncrement(maps);
    }

    public UserEntity getUserEntityById(long userId) {
        String sql = "select * from "+getTableName()+" where id = ?";
        return queryObject(sql, userId);
    }

    public Object getSendLogByUserId(long userId) {
        String sql = "select id from w_log_senduser where id = ?";
        return queryObject(sql, userId);
    }

    public List<Long> getSendedUserId() {
        String sql = "select id from w_log_senduser";
        return getJdbcTemplate().queryForList(sql, Long.class);
    }

    public List<Long> getNeedSendUserIds() {
        String sql = "select distinct follow_user_id from w_user_relation where follow_user_id not in ( select id from w_user );";
        return getJdbcTemplate().queryForList(sql, Long.class);
    }

    public void insertSendedUserId(long userId) {
        String sql = "insert into w_log_senduser (id) values (?);";
        executeUpdate(sql, userId);
    }

}
