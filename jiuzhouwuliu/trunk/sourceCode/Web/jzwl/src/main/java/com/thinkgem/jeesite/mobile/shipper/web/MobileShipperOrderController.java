package com.thinkgem.jeesite.mobile.shipper.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.entity.ComplainRecord;
import com.thinkgem.jeesite.common.entity.OrderRemark;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.mobile.shipper.service.MobileShipperOrderService;

/**
 * 货主端订单--Controller
 *   
 * @author 张彦学
 * 日期 2017年9月1日
 */
@Controller
@RequestMapping(value = "/mobileShipperOrder")
public class MobileShipperOrderController {
	
	@Resource
	private MobileShipperOrderService mobileShipperOrderService;
	
	/**
	 * 测试页面
	 *   
	 * @author 张彦学
	 * 日期 2017年9月1日
	 * @return
	 */
	@RequestMapping(value = "/goShipperOrderTest")
	public String goShipperOrderTest(){
		return "mobile/test/shipperOrderTest";
	}
	
	/**
	 * 货主订单列表
	 *   
	 * @author 张彦学
	 * 日期 2017年9月1日
	 * @param userId 当前登录人id
	 * @param type 订单状态（当前订单--0，历史订单--1，全部订单--2）
	 * @param currentPage 当前页数
	 * @return 订单列表数据
	 */
	@RequestMapping(value = "/shipperOrderList",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil findShipperOrderList(String userId,String type,String currentPage){
		
		return mobileShipperOrderService.findShipperOrderList(userId, type, currentPage);
	}

	/**
	 * 根据订单id查询订单详情
	 *   
	 * @author 张彦学
	 * 日期 2017年9月1日
	 * @param orderId 订单id
	 * @param type 是否为委托单 0--不是，1--是
	 * @return
	 */
	@RequestMapping(value = "/checkOrder",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil checkOrder(String orderId, String type){
		
		return mobileShipperOrderService.checkOrder(orderId, type);
	}
	
	/**
	 * 根据车主订单id获取物流位置信息
	 *   
	 * @author 张彦学
	 * 日期 2017年9月2日
	 * @param driverOrderId 车主订单id
	 * @return
	 */
	@RequestMapping(value = "/driverLogisticsPosition", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil driverLogisticsPosition(String driverOrderId){
		
		return mobileShipperOrderService.driverLogisticsPosition(driverOrderId);
	}
	
	/**
	 * 货主确认送达
	 *   
	 * @author 张彦学
	 * 日期 2017年9月2日
	 * @param goodsownerOrderId 货主订单id
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(value = "/confirmDelivery", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil confirmDelivery(String goodsownerOrderId, String userId){
		
		return mobileShipperOrderService.confirmDelivery(goodsownerOrderId, userId);
	}
	
	/**
	 * 订单评价
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderRemark 评价内容
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(value = "/evaluation", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil evaluation(OrderRemark orderRemark,String userId){
		
		return mobileShipperOrderService.evaluation(orderRemark, userId);
	}
	
	/**
	 * 根据货主订单id查询货物信息
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderId 货主订单id
	 * @return
	 */
	@RequestMapping(value = "/goodsDetail", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil goodsDetail(String orderId){
		
		return mobileShipperOrderService.goodsDetail(orderId);
	}
	
	/**
	 * 申诉
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param complainRecord
	 * @param userId
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = "/complain", method = RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil complain(ComplainRecord complainRecord, String userId,HttpServletRequest request) throws IllegalStateException, IOException{
		
		return mobileShipperOrderService.complain(complainRecord, userId, request);
	}
	
	/**
	 * 提醒装载
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderId 货主订单id
	 * @param userId 用户id
	 * @return
	 */
	@RequestMapping(value = "/reminderToLoad")
	@ResponseBody
	public AjaxBeanUtil reminderToLoad(String orderId, String userId){
	
		return mobileShipperOrderService.reminderToLoad(orderId, userId);
	}
	
	/** 
	 * @desc 根据货物id查询回执单
	 * @author yaotengfei
	 * @date 2017年9月13日上午9:54:29
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/getReturnReceiptByGoodsId")
	@ResponseBody
	public AjaxBeanUtil getReturnReceiptByGoodsId(String goodsId){
		return mobileShipperOrderService.getReturnReceiptByGoodsId(goodsId);
	}
}
