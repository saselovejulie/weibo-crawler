package com.leo.weibo.commons.vo;

import java.util.List;

/**
 * Created by Leo on 2017/2/24.
 */
public class TabsInfo {

    private List<Tabs> tabs;
    private Integer selectedTab;

    public List<Tabs> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tabs> tabs) {
        this.tabs = tabs;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }
}
