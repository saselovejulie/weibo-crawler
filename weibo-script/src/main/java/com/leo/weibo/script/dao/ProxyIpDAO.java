package com.leo.weibo.script.dao;

import com.leo.weibo.commons.entity.ProxyIpEntity;
import com.leo.weibo.script.dao.base.BaseJdbcSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2017/3/3.
 */
@Repository
public class ProxyIpDAO extends BaseJdbcSupport<ProxyIpEntity> {

    @Override
    public String getTableName() {
        return "w_proxy_ip";
    }

    public List<ProxyIpEntity> findAll() {
        String sql = "select * from "+getTableName();
        return queryForList(sql);
    }

}
