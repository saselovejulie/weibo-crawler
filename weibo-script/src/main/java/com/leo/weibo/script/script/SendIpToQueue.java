package com.leo.weibo.script.script;

import com.aliyun.mns.model.Message;
import com.leo.weibo.commons.entity.ProxyIpEntity;
import com.leo.weibo.commons.util.AliSDKUtils;
import com.leo.weibo.script.dao.ProxyIpDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Leo on 2017/3/3.
 */
@Component
public class SendIpToQueue extends BaseScript {

    protected static Log logger = LogFactory.getLog(SendIpToQueue.class);

    @Autowired
    private ProxyIpDAO proxyIpDAO;

    public void startProcess() {
        initCloudQueue();

        List<ProxyIpEntity> ipEntities = proxyIpDAO.findAll();
        List<String> message = ipEntities.stream().map(proxyIpEntity -> gson.toJson(proxyIpEntity)).collect(Collectors.toList());
        List<Message> tempList = new ArrayList<>(10);
        for (String s : message) {
            Message msg = new Message();
            msg.setMessageBody(s);
            tempList.add(msg);
            if (tempList.size() == 10) {
                AliSDKUtils.sendBatchMessage(ipQueue, tempList);
                tempList.clear();
                logger.info("send ip count +10");
            }
        }
        if (!tempList.isEmpty()) {
            AliSDKUtils.sendBatchMessage(ipQueue, tempList);
            tempList.clear();
        }
    }

}
