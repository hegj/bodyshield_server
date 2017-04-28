package tech.hegj.bodyshield.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hegj.bodyshield.service.CaptchaService;

/**
 * 
 * @author hegj
 *
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {

	@Autowired
	private CaptchaService captchaService;

	@RequestMapping(value = "/send/v1", method = RequestMethod.POST)
	public Object sendCaptcha(@RequestParam("mobile") String mobile, ModelMap modelMap){
		try{
			modelMap = captchaService.sendByMobile(mobile, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}


	@RequestMapping(value = "/sendByUserName/v1", method = RequestMethod.POST)
	public Object sendCaptchaByUserName(@RequestParam("userName") String userName, ModelMap modelMap){
		try{
			modelMap = captchaService.sendByUserName(userName, modelMap);
		}catch(Exception ex){
			handlerSysError(modelMap, ex);
		}
		return modelMap;
	}

}
