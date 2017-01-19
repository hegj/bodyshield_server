package tech.hegj.bodyshield.service.impl;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import tech.hegj.bodyshield.config.ErrorCode;
import tech.hegj.bodyshield.config.Keys;
import tech.hegj.bodyshield.dao.UserDao;
import tech.hegj.bodyshield.model.SexEnum;
import tech.hegj.bodyshield.model.ThirdLoginTypeEnum;
import tech.hegj.bodyshield.model.User;
import tech.hegj.bodyshield.service.UserService;

/**
 * 
 * @author hegj
 *
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;
	
	@Override
	public ModelMap register(User user, ModelMap modelMap) throws Exception {
		User oldUser = userDao.getByName(user.getName());
		if(oldUser != null){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "该用户已被注册");
			return modelMap;
		}
		if(StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getPassword())){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "该把资料填写完整");
			return modelMap;
		}
		int id = userDao.add(user);
		if(id > 0){
			user.setId(id);
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			modelMap.put(Keys.DATA, user);
		}else{
			modelMap.put(Keys.RETURN_CODE,ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "注册失败");
		}
		
		return modelMap;
	}

	@Override
	public ModelMap checkName(String name, ModelMap modelMap) throws Exception {
		User user = userDao.getByName(name);
		if(user == null){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
		}else{
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "该用户名已被注册");
		}
		return modelMap;
	}

	@Override
	public ModelMap login(String name, String password, HttpServletRequest request, ModelMap modelMap) throws Exception {
		User user = userDao.login(name, password);
		if(user != null){
			if(StringUtils.isNotBlank(user.getHeadImg()) && user.getHeadImg().indexOf("http://") == -1){
				String url=request.getScheme()+"://" + request.getServerName() //服务器地址    
		        + ":"     
		        + request.getServerPort()+request.getContextPath();
				user.setHeadImg(url+"/imgs/headImg/"+user.getHeadImg());
			}
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			modelMap.put(Keys.DATA, user);
		}else {
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "用户名或密码错误");
		}
		return modelMap;
	}
	
	
	public ModelMap thirdLogin(String openid, String name, int thirdLoginType, String headImg, ModelMap modelMap) throws Exception {
		if(StringUtils.isBlank(openid)){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "openid不能为null");
			return modelMap;
		}
		User user = userDao.getByOpenid(openid, thirdLoginType);
		if(user != null){
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			modelMap.put(Keys.DATA, user);
		}else {
			user = new User();
			user.setName(name);
			user.setPassword("xxxxxx");
			user.setMobile("");
			user.setEmail("");
			user.setSex(SexEnum.SECRECY.getId());
			user.setAge(0);
			user.setBirthday(System.currentTimeMillis());
			user.setCreateTime(System.currentTimeMillis());
			user.setHeadImg(headImg);
			if(thirdLoginType == ThirdLoginTypeEnum.WX.getId()){
				user.setWxOpenid(openid);
			}else if (thirdLoginType == ThirdLoginTypeEnum.QQ.getId()) {
				user.setQqOpenid(openid);
			}else if (thirdLoginType == ThirdLoginTypeEnum.FACEBOOK.getId()) {
				user.setFacebookOpenid(openid);
			}else {
				modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
				modelMap.put(Keys.MESSAGE, "登录类型出错");
				return modelMap;
			}
			if(userDao.add(user) > 0){
				modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			}else {
				modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
				modelMap.put(Keys.MESSAGE, "注册失败");
			}
		}
		return modelMap;
	}
	
	public ModelMap feedback(String subject, String content, int uid, ModelMap modelMap) throws Exception {
		sendMail(subject, content);
		modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
		return modelMap;
	}
	
	private void sendMail(String subject, String text) throws Exception{
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
        // 设定mail server  
        senderImpl.setHost("smtp.163.com");  
  
        // 建立邮件消息  
        SimpleMailMessage mailMessage = new SimpleMailMessage();  
        // 设置收件人，寄件人 用数组发送多个邮件  
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};  
        // mailMessage.setTo(array);  
        mailMessage.setTo("337418794@qq.com");  
        mailMessage.setFrom("13632772770@163.com");  
        mailMessage.setSubject(subject);  
        mailMessage.setText(text);  
  
        senderImpl.setUsername("13632772770@163.com"); // 根据自己的情况,设置username  
        senderImpl.setPassword("2009150278"); // 根据自己的情况, 设置password  
  
        Properties prop = new Properties();  
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确  
        prop.put("mail.smtp.timeout", "25000");  
        senderImpl.setJavaMailProperties(prop);  
        // 发送邮件  
        senderImpl.send(mailMessage); 
	}

	@Override
	public ModelMap updateHeadImg(String headImg, int uid, HttpServletRequest request, ModelMap modelMap) throws Exception {
		if(userDao.updateHeadImg(uid, headImg)>0){
			String url=request.getScheme()+"://" + request.getServerName() //服务器地址    
	        + ":"     
	        + request.getServerPort()+request.getContextPath();
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			modelMap.put(Keys.DATA, url+"/imgs/headImg/"+headImg);
		}else{
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "修改头像失败");
		}
		return modelMap;
	}
}
