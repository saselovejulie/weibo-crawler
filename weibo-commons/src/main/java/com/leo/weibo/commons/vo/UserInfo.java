package com.leo.weibo.commons.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * 映射WEIBO返回的JSON
 * Created by Leo on 2017/2/24.
 */
public class UserInfo {

    private Long id;
    private String cover_image_phone;//头像
    private String description;
    private Integer follow_count;
    private Integer followers_count;
    private String gender;
    private Integer mbrank;
    private Integer mbtype;
    private String profile_image_url;
    private String profile_url;
    private String screen_name;//昵称
    private Integer statuses_count;//微博条数
    private Integer urank;

    //微博认证以及认证详细
    private boolean verified;
    private Integer verified_type;
    private Integer verified_type_ext;
    private String verified_reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public void setCover_image_phone(String cover_image_phone) {
        this.cover_image_phone = cover_image_phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(Integer follow_count) {
        this.follow_count = follow_count;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getMbrank() {
        return mbrank;
    }

    public void setMbrank(Integer mbrank) {
        this.mbrank = mbrank;
    }

    public Integer getMbtype() {
        return mbtype;
    }

    public void setMbtype(Integer mbtype) {
        this.mbtype = mbtype;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public Integer getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(Integer statuses_count) {
        this.statuses_count = statuses_count;
    }

    public Integer getUrank() {
        return urank;
    }

    public void setUrank(Integer urank) {
        this.urank = urank;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Integer getVerified_type() {
        return verified_type;
    }

    public void setVerified_type(Integer verified_type) {
        this.verified_type = verified_type;
    }

    public Integer getVerified_type_ext() {
        return verified_type_ext;
    }

    public void setVerified_type_ext(Integer verified_type_ext) {
        this.verified_type_ext = verified_type_ext;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public String getRelationId() {
        try {
            return StringUtils.splitByWholeSeparator(profile_url, "&lfid=")[1];
        } catch (Exception e) {
            return null;
        }
    }

}
