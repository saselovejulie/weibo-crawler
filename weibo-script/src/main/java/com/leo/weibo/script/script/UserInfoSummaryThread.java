package com.leo.weibo.script.script;

import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.script.dao.UserDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2017/3/11.
 */
@Component
public class UserInfoSummaryThread extends BaseScript implements Runnable {

    protected static Log logger = LogFactory.getLog(UserInfoSummaryThread.class);

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

            UserEntity userEntity;
            //1. get Message from Queue
            try {
                userEntity = (UserEntity)AliSDKUtils.getEntityFromQueue(userResultQueue, new UserEntity());
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("go on process.");
                continue;
            }

            if (null == userEntity || userEntity.getId() == null) {
                logger.error("UserEntity is null, will continue.");
                AliSDKUtils.deleteMessage(userResultQueue, userEntity.getMessage());
                continue;
            }

            //check user if exist
            UserEntity existEntity = userDAO.getUserEntityById(userEntity.getId());
            if(null != existEntity) {
                logger.error("user :"+userEntity.getId()+" exist, will skip.");
                //delete userEntity message
                AliSDKUtils.deleteMessage(userResultQueue, userEntity.getMessage());
                continue;
            }

            //2.insert service into database
            userDAO.insertEntity(userEntity);

            //3. delete userEntity from Queue
            AliSDKUtils.deleteMessage(userResultQueue, userEntity.getMessage());

            //4. send userId to relation queue
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getId());
            userDTO.setRelationId(userEntity.getRelationId());
            AliSDKUtils.sendSingleMessage(userRelationQueue, gson.toJson(userDTO));
        }

    }

}
