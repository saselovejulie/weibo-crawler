package com.leo.weibo.script.script;

import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.dto.UserRelationDTO;
import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.script.dao.UserDAO;
import com.leo.weibo.script.dao.UserRelationDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/3/12.
 */
@Component
public class UserRelationSummaryThread extends BaseScript implements Runnable {

    protected static Log logger = LogFactory.getLog(UserRelationSummaryThread.class);

    @Autowired
    private UserRelationDAO userRelationDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void run() {
        initCloudQueue();
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() throws Exception {
        while (true) {
            //1. get relation from queue
            UserRelationDTO relationDTO;
            try {
                relationDTO = (UserRelationDTO)AliSDKUtils.getEntityFromQueue(userRelationResultQueue, new UserRelationDTO());
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("go on process.");
                continue;
            }

            if(null == relationDTO || relationDTO.getUserId() == null) {
                logger.error("get empty DTO from queue");
                continue;
            }

            Long userId = relationDTO.getUserId();

            //2. insert relation.
            relationDTO.getFollowIds().stream().forEach(follow -> {
                try {
                    userRelationDAO.insertEntity(userId, follow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            logger.info("finished insert user relation, start send to user Queue.");

            //3. filter the id which exist in our database then send follow id to queue
            relationDTO.getFollowIds().stream().forEach(follow -> {
//                UserEntity userEntity = userDAO.getUserEntityById(follow);
                Object obj = userDAO.getSendLogByUserId(follow);
                if(null == obj) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(follow);
                    userDAO.insertSendedUserId(follow);
                    AliSDKUtils.sendSingleMessage(userQueue, gson.toJson(userDTO));
                } else {
                    logger.info("user follow id exist :"+follow+" or already send to queue.");
                }
            });

            // delete message
            AliSDKUtils.deleteMessage(userRelationResultQueue, relationDTO.getMessage());
        }
    }
}
