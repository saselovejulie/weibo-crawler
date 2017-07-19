/**
 *
 */
package com.leo.weibo.script.dao.base;

import com.leo.weibo.script.util.GenericsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public abstract class BaseJdbcSupport<T> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private String primaryKeyName;

    protected Class<T> entityClass;

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    public String getPrimaryKeyName() {
        if (StringUtils.isEmpty(primaryKeyName))
            primaryKeyName = "id";
        return primaryKeyName;
    }

    private void checkJdbcInsert() {
        if (simpleJdbcInsert == null)
            simpleJdbcInsert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(getTableName())
                    .usingGeneratedKeyColumns(getPrimaryKeyName());
    }

    public BaseJdbcSupport() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public abstract String getTableName();

    /**
     * @param parameters
     * @return the new record's id, -1 if some error
     */
    public long insert(Map<String, Object> parameters) {
        checkJdbcInsert();
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId == null ? -1 : newId.longValue();
    }

    public boolean insertWithoutAutoIncrement(Map<String, Object> parameters) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(getTableName());
        return simpleJdbcInsert.execute(parameters) > 0;
    }
    
    /**
     * find first object by name = value
     *
     * @param name
     * @param value
     * @return null if not found
     */
    protected final T findFirstBy(String name, Object value) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM ").append(getTableName());
        sql.append(" WHERE ").append(name).append(" = ? ");
        sql.append(" LIMIT 1");

        List result = getJdbcTemplate().query(sql.toString(),
                BeanPropertyRowMapper.newInstance(getEntityClass()), value);

        if (result != null && result.size() > 0) {
            return (T) result.get(0);
        }
        return null;
    }

    protected final T queryObject(String sql, Object... args) {
        List result = getJdbcTemplate().query(sql,
                BeanPropertyRowMapper.newInstance(getEntityClass()), args);
        if (result != null && result.size() > 0) {
            return (T) result.get(0);
        }
        return null;
    }

    protected String queryForString(String sql, Object... args) throws DataAccessException {
        String result = (String) this.getJdbcTemplate().queryForObject(sql, args, String.class);
        return (result != null ? result : null);
    }

    protected List<String> queryForStringList(String sql, Object... args) throws DataAccessException {
        List result = this.getJdbcTemplate().queryForList(sql, args, String.class);
        return (result != null ? result : null);
    }

    protected List<Object[]> queryForObjectArrayList(String sql, Object... args) throws DataAccessException {
        List result = this.getJdbcTemplate().queryForList(sql, args, Object[].class);
        return result;
    }
    
    protected List queryForMapList(String sql, Object... args) throws DataAccessException {
        List result = this.getJdbcTemplate().queryForList(sql, args);
        return result;
    }

    protected List<Integer> queryForIntegerList(String sql, Object... args) throws DataAccessException {
        List result = this.getJdbcTemplate().queryForList(sql, args, Integer.class);
        return (result != null ? result : null);
    }

    protected Integer queryForInteger(String sql, Object... args) throws DataAccessException {
        List list = this.getJdbcTemplate().queryForList(sql, args, BigInteger.class);
        if (list != null && list.size() > 0) {
            BigInteger result = (BigInteger) list.get(0);
            return (result != null ? result.intValue() : null);
        }
        return null;
    }

    protected Date queryForDate(String sql, Object... args) throws DataAccessException {
        Date result = (Date) this.getJdbcTemplate().queryForObject(sql, args, Date.class);
        return (result != null ? result : null);
    }

    protected final int executeUpdate(String sql, Object... args) {
        return getJdbcTemplate().update(sql, args);
    }

    protected List<T> queryForList(String sql, Object... args) {
        return getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(getEntityClass()),
                args);
    }

    protected int[] executeBatch(String sql, List<Object[]> batchData) {
        int[] updateCounts = getJdbcTemplate().batchUpdate(sql, batchData);
        return updateCounts;
    }
    
}
