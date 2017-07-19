package com.leo.weibo.script.service;

import com.leo.weibo.commons.util.Command;
import com.leo.weibo.script.Application;
import com.leo.weibo.script.script.SendIpToQueue;
import com.leo.weibo.script.script.UserInfoSummaryThread;
import com.leo.weibo.script.script.UserRelationSummaryThread;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Leo on 2017/3/9.
 */
@Component
public class WeiboScriptService {

    @Autowired
    private ExecutorService weiboTaskExecutor;

    protected static Log logger = LogFactory.getLog(WeiboScriptService.class);

    public void startProcess(String command) {

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) weiboTaskExecutor;

        if (StringUtils.equalsIgnoreCase(Command.SAVE_USER_RELATION_ENTITY.toString(), command)) {
            initUserRelationCrawler(poolExecutor);
        } else if (StringUtils.equalsIgnoreCase(Command.SAVE_USER_ENTITY.toString(), command)) {
            initUserCrawler(poolExecutor);
        }

        //禁止添加新的任务
        poolExecutor.shutdown();

        while(poolExecutor.getActiveCount() > 0) {
            logger.info("active thread count : "+poolExecutor.getActiveCount());
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("all thread finished.");
        System.exit(0);
    }

    public void initUserCrawler(ThreadPoolExecutor poolExecutor) {
        for (int i = 0; i < poolExecutor.getMaximumPoolSize(); i++) {
            UserInfoSummaryThread infoSummaryThread = Application.getBean("userInfoSummaryThread");
            weiboTaskExecutor.execute(infoSummaryThread);
        }
    }

    public void initUserRelationCrawler(ThreadPoolExecutor poolExecutor) {
        for (int i = 0; i < poolExecutor.getMaximumPoolSize(); i++) {
            UserRelationSummaryThread userRelationCrawlerThread = Application.getBean("userRelationSummaryThread");
            weiboTaskExecutor.execute(userRelationCrawlerThread);
        }
    }

}
