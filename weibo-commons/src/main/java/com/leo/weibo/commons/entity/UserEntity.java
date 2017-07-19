/******************************************************************
** 类    名：UserEntity
** 描    述：新浪微博 用户主表
** 创 建 者：bianj
** 创建时间：2017-03-02 20:59:26
******************************************************************/

package com.leo.weibo.commons.entity;

import com.aliyun.mns.model.Message;
import com.leo.weibo.commons.vo.AliQueueSupport;

import java.beans.Transient;
import java.util.Date;

/**
 * 新浪微博 用户主表(w_user)
 *
 * @author bianj
 * @version 1.0.0 2017-03-03
 */
public class UserEntity implements java.io.Serializable,AliQueueSupport {
    /** 版本号 */
    private static final long serialVersionUID = -8125758867779245836L;

    /** 与新浪微博ID同步 */
    private Long id;

    /** 昵称 */
    private String screenName;

    /** 头像 */
    private String profileImageUrl;

    /** 信息URL */
    private String profileUrl;

    /** 微博总条数 */
    private Integer statusesCount;

    /** 是否认证 */
    private Boolean verified;

    /** 认证类型 */
    private Integer verifiedType;

    /**
     * 暂无
     */
    private Integer verifiedTypeExt;

    /**
     * 认证原因
     */
    private String verifiedReason;

    /** 个人简介 */
    private String description;

    /** m 男 f 女 */
    private String gender;

    /** 暂无 */
    private Integer mbtype;

    /** 暂无 */
    private Integer urank;

    /** 暂无 */
    private Integer mbrank;

    /** 粉丝数 */
    private Integer followersCount;

    /** 关注人数 */
    private Integer followCount;

    /** 貌似手机端头像 */
    private String coverImagePhone;

    /** 暂无 */
    private String fansScheme;

    /** 暂无 */
    private String followScheme;

    /** 用于获取用户好友关系的ID */
    private String relationId;

    /** 用于获取用户详细信息的ID */
    private String profileId;

    /** 用于获取用户全部微博的ID */
    private String weiboId;

    /** 所在地 */
    private String location;

    /** 学校名称 */
    private String school;

    /** 等级 LV XX */
    private String levelStr;

    /** 阳光信用状态 */
    private String creditInfo;

    /** 注册时间, 精确到天 */
    private Date registerDate;

    /** 第一次创建时间 */
    private Date gmtCreate;

    /** 更新时间 */
    private Date gmtModified;

    /**
     * AliQueue 的Message消息
     */
    private transient Message message;

    /**
     * 获取与新浪微博ID同步
     *
     * @return 与新浪微博ID同步
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置与新浪微博ID同步
     *
     * @param id
     *          与新浪微博ID同步
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取昵称
     *
     * @return 昵称
     */
    public String getScreenName() {
        return this.screenName;
    }

    /**
     * 设置昵称
     *
     * @param screenName
     *          昵称
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * 获取头像
     *
     * @return 头像
     */
    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    /**
     * 设置头像
     *
     * @param profileImageUrl
     *          头像
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * 获取信息URL
     *
     * @return 信息URL
     */
    public String getProfileUrl() {
        return this.profileUrl;
    }

    /**
     * 设置信息URL
     *
     * @param profileUrl
     *          信息URL
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /**
     * 获取微博总条数
     *
     * @return 微博总条数
     */
    public Integer getStatusesCount() {
        return this.statusesCount;
    }

    /**
     * 设置微博总条数
     *
     * @param statusesCount
     *          微博总条数
     */
    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    /**
     * 获取是否认证
     *
     * @return 是否认证
     */
    public Boolean getVerified() {
        return this.verified;
    }

    /**
     * 设置是否认证
     *
     * @param verified
     *          是否认证
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * 获取认证类型
     *
     * @return 认证类型
     */
    public Integer getVerifiedType() {
        return this.verifiedType;
    }

    /**
     * 设置认证类型
     *
     * @param verifiedType
     *          认证类型
     */
    public void setVerifiedType(Integer verifiedType) {
        this.verifiedType = verifiedType;
    }

    public Integer getVerifiedTypeExt() {
        return verifiedTypeExt;
    }

    public void setVerifiedTypeExt(Integer verifiedTypeExt) {
        this.verifiedTypeExt = verifiedTypeExt;
    }

    public String getVerifiedReason() {
        return verifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        this.verifiedReason = verifiedReason;
    }

    /**
     * 获取个人简介
     *
     * @return 个人简介
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置个人简介
     *
     * @param description
     *          个人简介
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取m 男 f 女
     *
     * @return m 男 f 女
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * 设置m 男 f 女
     *
     * @param gender
     *          m 男 f 女
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取暂无
     *
     * @return 暂无
     */
    public Integer getMbtype() {
        return this.mbtype;
    }

    /**
     * 设置暂无
     *
     * @param mbtype
     *          暂无
     */
    public void setMbtype(Integer mbtype) {
        this.mbtype = mbtype;
    }

    /**
     * 获取暂无
     *
     * @return 暂无
     */
    public Integer getUrank() {
        return this.urank;
    }

    /**
     * 设置暂无
     *
     * @param urank
     *          暂无
     */
    public void setUrank(Integer urank) {
        this.urank = urank;
    }

    /**
     * 获取暂无
     *
     * @return 暂无
     */
    public Integer getMbrank() {
        return this.mbrank;
    }

    /**
     * 设置暂无
     *
     * @param mbrank
     *          暂无
     */
    public void setMbrank(Integer mbrank) {
        this.mbrank = mbrank;
    }

    /**
     * 获取粉丝数
     *
     * @return 粉丝数
     */
    public Integer getFollowersCount() {
        return this.followersCount;
    }

    /**
     * 设置粉丝数
     *
     * @param followersCount
     *          粉丝数
     */
    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * 获取关注人数
     *
     * @return 关注人数
     */
    public Integer getFollowCount() {
        return this.followCount;
    }

    /**
     * 设置关注人数
     *
     * @param followCount
     *          关注人数
     */
    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    /**
     * 获取貌似手机端头像
     *
     * @return 貌似手机端头像
     */
    public String getCoverImagePhone() {
        return this.coverImagePhone;
    }

    /**
     * 设置貌似手机端头像
     *
     * @param coverImagePhone
     *          貌似手机端头像
     */
    public void setCoverImagePhone(String coverImagePhone) {
        this.coverImagePhone = coverImagePhone;
    }

    /**
     * 获取暂无
     *
     * @return 暂无
     */
    public String getFansScheme() {
        return this.fansScheme;
    }

    /**
     * 设置暂无
     *
     * @param fansScheme
     *          暂无
     */
    public void setFansScheme(String fansScheme) {
        this.fansScheme = fansScheme;
    }

    /**
     * 获取暂无
     *
     * @return 暂无
     */
    public String getFollowScheme() {
        return this.followScheme;
    }

    /**
     * 设置暂无
     *
     * @param followScheme
     *          暂无
     */
    public void setFollowScheme(String followScheme) {
        this.followScheme = followScheme;
    }

    /**
     * 获取用于获取用户好友关系的ID
     *
     * @return 用于获取用户好友关系的ID
     */
    public String getRelationId() {
        return relationId;
    }

    /**
     * 设置用于获取用户好友关系的ID
     *
     * @param relationId
     *          用于获取用户好友关系的ID
     */
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /**
     * 获取用于获取用户详细信息的ID
     *
     * @return 用于获取用户详细信息的ID
     */
    public String getProfileId() {
        return this.profileId;
    }

    /**
     * 设置用于获取用户详细信息的ID
     *
     * @param profileId
     *          用于获取用户详细信息的ID
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     * 获取用于获取用户全部微博的ID
     *
     * @return 用于获取用户全部微博的ID
     */
    public String getWeiboId() {
        return this.weiboId;
    }

    /**
     * 设置用于获取用户全部微博的ID
     *
     * @param weiboId
     *          用于获取用户全部微博的ID
     */
    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    /**
     * 获取所在地
     *
     * @return 所在地
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * 设置所在地
     *
     * @param location
     *          所在地
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取学校名称
     *
     * @return 学校名称
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * 设置学校名称
     *
     * @param school
     *          学校名称
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * 获取等级 LV XX
     *
     * @return 等级 LV XX
     */
    public String getLevelStr() {
        return this.levelStr;
    }

    /**
     * 设置等级 LV XX
     *
     * @param levelStr
     *          等级 LV XX
     */
    public void setLevelStr(String levelStr) {
        this.levelStr = levelStr;
    }

    /**
     * 获取阳光信用状态
     *
     * @return 阳光信用状态
     */
    public String getCreditInfo() {
        return this.creditInfo;
    }

    /**
     * 设置阳光信用状态
     *
     * @param creditInfo
     *          阳光信用状态
     */
    public void setCreditInfo(String creditInfo) {
        this.creditInfo = creditInfo;
    }

    /**
     * 获取注册时间, 精确到天
     *
     * @return 注册时间
     */
    public Date getRegisterDate() {
        return this.registerDate;
    }

    /**
     * 设置注册时间, 精确到天
     *
     * @param registerDate
     *          注册时间, 精确到天
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * 获取第一次创建时间
     *
     * @return 第一次创建时间
     */
    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    /**
     * 设置第一次创建时间
     *
     * @param gmtCreate
     *          第一次创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    public Date getGmtModified() {
        return this.gmtModified;
    }

    /**
     * 设置更新时间
     *
     * @param gmtModified
     *          更新时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Transient
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}