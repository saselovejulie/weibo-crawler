package com.leo.weibo.script.script.onetime;

import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.commons.vo.AliQueueSupport;
import com.leo.weibo.commons.vo.UserVO;
import com.leo.weibo.script.dao.UserDAO;
import com.leo.weibo.script.script.BaseScript;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/6/10.
 */
@Component
public class CleanUpUserQueue extends BaseScript {

    protected static Log logger = LogFactory.getLog(CleanUpUserQueue.class);

    @Autowired
    private UserDAO userDAO;

    public void process() {
        initCloudQueue();
        List<Long> userIds = userDAO.getNeedSendUserIds();
        logger.info("need send  : "+userIds.size());

        List<UserDTO> msgs = new ArrayList<>();
        int count = 0;
        for (Long userId : userIds) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userId);
            msgs.add(userDTO);
            if (msgs.size() == 10) {
                List<String> strs = new ArrayList<>();
                for (UserDTO msg : msgs) {
                    strs.add(gson.toJson(msg));
                    userDAO.insertSendedUserId(msg.getUserId());
                }
                AliSDKUtils.sendBatchMessageByString(userQueue, strs);
                count = count+10;
                logger.info("send user count :"+count);
                msgs.clear();
            }
        }

        if (msgs.size() > 0) {
            List<String> strs = new ArrayList<>();
            for (UserDTO msg : msgs) {
                strs.add(gson.toJson(msg));
                userDAO.insertSendedUserId(msg.getUserId());
            }
            AliSDKUtils.sendBatchMessageByString(userQueue, strs);
            count = count+10;
            logger.info("send user count :"+count);
        }

    }

}
