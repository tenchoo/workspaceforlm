package com.thinkgem.jeesite.mobile.shipper.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.mobile.shipper.service.MobileIntegralService;
/**
 * @description	我的积分
 * @author 文帅
 * @date 2017年9月4日 上午10:31:54
 */
@Controller
@RequestMapping(value="/mobileIntegral")
public class MobileIntegralController {
	@Autowired
	private MobileIntegralService mobileIntegralService;
	
	/**
	 * @description	测试页面
	 * @author 文帅
	 * @date 2017年8月21日 下午4:47:31
	 * @return
	 */
	@RequestMapping(value="goShipperTest")
	public String goShipperTest(){
		return "mobile/test/shipperTest";
	}
	
	/**
	 * @description	我的积分明细列表
	 * @author 文帅
	 * @date 2017年9月4日 上午10:42:30
	 * @param currentPage
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="findInseranceList",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil findInseranceList(String currentPage,String userId){
		return mobileIntegralService.findInseranceList(currentPage,userId);
	}
	/**
	 * @description	添加积分
	 * @author 文帅
	 * @date 2017年9月4日 上午11:28:42
	 * @param type 用于判断添加积分的时机（0-货主注册，1-车主注册，2-经纪人注册，3-每天登录，4-完善用户信息，5-评价订单，6-完成一次交易）
	 * @param userId 当前用户ID
	 * @return
	 */
	@RequestMapping(value="insertIntegral",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil insertIntegral(String type,String userId){
		return mobileIntegralService.insertIntegral(type,userId);
	}
}
