package tech.hegj.bodyshield.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import tech.hegj.bodyshield.config.Constants;
import tech.hegj.bodyshield.config.ErrorCode;
import tech.hegj.bodyshield.config.Keys;

public class BaseController {
	
	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	protected HttpSession session;
	
	@Autowired
	protected HttpServletRequest request;
	
	/**
	 * 从session中取值， 返回 int 类型的值
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 * @author xiao
	 */
	public int getSessionAttribute(String name) throws Exception {
		Object idObj = session.getAttribute(name);
		if (idObj == null)
			return Constants.INVALIDATE;
		return Integer.parseInt(idObj.toString());
	}
	
	
	
	/**
	 * 处理异常
	 * @param ex
	 * @return
	 */
	public ModelMap handlerSysError(ModelMap modelMap,Exception ex){
		modelMap.addAttribute(Keys.RETURN_CODE,ErrorCode.SYSTEM_ERROR);
		if(ex != null){
			ex.printStackTrace();
			modelMap.addAttribute(Keys.MESSAGE,ex.getMessage() == null?"系统错误":ex.getMessage());
			//ExcHandler.sendException(ex, ErrorLevelEnum.FALAL_ERROR);
		}else{
			modelMap.addAttribute(Keys.MESSAGE,"系统错误");
		}
		return modelMap;
	}
	
	 
	/**
	 * 获取String类型参数值
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	protected String getString(String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		} else {
			return value.trim();
		}
	}

	/**
	 * 获取Integer类型参数值
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	protected Integer getInt(String paramName, Integer defaultValue) {
		String value = getString(paramName, null);
		if (value != null) {
			try {
				return Integer.valueOf(value.trim());
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Long类型参数值
	 */
	protected Long getLong(String paramName, Long defaultValue) {
		String value = getString(paramName, null);
		if (value != null) {
			try {
				return Long.valueOf(value.trim());
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}
	
	/**
	 * 获取Boolean类型参数值
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	protected Boolean getBoolean(String paramName, Boolean defaultValue) {
		String value = getString(paramName, null);
		if (value != null) {
			try {
				return Integer.valueOf(value) != 0;
			} catch (Exception e) {
			}
			try {
				return Boolean.valueOf(value);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}
	
	/**
	 * 获取请求ip
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getRequestIP() {
		String ip = null;
		Enumeration enu = request.getHeaderNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			if (name.equalsIgnoreCase("X-Forwarded-For")) {
				ip = request.getHeader(name);
			} else if (name.equalsIgnoreCase("Proxy-Client-IP")) {
				ip = request.getHeader(name);
			} else if (name.equalsIgnoreCase("WL-Proxy-Client-IP")) {
				ip = request.getHeader(name);
			}
			if (StringUtils.equalsIgnoreCase(ip, "unknown")) {
				continue;
			}
			if (!StringUtils.isEmpty(ip)) {
				break;
			}
		}
		if (StringUtils.isEmpty(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
}