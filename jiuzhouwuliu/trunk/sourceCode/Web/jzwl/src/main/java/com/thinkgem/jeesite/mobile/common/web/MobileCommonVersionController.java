package com.thinkgem.jeesite.mobile.common.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.mobile.common.service.MobileCommonVersionService;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;

/**
 * @desc app版本更新
 * @author yaotengfei
 * @date 2017年8月10日下午2:50:59
 */
@Controller
@RequestMapping(value="/MobileCommonVersion")
public class MobileCommonVersionController {

	@Autowired
	private MobileCommonVersionService mobileCommonVersionService;
	
	/**
	 * @desc APP版本更新
	 * @author yaotengfei
	 * @date 2017年8月10日下午2:57:01
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/versionUpdate", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil versionUpdate(HttpServletRequest request){
		return mobileCommonVersionService.versionUpdate(request);
	}
}
