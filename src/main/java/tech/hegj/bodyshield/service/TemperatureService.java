package tech.hegj.bodyshield.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import tech.hegj.bodyshield.model.TemperatureData;

/**
 * 
 * @author hegj
 *
 */
@Service
public interface TemperatureService {
	public ModelMap save(List<TemperatureData> list, ModelMap modelMap) throws Exception;
	public ModelMap list(int uid, String deviceAddress, long lastTime, ModelMap modelMap) throws Exception;
}
