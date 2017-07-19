package com.leo.weibo.commons.vo;

import com.aliyun.mns.model.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Created by Leo on 2017/2/24.
 */
public class UserVO implements CommonVOSupport {

    private UserInfo userInfo;
    private TabsInfo tabsInfo;

    private String fans_scheme;
    private String follow_scheme;
    private Integer ok;
    private String scheme;
    private Integer seeLevel;
    private Integer showAppTips;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public TabsInfo getTabsInfo() {
        return tabsInfo;
    }

    public void setTabsInfo(TabsInfo tabsInfo) {
        this.tabsInfo = tabsInfo;
    }

    public String getFans_scheme() {
        return fans_scheme;
    }

    public void setFans_scheme(String fans_scheme) {
        this.fans_scheme = fans_scheme;
    }

    public String getFollow_scheme() {
        return follow_scheme;
    }

    public void setFollow_scheme(String follow_scheme) {
        this.follow_scheme = follow_scheme;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Integer getSeeLevel() {
        return seeLevel;
    }

    public void setSeeLevel(Integer seeLevel) {
        this.seeLevel = seeLevel;
    }

    public Integer getShowAppTips() {
        return showAppTips;
    }

    public void setShowAppTips(Integer showAppTips) {
        this.showAppTips = showAppTips;
    }

    public String getProfileId() {
        Optional<Tabs> tab = tabsInfo.getTabs().stream().filter(tabs -> StringUtils.equalsIgnoreCase(tabs.getTab_type(), "profile")).findFirst();
        return tab.isPresent() ? tab.get().getContainerid() : null;
    }

    public String getWeiboId() {
        Optional<Tabs> tab = tabsInfo.getTabs().stream().filter(tabs -> StringUtils.equalsIgnoreCase(tabs.getTab_type(), "weibo")).findFirst();
        return tab.isPresent() ? tab.get().getContainerid() : null;
    }
}
