package com.leo.weibo.commons.vo;

import java.util.List;

/**
 * Created by Leo on 2017/2/26.
 */
public class FansVO implements CommonVOSupport {

    private Integer ok;
    private String msg;
    private String title;
    private Integer maxPage;

    private List<Card> cards;

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }
}
