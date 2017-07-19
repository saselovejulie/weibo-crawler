package com.leo.weibo.script.dao.base;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Leo on 2017/3/3.
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
        initParams={
                @WebInitParam(name="allow",value="127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
                // @WebInitParam(name="deny",value="192.168.16.111"),IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name="loginUsername",value="leo"),// 用户名
                @WebInitParam(name="loginPassword",value="8373616"),// 密码
                @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidStatViewServlet {

}
