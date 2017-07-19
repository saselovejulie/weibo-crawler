package com.leo.weibo.commons.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;
import com.google.gson.Gson;
import com.leo.weibo.commons.dto.UserDTO;
import com.leo.weibo.commons.entity.UserEntity;
import com.leo.weibo.commons.vo.AliQueueSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Leo on 2017/3/3.
 */
public class AliSDKUtils {

    private static Log logger = LogFactory.getLog(AliSDKUtils.class);

    public static final int POLLING_WAIT_SECONDS = 15;
    public static final Long MAX_MESSAGE_SIZE = 65536L;
    public static final Long VISIBIILIY_TIMEOUT = 600L;
    private static final int maxTry = 5;

    private static final int MAX_PAGE_SIZE = 16;


    /**
     * 获取放置在用户HOME下的配置文件的信息
     */
    private static CloudAccount account = new CloudAccount(
            ServiceSettings.getMNSAccessKeyId(),
            ServiceSettings.getMNSAccessKeySecret(),
            ServiceSettings.getMNSAccountEndpoint());

    /**
     * 建立client连接
     */
    private static MNSClient client = account.getMNSClient();

    public static CloudQueue createQueue(String queueName) {
        QueueMeta meta1 = new QueueMeta();
        meta1.setQueueName(queueName);
        meta1.setPollingWaitSeconds(POLLING_WAIT_SECONDS);
        meta1.setMaxMessageSize(MAX_MESSAGE_SIZE);
        meta1.setVisibilityTimeout(VISIBIILIY_TIMEOUT);
        return client.createQueue(meta1);
    }

    public static CloudQueue getQueueUrl(String queueName) {
        return client.getQueueRef(queueName);
    }

    public static void deleteMessage(CloudQueue queue, Message message) {
        String receiptHandle = message.getReceiptHandle();
        queue.deleteMessage(receiptHandle);
    }

    public static Message getSingleMessage(CloudQueue queue) {
        return queue.popMessage();
    }

    /**
     * 发送单条消息
     * @param queue
     * @param messageStr
     * @return
     */
    public static Message sendSingleMessage(CloudQueue queue, String messageStr) {
        Message message = new Message();
        message.setMessageBody(messageStr);
        return queue.putMessage(message);
    }

    /**
     * 更新消息可见时间
     * @param queue
     * @param message
     * @param visibilityTimeout
     * @return
     */
    public static boolean updateSingleMessageVisibilityTime(CloudQueue queue, Message message, int visibilityTimeout) {
        String receiptHandle = message.getReceiptHandle();
        String rh = queue.changeMessageVisibilityTimeout(receiptHandle, visibilityTimeout);
        //TODO 需要检查是否成功
        return true;
    }

    /**
     * 批量获取消息
     * 注意!! size不能大于16!
     * @param queue
     * @param size
     * @return
     */
    public static List<Message> getBatchMessage(CloudQueue queue, int size) {
        size = size > 16 ? 16 : size;
        return queue.batchPopMessage(size);
    }

    /**
     * 批量删除消息,接受Message作为消息
     * @param queue
     * @param messages
     */
    public static void deleteBatchMessage(CloudQueue queue, List<Message> messages) {
        List<String> receiptHandles = messages.stream().map(message -> message.getReceiptHandle()).collect(Collectors.toList());
        deleteBatchMessageByReceiptHandle(queue, receiptHandles);
    }

    /**
     * 批量删除消息, 接受ReceiptHandle集合作为参数
     * @param queue
     * @param receiptHandles
     */
    public static void deleteBatchMessageByReceiptHandle(CloudQueue queue, List<String> receiptHandles) {
        queue.batchDeleteMessage(receiptHandles);
    }

    /**
     * 批量发送消息, 接受String作为MessageBody
     * @param queue
     * @param messages
     * @return
     */
    public static List<Message> sendBatchMessageByString(CloudQueue queue, List<String> messages) {
        List<Message> messageList = new ArrayList<>();
        for (String message : messages) {
            Message msg = new Message();
            msg.setMessageBody(message);
            messageList.add(msg);
        }

        return sendBatchMessage(queue, messageList);
    }

    /**
     * 批量发送消息, 接受封装好的Message作为参数
     * @param queue
     * @param messages
     * @return
     */
    public static List<Message> sendBatchMessage(CloudQueue queue, List<Message> messages) {
        return queue.batchPutMessage(messages);
    }

    public static AliQueueSupport getEntityFromQueue(CloudQueue queue, AliQueueSupport tClass) {
        Gson gson = new Gson();
        int tryCount = 0;
        while (true) {
            Message message = AliSDKUtils.getSingleMessage(queue);
            if (null != message) {
                String body = message.getMessageBody();
                try {
                    tClass = gson.fromJson(body, tClass.getClass());
                    tClass.setMessage(message);
                    return tClass;
                } catch (Exception e) {
                    e.printStackTrace();
                    // delete error message
                    AliSDKUtils.deleteMessage(queue, message);
                }
            }
            tryCount++;
            logger.info("Can't get Message from Queue : "+queue.getQueueURL()+", will retry: "+tryCount);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        throw new Exception("Can't get Message from Queue : "+queue.getQueueURL()+", after "+maxTry+" times. will skip");
    }

    public static void closeClient() {
        client.close();
    }

    public static void main(String[] args) {
        try {
            CloudQueue queue = AliSDKUtils.getQueueUrl("WEIBO-CLOUD-USER-QUEUE");
            UserDTO a =  (UserDTO)AliSDKUtils.getEntityFromQueue(queue, new UserDTO());
            System.out.println(a.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
