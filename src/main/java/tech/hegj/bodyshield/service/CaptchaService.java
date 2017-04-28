package tech.hegj.bodyshield.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 
 * @author hegj
 *
 */
@Service
public interface CaptchaService {
	public ModelMap sendByMobile(String mobile, ModelMap modelMap) throws Exception;
	public ModelMap sendByUid(int uid, ModelMap modelMap) throws Exception;
	public ModelMap sendByUserName(String userName, ModelMap modelMap) throws Exception;
}
