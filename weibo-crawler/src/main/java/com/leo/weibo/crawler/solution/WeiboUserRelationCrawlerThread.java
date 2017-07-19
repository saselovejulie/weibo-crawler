package com.leo.weibo.crawler.solution;

import com.google.gson.JsonSyntaxException;
import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.dto.UserRelationDTO;
import com.leo.weibo.commons.entity.ProxyIpEntity;
import com.leo.weibo.commons.exception.RequestFailedException;
import com.leo.weibo.commons.exception.UserNotFoundException;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.commons.util.WeiboUtils;
import com.leo.weibo.commons.vo.FansVO;
import com.leo.weibo.crawler.base.CrawlerThreadBase;
import com.leo.weibo.crawler.entity.RequestParamEntity;
import com.leo.weibo.crawler.util.NetworkUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/3/12.
 */
@Component
public class WeiboUserRelationCrawlerThread extends CrawlerThreadBase {

    private static final String WEIBO_FOLLOWERS_INFO_URI = "/container/getSecond?containerid=%s_-_FOLLOWERS&jumpfrom=wapv4&tip=1&page=%d";
    private static Log logger = LogFactory.getLog(WeiboUserRelationCrawlerThread.class);

    protected String getUserFollowersRequestUrl(UserDTO userDTO, int pageIndex) {
        Assert.notNull(userDTO);
        return WEIBO_HOST+String.format(WEIBO_FOLLOWERS_INFO_URI, userDTO.getRelationId(), pageIndex);
    }

    @Override
    public void run() {
        initCloudQueue();
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
            //send user relation back to queue
//            AliSDKUtils.updateSingleMessageVisibilityTime(userRelationQueue, userDTO.getMessage(), 1);
        }
    }

    public void process() throws Exception {
        while (true) {
            ProxyIpEntity ipEntity;

            //1. get userDTO from queue
            UserDTO userDTO = (UserDTO)AliSDKUtils.getEntityFromQueue(userRelationQueue, new UserDTO());

            //2. ip & httpclient
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

            List<Long> followIds = new ArrayList<>(2000);

            //3. request for user follower
            int pageIndex = 1;
            int errorCount = 0;
            while (errorCount < weiboConfig.getMaxRetryTime()) {

                logger.info("processing userId :"+userDTO.getUserId()+" page :"+pageIndex);

                FansVO fansVO;
                paramEntity.setRequestUrl(getUserFollowersRequestUrl(userDTO, pageIndex));
                try {
                    fansVO = (FansVO) queryUrlInfo(httpClient, paramEntity, new FansVO());
                } catch (RequestFailedException | IOException | UserNotFoundException e) {
                    //change IP and retry
                    logger.error("get error : "+e.getMessage()+" will change IP and retry.");
                    AliSDKUtils.updateSingleMessageVisibilityTime(ipQueue, ipEntity.getMessage(), ERROR_HIDDEN_TIME);
                    ipEntity = (ProxyIpEntity)AliSDKUtils.getEntityFromQueue(ipQueue, new ProxyIpEntity());
                    paramEntity = getRequestParamEntityFromProxyIP(ipEntity);
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                if (fansVO == null) {
                    logger.error("error for get user relation :"+userDTO.getUserId()+" get error.");
                    Thread.sleep(20 * 1000);
                    continue;
                }

                if(fansVO.getMaxPage() == 1 && CollectionUtils.isEmpty(fansVO.getCards())) {
                    logger.error("EMPTY for get user relation :"+userDTO.getUserId()+". followIds is empty.");
                    break;
                }

                fansVO.getCards().stream().forEach(card -> {
                    if (null != card.getUser()) {
                        followIds.add(card.getUser().getId());
                    } else {
                        logger.error("get empty user for card :"+userDTO.getUserId());
                    }
                });

                if (pageIndex == fansVO.getMaxPage()) {
                    logger.info("user :"+userDTO.getUserId()+" get relation finished. total page :"+fansVO.getMaxPage());
                    break;
                }

                pageIndex++;
                Thread.sleep(5 * 1000);
            }

            logger.info("user :"+userDTO.getUserId()+" get "+followIds.size()+" follow.");
            if(CollectionUtils.isEmpty(followIds)) {
                continue;
            }

            //4. send relationship to queue
            UserRelationDTO userRelationDTO = new UserRelationDTO();
            userRelationDTO.setUserId(userDTO.getUserId());
            userRelationDTO.setFollowIds(followIds);

            AliSDKUtils.sendSingleMessage(userRelationResultQueue, gson.toJson(userRelationDTO));

            //5. delete message from user relation queue
            AliSDKUtils.deleteMessage(userRelationQueue, userDTO.getMessage());

        }
    }

}
