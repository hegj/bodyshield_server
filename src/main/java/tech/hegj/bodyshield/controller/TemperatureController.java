package tech.hegj.bodyshield.controller;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public Object feedback(ModelMap modelMap) {
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
	
//	
//	public static void main(String args[])  
//    {  
//        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
//        // 设定mail server  
//        senderImpl.setHost("smtp.163.com");  
//  
//        // 建立邮件消息  
//        SimpleMailMessage mailMessage = new SimpleMailMessage();  
//        // 设置收件人，寄件人 用数组发送多个邮件  
//        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};  
//        // mailMessage.setTo(array);  
//        mailMessage.setTo("337418794@qq.com");  
//        mailMessage.setFrom("13632772770@163.com");  
//        mailMessage.setSubject(" 测试简单文本邮件发送！ ");  
//        mailMessage.setText(" 测试我的简单邮件发送机制！！ ");  
//  
//        senderImpl.setUsername("13632772770@163.com"); // 根据自己的情况,设置username  
//        senderImpl.setPassword("2009150278"); // 根据自己的情况, 设置password  
//  
//        Properties prop = new Properties();  
//        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确  
//        prop.put("mail.smtp.timeout", "25000");  
//        senderImpl.setJavaMailProperties(prop);  
//        // 发送邮件  
//        senderImpl.send(mailMessage);  
//  
//        System.out.println(" 邮件发送成功.. ");  
//    }  
}
