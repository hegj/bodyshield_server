package tech.hegj.bodyshield.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tech.hegj.bodyshield.dao.BaseDao;
import tech.hegj.bodyshield.dao.TemperatureDataDao;
import tech.hegj.bodyshield.model.TemperatureData;

@Repository
public class TempertureDataDaoImpl extends BaseDao implements TemperatureDataDao{
	private RowMapper<TemperatureData> rowMapper = new RowMapper<TemperatureData>() {
		@Override
		public TemperatureData mapRow(ResultSet rs, int arg1) throws SQLException {
			TemperatureData entity = new TemperatureData();
			entity.setId(rs.getInt("id"));
			entity.setUid(rs.getInt("uid"));
			entity.setDeviceAddress(rs.getString("device_address"));
			entity.setValue(rs.getDouble("value"));
			entity.setTimestamp(rs.getLong("timestamp"));
			return entity;
		}
	};

	@Override
	public int add(TemperatureData data) throws Exception {
		StringBuilder sql = new StringBuilder("insert into temperature_data(`uid`, `device_address`, `value`, `timestamp`) ");
		sql.append("values(?,?,?,?) ");
		Object[] params = {data.getUid(),data.getDeviceAddress(),data.getValue(),data.getTimestamp()};
		return insert(sql.toString(), params);
	}

//	@Override
//	public List<TemperatureData> list(int uid, long lastTime) throws Exception {
//		StringBuilder sql = new StringBuilder("select * from temperature_data ");
//		sql.append("where uid=? ");
//		sql.append("and time>? ");
//		sql.append("order by timestamp asc ");
//		Object[] params = {uid,lastTime};
//		return jdbcTemplate.query(sql.toString(), params, rowMapper);
//	}

	@Override
	public List<TemperatureData> list(int uid, String deviceAddress, long lastTime) throws Exception {
		List<Object> params = new ArrayList<>();
		params.add(uid);
		params.add(lastTime);
		
		StringBuilder sql = new StringBuilder("select * from temperature_data ");
		sql.append("where uid=? ");
		sql.append("and timestamp>? ");
		if(StringUtils.isNotBlank(deviceAddress)){
			sql.append("and device_address=? ");
			params.add(deviceAddress);
		}
		sql.append("order by timestamp asc ");
		return jdbcTemplate.query(sql.toString(), params.toArray(), rowMapper);
	}


}