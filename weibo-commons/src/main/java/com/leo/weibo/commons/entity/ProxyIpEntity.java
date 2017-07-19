/******************************************************************
** 类    名：ProxyIpEntity
** 描    述：
** 创 建 者：bianj
** 创建时间：2017-03-03 16:45:58
******************************************************************/

package com.leo.weibo.commons.entity;

import com.aliyun.mns.model.Message;
import com.leo.weibo.commons.vo.AliQueueSupport;

import java.beans.Transient;

/**
 * (w_proxy_ip)
 * 
 * @author bianj
 * @version 1.0.0 2017-03-03
 */
public class ProxyIpEntity implements java.io.Serializable,AliQueueSupport {
    /** 版本号 */
    private static final long serialVersionUID = 8013565110641403967L;
    
    /**  */
    private String id;
    
    /** ip:port */
    private String ip;
    
    /**  */
    private String uname;
    
    /**  */
    private String pwd;
    
    /** 是否需要密码认证 */
    private Boolean authentication;

    private transient Message message;
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getId() {
        return this.id;
    }
     
    /**
     * 设置
     * 
     * @param id
     *          
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * 获取ip:port
     * 
     * @return ip
     */
    public String getIp() {
        return this.ip;
    }
     
    /**
     * 设置ip:port
     * 
     * @param ip
     *          ip:port
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getUname() {
        return this.uname;
    }
     
    /**
     * 设置
     * 
     * @param uname
     *          
     */
    public void setUname(String uname) {
        this.uname = uname;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getPwd() {
        return this.pwd;
    }
     
    /**
     * 设置
     * 
     * @param pwd
     *          
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    /**
     * 获取是否需要密码认证
     * 
     * @return 是否需要密码认证
     */
    public Boolean getAuthentication() {
        return this.authentication;
    }
     
    /**
     * 设置是否需要密码认证
     * 
     * @param authentication
     *          是否需要密码认证
     */
    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    @Transient
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}