package com.leo.weibo.crawler.solution;

import com.google.gson.JsonSyntaxException;
import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.entity.ProxyIpEntity;
import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.exception.RequestFailedException;
import com.leo.weibo.commons.exception.UserNotFoundException;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.commons.vo.UserDetailVO;
import com.leo.weibo.commons.vo.UserVO;
import com.leo.weibo.crawler.base.CrawlerThreadBase;
import com.leo.weibo.crawler.entity.RequestParamEntity;
import com.leo.weibo.crawler.util.NetworkUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Leo on 2017/2/23.
 */
@Component
public class WeiboUserCrawlerThread extends CrawlerThreadBase {

    private static Log logger = LogFactory.getLog(WeiboUserCrawlerThread.class);

    private static final String WEIBO_USER_INFO_URI = "/container/getIndex?type=uid&value=%d";
    private static final String WEIBO_USER_DETAIL_INFO_URL = "/container/getIndex?containerid=%s_-_INFO&type=uid&value=%d";

    private String getUserInfoRequestUrl(UserEntity userEntity) {
        Assert.notNull(userEntity.getId());
        return WEIBO_HOST+String.format(WEIBO_USER_INFO_URI, userEntity.getId());
    }

    private String getUserDetailInfoRequestUrl(UserEntity userEntity) {
        Assert.notNull(userEntity.getId());
        Assert.notNull(userEntity.getProfileId());
        return WEIBO_HOST+String.format(WEIBO_USER_DETAIL_INFO_URL, userEntity.getProfileId(), userEntity.getId());
    }

    @Override
    public void run() {
        initCloudQueue();
        try {
            process();
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName()+"## error.");
            e.printStackTrace();
            //send userDTO back to queue
//            if (null != userEntity.getMessage()) {
//                AliSDKUtils.updateSingleMessageVisibilityTime(userQueue, userEntity.getMessage(), 1);
//            }
        }
    }

    private void process() throws Exception {

        while (true) {

            UserEntity userEntity;
            ProxyIpEntity ipEntity;

            userEntity = new UserEntity();

            //1.get IP proxy from Queue
            try {
                ipEntity = (ProxyIpEntity)AliSDKUtils.getEntityFromQueue(ipQueue, new ProxyIpEntity());
            } catch (Exception e) {
                e.printStackTrace();
                // can't get IP from Queue, return.
                break;
            }

            RequestParamEntity paramEntity = getRequestParamEntityFromProxyIP(ipEntity);
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpClient = NetworkUtils.getAutoRetryHttpClientWithCookieStore(cookieStore, paramEntity);

            //2. get userDTO from queue
            try {
                UserDTO userDTO = (UserDTO)AliSDKUtils.getEntityFromQueue(userQueue, new UserDTO());
                userEntity.setId(userDTO.getUserId());
                userEntity.setMessage(userDTO.getMessage());
            } catch (Exception e) {
                // send IP back
                AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), 1);
                e.printStackTrace();
                // can't get userDTO from Queue, return.
                break;
            }

            UserVO userVO;
            paramEntity.setRequestUrl(getUserInfoRequestUrl(userEntity));
            try {
                userVO = (UserVO) queryUrlInfo(httpClient, paramEntity, new UserVO());
            } catch (IOException | RequestFailedException | JsonSyntaxException e) {
                logger.error(e.getMessage());
                AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), ERROR_HIDDEN_TIME);
                AliSDKUtils.updateSingleMessageVisibilityTime(userQueue, userEntity.getMessage(), 1);
                continue;
            } catch (UserNotFoundException e) {
                AliSDKUtils.deleteMessage(userQueue, userEntity.getMessage());
                AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), 1);
                continue;
            }

            if (null == userVO) {
                logger.error("userVO is null for userId:"+userEntity.getId());
                continue;
            } else if (null == userVO.getUserInfo()) {
                logger.error("userInfo is null for userId:"+userEntity.getId());
                continue;
            }

            userEntity.setCoverImagePhone(userVO.getUserInfo().getCover_image_phone());
            userEntity.setDescription(userVO.getUserInfo().getDescription());

            userEntity.setFansScheme(userVO.getFans_scheme());
            userEntity.setFollowScheme(userVO.getFollow_scheme());

            userEntity.setFollowCount(userVO.getUserInfo().getFollow_count());
            userEntity.setFollowersCount(userVO.getUserInfo().getFollowers_count());

            userEntity.setGender(userVO.getUserInfo().getGender());
            userEntity.setMbrank(userVO.getUserInfo().getMbrank());
            userEntity.setMbtype(userVO.getUserInfo().getMbtype());
            userEntity.setProfileImageUrl(userVO.getUserInfo().getProfile_image_url());
            userEntity.setProfileUrl(userVO.getUserInfo().getProfile_url());
            // 获取用的微博和主页ID
            userEntity.setWeiboId(userVO.getWeiboId());
            userEntity.setProfileId(userVO.getProfileId());
            // 获取用户的关系ID
            userEntity.setRelationId(userVO.getUserInfo().getRelationId());

            userEntity.setScreenName(userVO.getUserInfo().getScreen_name());
            userEntity.setStatusesCount(userVO.getUserInfo().getStatuses_count());
            userEntity.setVerified(userVO.getUserInfo().isVerified());
            userEntity.setVerifiedType(userVO.getUserInfo().getVerified_type());
            userEntity.setVerifiedTypeExt(userVO.getUserInfo().getVerified_type_ext());
            userEntity.setVerifiedReason(userVO.getUserInfo().getVerified_reason());
            userEntity.setVerifiedType(userVO.getUserInfo().getVerified_type());
            userEntity.setUrank(userVO.getUserInfo().getUrank());

            //3. 准备获取用户详细信息
            UserDetailVO userDetailVO = queryUserDetailVO(httpClient, paramEntity, ipEntity, getUserDetailInfoRequestUrl(userEntity));
            if (null == userDetailVO) {
                logger.error("userDetailVO is null for userId:"+userEntity.getId());
                AliSDKUtils.updateSingleMessageVisibilityTime(userQueue, userEntity.getMessage(), 3600);
                continue;
            }

            userEntity.setLocation(userDetailVO.findItemContent(UserDetailVO.LOCATION));
            userEntity.setLevelStr(userDetailVO.findItemContent(UserDetailVO.LEVEL));
            try {
                userEntity.setRegisterDate(userDetailVO.getRegisterDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            userEntity.setSchool(userDetailVO.findItemContent(UserDetailVO.SCHOOL));
            userEntity.setCreditInfo(userDetailVO.findItemContent(UserDetailVO.CREDIT));

            // 创建时间
            userEntity.setGmtCreate(new Date());

            //发送到结果Queue
            sendEntityToQueue(userEntity);

            //delete UserDTO from Queue
            logger.info("query success, Delete userId :"+userEntity.getId());
            AliSDKUtils.deleteMessage(userQueue, userEntity.getMessage());

            logger.info("success for userId: "+userEntity.getId());
            AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), 300);
        }
    }

    @Retryable
    private void sendEntityToQueue(UserEntity userEntity) {
        AliSDKUtils.sendSingleMessage(userResultQueue, gson.toJson(userEntity));
    }

    private UserDetailVO queryUserDetailVO(CloseableHttpClient httpClient, RequestParamEntity paramEntity, ProxyIpEntity ipEntity, String url) {
        UserDetailVO userDetailVO;
        paramEntity.setRequestUrl(url);
        try {
            userDetailVO = (UserDetailVO) queryUrlInfo(httpClient, paramEntity, new UserDetailVO());
            return userDetailVO;
        } catch (IOException | RequestFailedException e) {
            logger.info("found request IOException or RequestFailedException, Hidden Ip and retry...");
            AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), ERROR_HIDDEN_TIME);
            ipEntity = (ProxyIpEntity) AliSDKUtils.getEntityFromQueue(ipQueue, new ProxyIpEntity());
            paramEntity = getRequestParamEntityFromProxyIP(ipEntity);
            return queryUserDetailVO(httpClient, paramEntity, ipEntity, url);
        } catch (JsonSyntaxException | UserNotFoundException e) {
            return null;
        }
    }

}
