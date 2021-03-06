package com.thinkgem.jeesite.mobile.shipper.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.mobile.shipper.service.MobileShipperMessageService;

/**
 * app货主端消息--Controller
 *   
 * @author 张彦学
 * 日期 2017年9月4日
 */
@Controller
@RequestMapping(value = "/mobileShipperMessage")
public class MobileShipperMessageController {

	@Resource
	private MobileShipperMessageService shipperMessageService;
	
	/**
	 * 消息测试页面
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @return
	 */
	@RequestMapping(value = "/shipperMessageTest")
	public String getshipperMessageTest(){
		
		return "mobile/test/shipperMessageTest";
	}
	
	/**
     * 货主端消息分类
     *   
     * @author 张彦学
     * 日期 2017年9月4日
     * @param userId 用户id
     * @return
     */
	@RequestMapping(value = "/messageInfo")
	@ResponseBody
	public AjaxBeanUtil getMessageInfo(String userId){
	
		return shipperMessageService.getMessageInfo(userId);
	}
	
	/**
	 * 货主端消息列表
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param messageType 消息类型
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(value = "/messageList", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil getMessageList(String messageType, String userId){
		
		return shipperMessageService.getMessageList(messageType, userId);
	}
}
