/******************************************************************
** 类    名：UserRelationEntity
** 描    述：用户关系表\r\nfans_user_id 关注 user_id
** 创 建 者：bianj
** 创建时间：2017-03-02 20:59:26
******************************************************************/

package com.leo.weibo.commons.entity;

/**
 * 用户关系表\r\nfans_user_id 关注 user_id(w_user_relation)
 * 
 * @author bianj
 * @version 1.0.0 2017-03-02
 */
public class UserRelationEntity implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = -1833260300737374932L;
    
    /** 用户ID */
    private String userId;
    
    /** 粉丝的用户ID */
    private String fansUserId;
    
    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public String getUserId() {
        return this.userId;
    }
     
    /**
     * 设置用户ID
     * 
     * @param userId
     *          用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * 获取粉丝的用户ID
     * 
     * @return 粉丝的用户ID
     */
    public String getFansUserId() {
        return this.fansUserId;
    }
     
    /**
     * 设置粉丝的用户ID
     * 
     * @param fansUserId
     *          粉丝的用户ID
     */
    public void setFansUserId(String fansUserId) {
        this.fansUserId = fansUserId;
    }
}