package tech.hegj.bodyshield.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tech.hegj.bodyshield.config.ErrorCode;
import tech.hegj.bodyshield.config.Keys;
import tech.hegj.bodyshield.model.User;
import tech.hegj.bodyshield.service.UserService;

/**
 * 
 * @author hegj
 *
 */
@RestController
@RequestMapping("/user")
@Component
public class UserController extends BaseController {
	@Value("${directory.headImg}")
	private String headImgDirectory;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Object register(ModelMap modelMap) {
		try{
			String name = getString("name", "");
			String password = getString("password", "");
			int age = getInt("age", 0);
			int sex = getInt("sex", 0);
			String mobile = getString("mobile", "");
			String email = getString("email", "");
			
			User user = new User();
			user.setName(name);
			user.setPassword(password);
			user.setMobile(mobile);
			user.setEmail(email);
			user.setSex(sex);
			user.setAge(age);
			user.setBirthday(System.currentTimeMillis());
			user.setCreateTime(System.currentTimeMillis());
			
			modelMap = userService.register(user, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}

	@RequestMapping(value = "/checkName", method = RequestMethod.POST)
	public Object checkName(@RequestParam("name") String name,ModelMap modelMap) {
		try{
			modelMap = userService.checkName(name, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(ModelMap modelMap) {
		try{
			String name = getString("name", "");
			String password = getString("password", "");
			modelMap = userService.login(name, password,request, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modify/password", method = RequestMethod.POST)
	public Object modifyPassword(ModelMap modelMap) {
		try{
			String name = getString("name", "");
			String password = getString("password", "");
			modelMap = userService.updatePassword(name, password, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value="upload/headImg", method=RequestMethod.POST)
	public Object processUpload(@RequestParam MultipartFile file, ModelMap modelMap){
		try {
			int uid = getInt("uid", 1);
			if(file != null){
//				file.transferTo(new File("C:/Qiyi/" + uid +".jpg"));
				file.transferTo(new File(headImgDirectory+uid+".jpg"));
				modelMap = userService.updateHeadImg(uid+".jpg", uid, request, modelMap);
			}else {
				modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
				modelMap.put(Keys.MESSAGE,"上传的头像文件为null");
			}
		} catch (Exception e) {
			handlerSysError(modelMap, e);
		}
		return modelMap;
	}
	
	@RequestMapping(value="/upload/form")
	public String uploadpage( Model model) throws IOException {
		return "test";
	}
	
	@RequestMapping(value = "/third/login", method = RequestMethod.POST)
	public Object thirdLogin(ModelMap modelMap) {
		try{
			int type = getInt("type", 0);
			String openid = getString("openid", "");
			String name = getString("name", "");
			String headImg = getString("headImg", "");
			modelMap = userService.thirdLogin(openid, name, type, headImg, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public Object feedback(ModelMap modelMap) {
		try{
			int uid = getInt("uid", 0);
			String contact = getString("contact", "");
			String content = getString("content", "");
			modelMap = userService.feedback(contact, content, uid, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}
}
