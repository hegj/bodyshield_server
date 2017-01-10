package tech.hegj.bodyshield.dao;


import java.util.List;

import tech.hegj.bodyshield.model.TemperatureData;

/**
 * user
 * @author hegj
 *
 */
public interface TemperatureDataDao {
	public int add(TemperatureData data) throws Exception;
//	public List<TemperatureData> list(int uid, long lastTime) throws Exception;
	public List<TemperatureData> list(int uid, String deviceAddress, long lastTime) throws Exception;
}
