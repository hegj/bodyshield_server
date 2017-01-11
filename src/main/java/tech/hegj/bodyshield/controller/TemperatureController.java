package tech.hegj.bodyshield.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;

import tech.hegj.bodyshield.model.TemperatureData;
import tech.hegj.bodyshield.service.TemperatureService;

/**
 * 
 * @author hegj
 *
 */
@RestController
@RequestMapping("/temperature")
public class TemperatureController extends BaseController {
	
	@Autowired
	TemperatureService temperatureService;
	@Value(value="/resources/HID-master.zip")
    private  Resource firmware;
	
	@RequestMapping(value = "/upload/data", method = RequestMethod.POST)
	public Object uploadHistoryData(@RequestParam("data") String data,ModelMap modelMap) {
		try{
			List<TemperatureData> list = JSONArray.parseArray(data, TemperatureData.class);
			modelMap = temperatureService.save(list, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/load/data", method = RequestMethod.POST)
	public Object login(ModelMap modelMap) {
		try{
			int uid = getInt("uid", 0);
			String deviceAddress = getString("deviceAddress", "");
			long lastTimestamp = getLong("lastTimestamp", 0L);
			modelMap = temperatureService.list(uid, deviceAddress, lastTimestamp, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/download/firmware")
	public ResponseEntity<byte[]> download() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "firmware.zip");
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(firmware.getFile()), headers, HttpStatus.CREATED);
	}
	 
}
