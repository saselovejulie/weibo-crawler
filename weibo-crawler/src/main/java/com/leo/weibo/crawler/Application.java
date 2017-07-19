package com.leo.weibo.crawler;

import com.leo.weibo.crawler.config.WeiboConfig;
import com.leo.weibo.crawler.service.WeiboCrawlerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Leo on 2017/3/1.
 */
@Configuration
@SpringBootApplication
@EnableRetry
public class Application {

    private static ApplicationContext springApplicationContext;

    @Autowired
    private WeiboConfig weiboConfig;

    public static void main(String[] args) {

        springApplicationContext = new SpringApplication(Application.class).run(args);
        Assert.isTrue(args != null && args.length >= 1 && StringUtils.trimToNull(args[0]) != null);

        WeiboCrawlerService weiboCrawlerService = Application.getBean("weiboCrawlerService");
        weiboCrawlerService.startProcess(args[0]);

        System.exit(0);
    }

    public static <T> T getBean(String beanName, Object... objects) {
        Assert.notNull(springApplicationContext);
        Assert.notNull(beanName);
        return (T) springApplicationContext.getBean(beanName, objects);
    }

    @Bean
    public ExecutorService weiboTaskExecutor() {
        ExecutorService pool = Executors.newFixedThreadPool(weiboConfig.getThreadCount());
        return pool;
    }

}
