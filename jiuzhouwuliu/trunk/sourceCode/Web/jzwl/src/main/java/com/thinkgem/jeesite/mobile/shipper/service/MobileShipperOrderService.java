package com.thinkgem.jeesite.mobile.shipper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.dao.AgentOrderMapper;
import com.thinkgem.jeesite.common.dao.AgentsMapper;
import com.thinkgem.jeesite.common.dao.ComplainRecordMapper;
import com.thinkgem.jeesite.common.dao.DriveOrderMapper;
import com.thinkgem.jeesite.common.dao.DriverLogisticsPositionMapper;
import com.thinkgem.jeesite.common.dao.DriversMapper;
import com.thinkgem.jeesite.common.dao.GoodsownerOrderMapper;
import com.thinkgem.jeesite.common.dao.IntegralRuleMapper;
import com.thinkgem.jeesite.common.dao.OrderRemarkMapper;
import com.thinkgem.jeesite.common.dao.UsersIntegralRecordMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.dao.UsersMessageMapper;
import com.thinkgem.jeesite.common.entity.AgentOrder;
import com.thinkgem.jeesite.common.entity.Agents;
import com.thinkgem.jeesite.common.entity.ComplainRecord;
import com.thinkgem.jeesite.common.entity.DriveOrder;
import com.thinkgem.jeesite.common.entity.DriverLogisticsPosition;
import com.thinkgem.jeesite.common.entity.Drivers;
import com.thinkgem.jeesite.common.entity.Goods;
import com.thinkgem.jeesite.common.entity.GoodsownerOrder;
import com.thinkgem.jeesite.common.entity.IntegralRule;
import com.thinkgem.jeesite.common.entity.OrderRemark;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.entity.UsersIntegralRecord;
import com.thinkgem.jeesite.common.entity.UsersMessage;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.mobile.utils.PageParam;
import com.thinkgem.jeesite.mobile.utils.SaveFileUtil;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 货主端订单--Service
 *   
 * @author 张彦学
 * 日期 2017年9月1日
 */
@Service
@Transactional
public class MobileShipperOrderService {
	
	@Resource
	private GoodsownerOrderMapper goodsownerOrderMapper;
	
	@Resource
	private ComplainRecordMapper complainRecordMapper;
	
	@Resource
	private OrderRemarkMapper orderRemarkMapper;
	
	@Resource
	private DriverLogisticsPositionMapper driverLogisticsPositionMapper;
	
	@Resource
	private DriversMapper driversMapper;
	
	@Resource
	private AgentsMapper agentsMapper;
	
	@Resource
	private AgentOrderMapper agentOrderMapper;
	
	@Resource
	private DriveOrderMapper driveOrderMapper;
	
	@Resource
	private UsersMapper usersMapper;
	
	@Resource
	private UsersMessageMapper usersMessageMapper;
	
	@Resource
	private IntegralRuleMapper integralRuleMapper;
	
	@Resource
	private UsersIntegralRecordMapper usersIntegralRecordMapper;
	
	
	/**
	 * 货主订单列表
	 *   
	 * @author 张彦学
	 * 日期 2017年9月1日 
	 * @param userId 用户id
	 * @param type 订单状态（当前订单--0，历史订单--1，全部订单--2）
	 * @param currentPage 当前页数
	 * @return 订单列表数据
	 */
	public AjaxBeanUtil findShipperOrderList(String userId,String type,String currentPage){
		if(userId == null || "".equals(userId)){
			return new AjaxBeanUtil("请先登录", false);
		}
		
		if(type == null || "".equals(type)){
			type = "0";
		}
		
		if(currentPage == null || "".equals(currentPage)){
			currentPage = "0";
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("type", type);
		paramMap.put("pageNo", Integer.parseInt(currentPage)*PageParam.DEFAULT_PAGESIZE);
		paramMap.put("pageSize", PageParam.DEFAULT_PAGESIZE);
		List<Goods> list = goodsownerOrderMapper.findShipperOrderList(paramMap);
		for(Goods goods : list){
			goods.setGoodCurrStatusStr(DictUtils.getDictLabel(goods.getGoodCurrStatus(), "order_status", goods.getGoodCurrStatus()));
			goods.setGoodsTypeStr(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", goods.getGoodsType()));
			goods.setNeedTruckTypeStr(DictUtils.getDictLabel(goods.getNeedTruckType(), "truck_type", goods.getNeedTruckType()));
			//查询order_remark是否评论过
			OrderRemark orderRemark = orderRemarkMapper.findOrderRemarkByGoodsownerOrder(goods.getGoodsownerOrderId());
			if(orderRemark == null){
				goods.setIsComment("0");
			}else{
				goods.setIsComment("1");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		resultMap.put("pageSize", PageParam.DEFAULT_PAGESIZE);
		return new AjaxBeanUtil("查询成功", true, resultMap);
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
	public AjaxBeanUtil checkOrder(String orderId, String type){
		//System.out.println("订单id："+orderId);
		//System.out.println("类型："+type);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.findOrderDetail(orderId);//货物及订单信息
		goodsownerOrder.setGoodsType(DictUtils.getDictLabel(goodsownerOrder.getGoodsType(), "goods_type", goodsownerOrder.getGoodsType()));
		resultMap.put("goodsownerOrder", goodsownerOrder);
		List<Drivers> driverList =null;
		if ("1".equals(type)) {//如果为委托单
			AgentOrder agentOrder = agentOrderMapper.findByGoodsownerOrderId(orderId);
			driverList = driversMapper.findDriverByGoodsownerOrder(agentOrder.getId());//车主信息
			resultMap.put("driverList", driverList);
			Agents agents = agentsMapper.findAgetnsByGoodsownerOrder(orderId);//经纪人
			resultMap.put("agents", agents);
		}else{//不是委托单
			driverList = driversMapper.findDriverByGoodsownerOrder(orderId);//车主信息
			resultMap.put("driverList", driverList);
			/*List<DriverLogisticsPosition> list = driverLogisticsPositionMapper.findDriverLogisticsPositionByGoodsownerOrder(orderId);//物流信息（倒序排序取最新的）
			if(list.size() > 0){
				resultMap.put("position", list);
			}*/
		}
		if(null!=driverList){
			Map<String,List<DriverLogisticsPosition>> mapPosition = new LinkedHashMap<String,List<DriverLogisticsPosition>>();
			Map<String,ComplainRecord> mapComplainRecord = new LinkedHashMap<String,ComplainRecord>();
			Map<String,OrderRemark> mapOrderRemark = new LinkedHashMap<String,OrderRemark>();
			for(Drivers d :driverList){
				//查询物流信息
				List<DriverLogisticsPosition> list = driverLogisticsPositionMapper.findDriverLogisticsPositionByDriverOrderId(d.getDriverOrderId());//物流信息（倒序排序取最新的）
				if(list.size() > 0){
					mapPosition.put(d.getId(), list);
				}
				//查询申诉信息
				ComplainRecord complainRecord = complainRecordMapper.findComplainRecordByGoodsownerOrder(d.getDriverOrderId());//申诉信息
				mapComplainRecord.put(d.getId(), complainRecord);
				//查询订单评论信息
				OrderRemark orderRemark = orderRemarkMapper.findOrderRemarkByGoodsownerOrder(d.getDriverOrderId());//评价信息
				mapOrderRemark.put(d.getId(), orderRemark);
			}
			resultMap.put("complainRecord", mapComplainRecord);
			resultMap.put("orderRemark", mapOrderRemark);
			resultMap.put("position", mapPosition);
		}
		return new AjaxBeanUtil("查询成功", true, resultMap);
	}
	
	/**
	 * 根据车主订单id查询物流位置信息
	 *   
	 * @author 张彦学
	 * 日期 2017年9月2日
	 * @param driverOrderId 车主订单id
	 * @return
	 */
	public AjaxBeanUtil driverLogisticsPosition(String driverOrderId){
		List<DriverLogisticsPosition> list = driverLogisticsPositionMapper.findDriverLogisticsPositionByDriverOrderId(driverOrderId);
		return new AjaxBeanUtil("查询成功", true, list);
	}
	
	/**
	 *  货主确认送达
	 *   
	 * @author 张彦学
	 * 日期 2017年9月2日
	 * @param goodsownerOrderId 货主订单id
	 * @param userId 用户id
	 * @return
	 */
	public AjaxBeanUtil confirmDelivery(String goodsownerOrderId,String userId){
		GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.selectByPrimaryKey(goodsownerOrderId);
		List<DriveOrder> list = null;
		AgentOrder agentOrder = null;
		if("1".equals(goodsownerOrder.getJingjiaOrWeituo())){
			agentOrder = agentOrderMapper.findByGoodsownerOrderId(goodsownerOrderId);
			list = driveOrderMapper.findDriveOrderByOrderId(agentOrder.getId());
		}else{
			list = driveOrderMapper.findDriveOrderByOrderId(goodsownerOrderId);
		}
		goodsownerOrder.setGoodCurrStatus("5");
		Users users = usersMapper.selectByPrimaryKey(userId);
		goodsownerOrder.setUpdateBy(users.getTruename());
		goodsownerOrder.setUpdateDate(new Date());
		goodsownerOrderMapper.updateByPrimaryKeySelective(goodsownerOrder);
		if(agentOrder != null){
			agentOrder.setGoodCurrStatus("5");
			agentOrder.setUpdateBy(users.getTruename());
			agentOrder.setUpdateDate(new Date());
			agentOrderMapper.updateByPrimaryKeySelective(agentOrder);
		}
		if(list.size() > 0){
			for(DriveOrder d : list){
				d.setGoodsStatus("5");
				d.setUpdateBy(users.getTruename());
				d.setUpdateDate(new Date());
				driveOrderMapper.updateByPrimaryKeySelective(d);
			}
		}
		//用户送积分
			//获取积分规则
		IntegralRule integralRule = integralRuleMapper.findEntity();
			//获取用户当前积分
		int currentPoint = users.getCurrentPoint();
		users.setCurrentPoint(currentPoint+integralRule.getRemarkOrder());
		users.preAppUpdate();
		usersMapper.updateByPrimaryKeySelective(users);
		//插入积分记录
		UsersIntegralRecord usersIntegralRecord = new UsersIntegralRecord();
		usersIntegralRecord.setId(IdGen.uuid());
		usersIntegralRecord.setUserid(userId);
		usersIntegralRecord.setIntegralDescribe("完成交易送积分");
		usersIntegralRecord.setIntegralScore(integralRule.getCompleteOrder());
		usersIntegralRecord.setDelFlag("0");
		usersIntegralRecord.preAppInsert();
		usersIntegralRecordMapper.insertSelective(usersIntegralRecord);
		return new AjaxBeanUtil("操作成功", true);
	}
	
	/**
	 * 订单评价
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderRemark 评价信息
	 * @param userId 用户id
	 * @return
	 */
	public AjaxBeanUtil evaluation(OrderRemark orderRemark, String userId){
		//根据orderId查询是否已评价
		OrderRemark orderRemarkObj = orderRemarkMapper.findOrderRemarkByGoodsownerOrder(orderRemark.getOrderId());
		if(orderRemarkObj != null){
			return new AjaxBeanUtil("改订单已评价", false);
		}
		Users users = usersMapper.selectByPrimaryKey(userId);
		orderRemark.preInsert();
		orderRemark.setCreateBy(users.getTruename());
		orderRemark.setUpdateBy(users.getTruename());
		orderRemark.setDelFlag("0");
		orderRemarkMapper.insertSelective(orderRemark);
		//用户送积分
			//获取积分规则
		IntegralRule integralRule = integralRuleMapper.findEntity();
			//获取用户当前积分
		int currentPoint = users.getCurrentPoint();
		users.setCurrentPoint(currentPoint+integralRule.getRemarkOrder());
		users.preAppUpdate();
		usersMapper.updateByPrimaryKeySelective(users);
		//插入积分记录
		UsersIntegralRecord usersIntegralRecord = new UsersIntegralRecord();
		usersIntegralRecord.setId(IdGen.uuid());
		usersIntegralRecord.setUserid(userId);
		usersIntegralRecord.setIntegralDescribe("订单评价送积分");
		usersIntegralRecord.setIntegralScore(integralRule.getRemarkOrder());
		usersIntegralRecord.setDelFlag("0");
		usersIntegralRecord.preAppInsert();
		usersIntegralRecordMapper.insertSelective(usersIntegralRecord);
		return new AjaxBeanUtil("评价成功", true);
	}
	
	/**
	 * 根据货主订单id查询货物信息
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderId 货主订单id
	 * @return
	 */
	public AjaxBeanUtil goodsDetail(String orderId){
		Goods goods = goodsownerOrderMapper.findGoodsByOrderId(orderId);
		goods.setOrderStatus(DictUtils.getDictLabel(goods.getGoodCurrStatus(), "order_status", goods.getGoodCurrStatus()));
		goods.setGoodsType(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", goods.getGoodsType()));
		goods.setNeedTruckType(DictUtils.getDictLabel(goods.getNeedTruckType(), "truck_type", goods.getNeedTruckType()));
		List<Dict> list = DictUtils.getDictList("complain_reason");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("goods", goods);
		resultMap.put("complainReason", list);
		return new AjaxBeanUtil("查询成功", true, resultMap);
	}
	

	/**
	 * 申述
	 *   
	 * @author 张彦学
	 * 日期 2017年9月4日
	 * @param orderId 货主订单id
	 * @return
	 */
	public AjaxBeanUtil complain(ComplainRecord complainRecord, String userId, HttpServletRequest request) throws IllegalStateException, IOException{
		Users users = usersMapper.selectByPrimaryKey(userId);
		complainRecord.preInsert();
		complainRecord.setUserid(userId);
		complainRecord.setComplainTime(new Date());
		complainRecord.setDelFlag("0");
		complainRecord.setCreateBy(users.getTruename());
		complainRecord.setUpdateBy(users.getTruename());
		//创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();
            //图片存放路径
            String tempURL = "/upload/Img/APPComplainImg/";
            int i = 1;
            while(iter.hasNext()){
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());
                String imgPath = SaveFileUtil.saveImg(request,file,tempURL);
                if(i == 1){
                	complainRecord.setImg1(imgPath);
    	        }
    	        if(i == 2){
    	        	complainRecord.setImg2(imgPath);
    	        }
    	        if(i == 3){
    	        	complainRecord.setImg3(imgPath);
    	        }
    	        if(i == 4){
    	        	complainRecord.setImg4(imgPath);
    	        }
    	        if(i == 5){
    	        	complainRecord.setImg5(imgPath);
    	        }
    	        i++;
            }
        }
        complainRecordMapper.insertSelective(complainRecord);
		return new AjaxBeanUtil("申诉成功", true);
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
	public AjaxBeanUtil reminderToLoad(String orderId,String userId){
		Users users = usersMapper.selectByPrimaryKey(userId);
		UsersMessage usersMessage = new UsersMessage();
		usersMessage.setCreateBy(users.getTruename());
		usersMessage.setUpdateBy(users.getTruename());
		usersMessage.setDelFlag("0");
		usersMessage.setIslook("0");
		usersMessage.setMessageOneType("1");
		usersMessage.setMessageTitle("通知消息");
		usersMessage.setMessageContent("提醒装载！");
		Goods goods = goodsownerOrderMapper.findGoodsByOrderId(orderId);
		usersMessage.setGoodsType(goods.getGoodsType());
		usersMessage.setOrderNo(goods.getOrderNo());
		List<Drivers> driverList = driversMapper.findDriverByGoodsownerOrder(orderId);
		List<Users> userList = new ArrayList<Users>();
		for(Drivers d : driverList){
			usersMessage.preInsert();
			usersMessage.setUserid(d.getId());
			usersMessageMapper.insertSelective(usersMessage);
			Users u = usersMapper.selectByPrimaryKey(d.getUserid());
			userList.add(u);
		}
		//PushMessageUtils.PushToSomeUser("提醒装载！", "通知消息", null, userList);
		return new AjaxBeanUtil("提醒成功", true);
	}
	
	/** 
	 * @desc 根据货物id查询回执单
	 * @author yaotengfei
	 * @date 2017年9月13日上午9:54:29
	 * @param goodsId
	 * @return
	 */
	public AjaxBeanUtil getReturnReceiptByGoodsId(String goodsId){
		List<DriveOrder> driveOrderList = driveOrderMapper.findOrderListByGoodsId(goodsId);
		if(driveOrderList.size() == 0){
			return new AjaxBeanUtil("暂无回执单", false);
		}
		return new AjaxBeanUtil("success", true, driveOrderList.get(0));
	}
}
