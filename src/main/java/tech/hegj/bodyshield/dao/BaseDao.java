package tech.hegj.bodyshield.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 数据库基类
 * @author hegj
 * @since
 */
public class BaseDao {
	@Autowired
    protected JdbcTemplate jdbcTemplate;
	
	
	public int insert(String sql, Object... params) throws SQLException{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
                new PreparedStatementCreator(){
                	@Override
                    public java.sql.PreparedStatement createPreparedStatement(Connection conn) throws SQLException{
                        java.sql.PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        setPreStateParams(ps, params);
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().intValue();
	}
	
	public int queryForInt(String sql, Object... params) throws Exception{
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql.toString(),params);
		int result = -1;
		if(rs != null && rs.next()){
			result = rs.getInt(1);
		}
		return result;
	}
	
	public String queryForString(String sql, Object... params) throws Exception{
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql.toString(),params);
		String result = "";
		if(rs != null && rs.next()){
			result = rs.getString(1);
		}
		return result;
	}
	
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... params){
		T result = null;
		try {
			result = jdbcTemplate.queryForObject(sql.toString(), rowMapper, params);
		} catch (DataAccessException e) {
			return null;
		}
		
		return result;
	}
	
	public static void setPreStateParams(PreparedStatement preStat, Object... params) throws SQLException {
		int i = 1;
		for (Object p : params) {
			if (p instanceof Integer) {
				preStat.setInt(i, (Integer) p);
			}
			if (p instanceof Short) {
				preStat.setShort(i, (Short) p);
			}
			if (p instanceof Byte) {
				preStat.setByte(i, (Byte) p);
			}
			if (p instanceof Long) {
				preStat.setLong(i, (Long) p);
			}
			if (p instanceof String) {
				preStat.setString(i, (String) p);
			}
			if (p instanceof Float) {
				preStat.setFloat(i, (Float) p);
			}
			if (p instanceof Double) {
				preStat.setDouble(i, (Double) p);
			}
			if (p instanceof Timestamp) {
				preStat.setTimestamp(i, (Timestamp) p);
			}
			if (p instanceof Date) {
				preStat.setDate(i, (Date) p);
			}
			if (p instanceof byte[]) {
				preStat.setBytes(i, (byte[]) p);
			}
			i++;
		}
	}
	
}
 