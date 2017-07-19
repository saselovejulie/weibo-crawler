package com.leo.weibo.commons.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.leo.weibo.commons.vo.Card;
import com.leo.weibo.commons.vo.UserDetailVO;
import com.leo.weibo.commons.vo.UserInfo;
import com.leo.weibo.commons.vo.UserVO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leo on 2017/3/5.
 */
public class WeiboUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获得一个[0,max)之间的随机整数。
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        return getRandom().nextInt(max);
    }

    /**
     * 获得一个[min, max]之间的随机整数
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(int min, int max) {
        return getRandom().nextInt(max-min+1) + min;
    }

    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 从list中随机取得一个元素
     * @param list
     * @return
     */
    public static <E> E getRandomElement(List<E> list){
        return list.get(getRandomInt(list.size()));
    }

    static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");
    public static String decodeUnicode(String s) {
        Matcher m = reUnicode.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
            m.appendReplacement(sb,
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    @Deprecated
    public static String decodeUnicodeOld(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();

    }

    /**
     * 转义JSON的反斜线
     * @param json
     * @return
     */
    public static String removeJsonIllegalChar(String json) {
        return json.replaceAll("\\\\", "\\\\\\\\");
    }

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Gson gson2 = new Gson();

        String s = "{\"userInfo\":{\"id\":1799151012,\"screen_name\":\"医生张宇静1981\",\"profile_image_url\":\"https:\\/\\/tva3.sinaimg.cn\\/crop.80.0.480.480.180\\/6b3cdda4jw8ejiuqo4j6oj20hs0dct8z.jpg\",\"profile_url\":\"http:\\/\\/m.weibo.cn\\/u\\/1799151012?uid=1799151012&luicode=10000011&lfid=1005051799151012\",\"statuses_count\":2632,\"verified\":true,\"verified_type\":0,\"verified_type_ext\":0,\"verified_reason\":\"  宁波市北仑区中医院   医师\",\"description\":\"人活一生，总该做点有意义的事\",\"gender\":\"m\",\"mbtype\":0,\"urank\":27,\"mbrank\":0,\"follow_me\":false,\"following\":false,\"followers_count\":1508,\"follow_count\":625,\"cover_image_phone\":\"https:\\/\\/tva1.sinaimg.cn\\/crop.0.0.640.640.640\\/6cf8d7ebjw1ehfr4xa8psj20hs0hsgpg.jpg\",\"toolbar_menus\":[{\"type\":\"profile_follow\",\"name\":\"关注\",\"pic\":\"\",\"params\":{\"uid\":1799151012}},{\"type\":\"link\",\"name\":\"聊天\",\"pic\":\"http:\\/\\/h5.sinaimg.cn\\/upload\\/2015\\/06\\/12\\/2\\/toolbar_icon_discuss_default.png\",\"params\":{\"scheme\":\"sinaweibo:\\/\\/messagelist?uid=1799151012&nick=\"},\"scheme\":\"https:\\/\\/passport.weibo.cn\\/signin\\/welcome?entry=mweibo&r=https%3A%2F%2Fm.weibo.cn%2Fcontainer%2FgetIndex%3Ftype%3Duid%26value%3D1799151012\"},{\"type\":\"link\",\"name\":\"文章\",\"pic\":\"\",\"params\":{\"scheme\":\"sinaweibo:\\/\\/cardlist?containerid=2303190002_445_1799151012_WEIBO_ARTICLE_LIST_DETAIL&count=20\"},\"scheme\":\"http:\\/\\/m.weibo.cn\\/p\\/index?containerid=2303190002_445_1799151012_WEIBO_ARTICLE_LIST_DETAIL&count=20&luicode=10000011&lfid=1005051799151012\"}]},\"fans_scheme\":\"http:\\/\\/m.weibo.cn\\/p\\/index?containerid=231051_-_fansrecomm_-_1799151012&luicode=10000011&lfid=1005051799151012\",\"follow_scheme\":\"http:\\/\\/m.weibo.cn\\/p\\/index?containerid=231051_-_followersrecomm_-_1799151012&luicode=10000011&lfid=1005051799151012\",\"tabsInfo\":{\"selectedTab\":1,\"tabs\":{\"0\":{\"title\":\"主页\",\"tab_type\":\"profile\",\"containerid\":\"2302831799151012\"},\"1\":{\"title\":\"微博\",\"tab_type\":\"weibo\",\"containerid\":\"1076031799151012\",\"url\":\"\\/index\\/my\"},\"3\":{\"title\":\"服务\",\"tab_type\":\"service\",\"containerid\":\"2305500002_1242_1799151012\"}}},\"ok\":1,\"seeLevel\":3,\"showAppTips\":0,\"scheme\":\"sinaweibo:\\/\\/userinfo?uid=1799151012&luicode=10000011&lfid=&featurecode=\"}";
        s = decodeUnicode(s);
        System.out.println("1 fi");
        System.out.println("1 fi");
        System.out.println("1 fi");
        System.out.println("1 fi");
        System.out.println(s);
//        s = WeiboUtils.removeJsonIllegalChar(s);
        System.out.println(s);


        UserVO u = gson2.fromJson(s, UserVO.class);
    }

//    public static void main(String[] args) {
//        try {
//            String c = "{\"card_type\": 10,\"user\": {\"id\": 2486841062,\"screen_name\": \"神蠢小圆\",\"profile_image_url\": \"http://tva1.sinaimg.cn/crop.0.0.800.800.180/943a2ee6jw8exp71pfjgtj20m80m8ju1.jpg\",\"profile_url\": \"sinaweibo://userinfo?uid=2486841062&luicode=10000012&lfid=1005051915050587_-_FOLLOWERS\",\"statuses_count\": 976,\"verified\": false,\"verified_type\": -1,\"description\": \"本命巡音/aimer. 96猫,蛇足,咕噜,柿姐模仿中...人生若只如初见 何事秋风悲画扇丶 /.\\ 三无腹黑控... 银发 轻微的傲娇情节才是人生...\",\"gender\": \"m\",\"mbtype\": 0,\"urank\": 16,\"mbrank\": 0,\"follow_me\": false,\"following\": false,\"followers_count\": 427,\"follow_count\": 1007,\"cover_image_phone\": \"http://ww1.sinaimg.cn/crop.0.0.640.640.640/549d0121tw1egm1kjly3jj20hs0hsq4f.jpg\",\"desc1\": null,\"desc2\": null},\"scheme\": \"sinaweibo://userinfo?uid=2486841062&luicode=10000012&lfid=1005051915050587_-_FOLLOWERS\",\"desc1\": \"本命巡音/aimer. 96猫,蛇足,咕噜,柿姐模仿中...人生若只如初见 何事秋风悲画扇丶 /.\\ 三无腹黑控... 银发 轻微的傲娇情节才是人生...\",\"desc2\": \"\"}";
//
//            System.out.println(c);
//
//            Gson gson = new Gson();
//
//            Card vo = gson.fromJson(c.replaceAll("\\\\", "\\\\\\\\"), Card.class);
//
//            System.out.println(vo.getUser().getDescription());
//
////            UserInfo u = new UserInfo();
////            u.setProfile_url("http://m.weibo.cn/u/1915050587?uid=1915050587&luicode=10000011&lfid=1005051915050587");
////            Arrays.asList(StringUtils.splitByWholeSeparator(u.getProfile_url(), "&lfid=")).forEach(t -> System.out.println(t));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
