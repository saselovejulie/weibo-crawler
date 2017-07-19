package com.leo.weibo.script;

import com.alibaba.druid.pool.DruidDataSource;
import com.leo.weibo.commons.util.Command;
import com.leo.weibo.script.config.WeiboConfig;
import com.leo.weibo.script.script.SendIpToQueue;
import com.leo.weibo.script.script.onetime.CleanUpUserQueue;
import com.leo.weibo.script.service.WeiboScriptService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Arrays;
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
    private static Log logger = LogFactory.getLog(Application.class);

    @Autowired
    private WeiboConfig weiboConfig;

    public static void main(String[] args) {

        springApplicationContext = new SpringApplication(Application.class).run(args);
        Assert.isTrue(args != null && args.length >= 1 && StringUtils.trimToNull(args[0]) != null);

        String command = args[0];

        if (StringUtils.equalsIgnoreCase(Command.SEND_PROXY_IP_DAILY.toString(), command)) {
            SendIpToQueue sendIpToQueue = getBean("sendIpToQueue");
            sendIpToQueue.startProcess();
        } else if (StringUtils.equalsIgnoreCase(Command.CLEANUP_USER_QUEUE.toString(), command)) {
            CleanUpUserQueue cleanUpUserQueue = getBean("cleanUpUserQueue");
            cleanUpUserQueue.process();
        } else {
            WeiboScriptService weiboScriptService = getBean("weiboScriptService");
            weiboScriptService.startProcess(command);
        }

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

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(weiboConfig.getDatasource().getUrl());
        dataSource.setUsername(weiboConfig.getDatasource().getUsername());
        dataSource.setPassword(weiboConfig.getDatasource().getPassword());
        dataSource.setInitialSize(weiboConfig.getDatasource().getInitialSize());
        dataSource.setMaxActive(weiboConfig.getDatasource().getMaxActive());
        dataSource.setMinIdle(weiboConfig.getDatasource().getMinIdle());
        dataSource.setMaxWait(weiboConfig.getDatasource().getMaxWait());
        dataSource.setValidationQuery(weiboConfig.getDatasource().getValidationQuery());
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4;"));
        return dataSource;
    }

}
