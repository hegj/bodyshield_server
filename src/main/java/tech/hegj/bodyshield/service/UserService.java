package tech.hegj.bodyshield.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import tech.hegj.bodyshield.model.User;

/**
 * 
 * @author hegj
 *
 */
@Service
public interface UserService {
	public ModelMap register(User user, ModelMap modelMap) throws Exception;
	public ModelMap checkName(String name, ModelMap modelMap) throws Exception;
	public ModelMap login(String name, String password, HttpServletRequest request, ModelMap modelMap) throws Exception;
	public ModelMap thirdLogin(String openid, String name, int thirdLoginType, String headImg, ModelMap modelMap) throws Exception;
	public ModelMap feedback(String contact, String content, int uid, ModelMap modelMap) throws Exception;
	public ModelMap updateHeadImg(String headImg, int uid,HttpServletRequest request, ModelMap modelMap) throws Exception;
}
