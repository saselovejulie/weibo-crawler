package com.leo.weibo.crawler.service;

import com.leo.weibo.commons.util.Command;
import com.leo.weibo.crawler.Application;
import com.leo.weibo.crawler.solution.WeiboUserCrawlerThread;
import com.leo.weibo.crawler.solution.WeiboUserRelationCrawlerThread;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Leo on 2017/3/1.
 */
@Component
public class WeiboCrawlerService {

    @Autowired
    private ExecutorService weiboTaskExecutor;

    protected static Log logger = LogFactory.getLog(WeiboCrawlerService.class);

    public void startProcess(String command) {

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) weiboTaskExecutor;

        if (StringUtils.equalsIgnoreCase(Command.CRAWL_USER_INFO.toString(), command)) {
            initUserCrawler(poolExecutor);
        } else if (StringUtils.equalsIgnoreCase(Command.CRAWL_USER_RELATION.toString(), command)) {
            initUserRelationCrawler(poolExecutor);
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

        logger.info("all thread start finished.");
    }

    public void initUserCrawler(ThreadPoolExecutor poolExecutor) {
        for (int i = 0; i < poolExecutor.getMaximumPoolSize(); i++) {
            Runnable userCrawlerThread = Application.getBean("weiboUserCrawlerThread");
            weiboTaskExecutor.execute(userCrawlerThread);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initUserRelationCrawler(ThreadPoolExecutor poolExecutor) {
        for (int i = 0; i < poolExecutor.getMaximumPoolSize(); i++) {
            Runnable userRelationCrawlerThread = Application.getBean("weiboUserRelationCrawlerThread");
            weiboTaskExecutor.execute(userRelationCrawlerThread);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
