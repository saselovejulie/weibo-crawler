package com.leo.weibo.commons.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.beans.Transient;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Leo on 2017/3/2.
 */
public class UserDetailVO implements CommonVOSupport {

    public final static String SCHOOL = "学校";
    public final static String LOCATION = "所在地";
    public final static String LEVEL = "等级";
    public final static String CREDIT = "阳光信用";
    public final static String REGISTER = "注册时间";

    public final static String LOCATION_OID = "签到足迹";

    private Map<String, Object> cardlistInfo;
    private List<Card> cards;

    private Integer ok;

    public Map<String, Object> getCardlistInfo() {
        return cardlistInfo;
    }

    public void setCardlistInfo(Map<String, Object> cardlistInfo) {
        this.cardlistInfo = cardlistInfo;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    @Transient
    public String findLocationOid() {
        List<Card> cards = new ArrayList<>();
        getCards().stream().forEach(card -> {
            cards.addAll(card.getCard_group()
                    .stream()
                    .filter(card1 -> StringUtils.equalsIgnoreCase(card1.getDesc(), LOCATION_OID))
                    .collect(Collectors.toList()));
        });

        Optional<Card> card = cards.stream().findFirst();
        return card.isPresent() ? card.get().getActionlog().getOid() : null;
    }

    public Date getRegisterDate() throws ParseException {
        String reDate = findItemContent(REGISTER);
        if (StringUtils.isEmpty(reDate)) {
            return DateUtils.parseDate("1900-01-01", "yyyy-MM-dd");
        }
        return DateUtils.parseDate(reDate, "yyyy-MM-dd");
    }

    public String findItemContent(String itemName) {
        Optional<Card> card = findItemCard(itemName);
        return card.isPresent() ? card.get().getItem_content() : null;
    }

    public Optional<Card> findItemCard(String itemName) {

        List<Card> cards = new ArrayList<>();
        getCards().stream().forEach(card -> {
            if (card.getCard_group() != null) {
                cards.addAll(card.getCard_group()
                        .stream()
                        .filter(card1 -> StringUtils.equalsIgnoreCase(card1.getItem_name(), itemName))
                        .collect(Collectors.toList()));
            }
        });

        return cards.stream().findFirst();
    }

}
