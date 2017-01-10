package tech.hegj.bodyshield.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import tech.hegj.bodyshield.config.ErrorCode;
import tech.hegj.bodyshield.config.Keys;
import tech.hegj.bodyshield.dao.TemperatureDataDao;
import tech.hegj.bodyshield.model.TemperatureData;
import tech.hegj.bodyshield.service.TemperatureService;

/**
 * 
 * @author hegj
 *
 */
@Service
public class TemperatureServiceImpl implements TemperatureService {
	
	@Autowired
	TemperatureDataDao temperatureDataDao;

	@Override
	public ModelMap save(List<TemperatureData> list, ModelMap modelMap) throws Exception {
		if(list == null){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "数据为null");
			return modelMap;
		}
		for(TemperatureData data : list){
			temperatureDataDao.add(data);
		}
		modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
		return modelMap;
	}

	@Override
	public ModelMap list(int uid, String deviceAddress, long lastTime, ModelMap modelMap) throws Exception {
		List<TemperatureData> list = temperatureDataDao.list(uid, deviceAddress, lastTime);
		if(list == null){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "数据为null");
			return modelMap;
		}
		modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
		modelMap.put(Keys.DATA, list);
		return modelMap;
	}
}
