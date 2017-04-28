package tech.hegj.bodyshield.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tech.hegj.bodyshield.config.ErrorCode;
import tech.hegj.bodyshield.config.Keys;
import tech.hegj.bodyshield.dao.UserDao;
import tech.hegj.bodyshield.model.User;
import tech.hegj.bodyshield.service.CaptchaService;
import tech.hegj.bodyshield.utils.HttpClientUtils;
import tech.hegj.bodyshield.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hegj
 *
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Autowired
	UserDao userDao;

	private static final String MSG_EN = "【Apexto】Hello! Your verification code is: %s, validity within five minutes, if not, please ignore";
	private static final String MSG_CN = "【Apexto】您好!您的验证码是:%s,五分钟内有效，如非本人操作请忽略。";

	public static final String APPID = "13547";
	public static final String APPKEY = "b7a58fa9947b3bea2d783e53bf10f3be";

	@Override
	public ModelMap sendByMobile(String mobile, ModelMap modelMap) throws Exception {
		String captcha = send(mobile);
		if(StringUtils.isBlank(captcha)) {
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "发送短信失败");
		}else {
			JSONObject json = new JSONObject();
			json.put("captcha", captcha);
			modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
			modelMap.put(Keys.DATA, json);
		}
		return modelMap;
	}

	@Override
	public ModelMap sendByUid(int uid, ModelMap modelMap) throws Exception {
		User user = userDao.get(uid);
		if(null != user && StringUtils.isNotBlank(user.getMobile())) {
			String captcha = send(user.getMobile());
			if(StringUtils.isBlank(captcha)) {
				modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
				modelMap.put(Keys.MESSAGE, "发送短信失败");
			}else {
				JSONObject json = new JSONObject();
				json.put("captcha", captcha);
				modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
				modelMap.put(Keys.DATA, json);
			}
		}else {
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "发送短信失败");
		}
		return modelMap;
	}

	@Override
	public ModelMap sendByUserName(String userName, ModelMap modelMap) throws Exception {
		User user = userDao.getByName(userName);
		if(null != user && StringUtils.isNotBlank(user.getMobile())) {
			String captcha = send(user.getMobile());
			if(StringUtils.isBlank(captcha)) {
				modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
				modelMap.put(Keys.MESSAGE, "发送短信失败");
			}else {
				JSONObject json = new JSONObject();
				json.put("captcha", captcha);
				modelMap.put(Keys.RETURN_CODE, ErrorCode.OK);
				modelMap.put(Keys.DATA, json);
			}
		}else {
			modelMap.put(Keys.RETURN_CODE, ErrorCode.SYSTEM_ERROR);
			modelMap.put(Keys.MESSAGE, "发送短信失败");
		}
		return modelMap;
	}

	private String send(String mobile) throws Exception{
		String captcha = Util.createRandom(true, 6);
		String[] arr = mobile.split("-");
		String countryCode = arr[0];
		String num = arr[1];
		if("86".equals(countryCode)){
			String url = "https://api.mysubmail.com/message/send.json";
			JSONObject vars = new JSONObject();
			vars.put("var", captcha);
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("appid", APPID));
			params.add(new BasicNameValuePair("to", num));
			params.add(new BasicNameValuePair("content", String.format(MSG_CN, captcha)));
			params.add(new BasicNameValuePair("signature", APPKEY));
			String result = HttpClientUtils.doPost(url, params);
			System.out.println(result);
			if(!result.contains("success")) {
				return  null;
			}
		}else {
			String url = "http://www.bananasystem.com/json.php";
			JSONObject arg = new JSONObject();
			arg.put("i_user_id", 1901);
			arg.put("s_api_key", "x4XaD30EXAnVtgb3Vpw7pjcc0h8yzz0fBVqNMrgB");
			arg.put("s_class", "c_sms");
			arg.put("s_method", "f_send_verify");
			arg.put("s_to_country_code", "1");
			arg.put("s_to_number", num);
			arg.put("s_message", String.format(MSG_EN, captcha));
			JSONObject json = new JSONObject();
			json.put("a_arg", arg);
			String result = HttpClientUtils.doPost(url, json.toJSONString());
		}
		return captcha;
	}
}
