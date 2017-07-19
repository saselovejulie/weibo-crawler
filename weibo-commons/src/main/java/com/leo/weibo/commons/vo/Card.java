package com.leo.weibo.commons.vo;

import java.util.List;

/**
 * Created by Leo on 2017/2/26.
 */
public class Card {

    private Integer card_type;
    private String scheme;
    private UserInfo user;

    //for userDetail card
    private List<Card> card_group;
    private transient ActionLogVO actionlog;
    private String item_content;
    private String item_name;
    private String desc;

    public Integer getCard_type() {
        return card_type;
    }

    public void setCard_type(Integer card_type) {
        this.card_type = card_type;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public List<Card> getCard_group() {
        return card_group;
    }

    public void setCard_group(List<Card> card_group) {
        this.card_group = card_group;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public ActionLogVO getActionlog() {
        return actionlog;
    }

    public void setActionlog(ActionLogVO actionlog) {
        this.actionlog = actionlog;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
