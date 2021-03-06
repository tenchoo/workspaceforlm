package com.thinkgem.jeesite.mobile.driver.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.dao.AgentOrderMapper;
import com.thinkgem.jeesite.common.dao.DriveOrderMapper;
import com.thinkgem.jeesite.common.dao.DriversMapper;
import com.thinkgem.jeesite.common.dao.GoodsMapper;
import com.thinkgem.jeesite.common.dao.GoodsownerAgentJingjiaMapper;
import com.thinkgem.jeesite.common.dao.GoodsownerOrderMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.AgentOrder;
import com.thinkgem.jeesite.common.entity.DriveOrder;
import com.thinkgem.jeesite.common.entity.Drivers;
import com.thinkgem.jeesite.common.entity.Goods;
import com.thinkgem.jeesite.common.entity.GoodsownerAgentJingjia;
import com.thinkgem.jeesite.common.entity.GoodsownerOrder;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.mobile.utils.PageParam;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * @description 车主端-竞价
 * @author 文帅
 * @date 2017年9月7日 上午9:40:53
 */
@Service
@Transactional
public class MobileDriverBidService {
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsownerAgentJingjiaMapper goodsownerAgentJingjiaMapper;
	@Autowired
	private GoodsownerOrderMapper goodsownerOrderMapper;
	@Autowired
	private AgentOrderMapper agentOrderMapper;
	@Autowired
	private DriversMapper driversMapper;
	@Autowired
	private DriveOrderMapper driveOrderMapper;

	/**
	 * C类客户没有委托经纪人；A类客户发布货物时只有经纪人、内部车主能看到；B类客户发布货物时经纪人和所有车主都能看到
	 * A类客户经纪人一对一服务。发布A类客户货物时只有内部车主能看到；发布B类客户货物时所有车主都能看到；发布自己货物时所有车主都能看到
	 * 
	 * @description 查询竞价列表
	 * @author 文帅
	 * @date 2017年9月7日 上午10:19:08
	 * @param pageNo
	 * @param userId
	 *            当前登录车主ID
	 * @return
	 */
	public AjaxBeanUtil findDriverBidList(String pageNo, String userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		int currentPage = Integer.parseInt(pageNo) * PageParam.DEFAULT_PAGESIZE;
		int pageSize = PageParam.DEFAULT_PAGESIZE;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("currentPage", currentPage);
		paramMap.put("pageSize", pageSize);
		// level车主（0外部 1内部）
		List<Goods> list = null;
		if ("1".equals(user.getLevel())) {// 内部车主可以查看（A,B,C）所有货主竞价和经纪人接收委托的（A，B）类货主委托
											// 的货物以及经纪人自己发布的货物
			list = goodsMapper.findDriverBidList(paramMap);
		} else if ("0".equals(user.getLevel())) {// 外部车主只可以查看(B,C)类货主的竞价和经纪人接收委托的B类货主委托的货物以及经纪人自己发布的货物
			paramMap.put("isWaiBuDriver", 1);
			list = goodsMapper.findDriverBidList(paramMap);
		}
		if (list.size() == 0) {
			return new AjaxBeanUtil("暂无更多数据", false);
		}
		return new AjaxBeanUtil("查询成功", true, list);
	}

	/**
	 * @description 我的竞价列表
	 * @author 文帅
	 * @date 2017年9月8日 上午10:51:07
	 * @param pageNo
	 * @param userId
	 * @return
	 */
	public AjaxBeanUtil myBidList(String pageNo, String userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		int currentPage = Integer.parseInt(pageNo) * PageParam.DEFAULT_PAGESIZE;
		int pageSize = PageParam.DEFAULT_PAGESIZE;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", user.getId());
		paramMap.put("currentPage", currentPage);
		paramMap.put("pageSize", pageSize);
		List<GoodsownerAgentJingjia> list = goodsownerAgentJingjiaMapper.findListByUserId(paramMap);
		if (list.size() == 0) {
			return new AjaxBeanUtil("暂无更多数据", false);
		}
		for (GoodsownerAgentJingjia g : list) {
			// JingjiaType 用于标记经纪人/货主发布的竞价（0货主 1经纪人）
			Goods goods = null;
			if ("0".equals(g.getJingjiaType())) {
				GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.selectByPrimaryKey(g.getOrderId());
				goods = goodsMapper.selectByPrimaryKey(goodsownerOrder.getGoodsId());
			} else if ("1".equals(g.getJingjiaType())) {
				AgentOrder agentOrder = agentOrderMapper.selectByPrimaryKey(g.getOrderId());
				goods = goodsMapper.selectByPrimaryKey(agentOrder.getGoodsId());
			}
			g.setGoodsId(goods.getId());
			g.setShipperArea(goods.getShipperArea());
			g.setReciverArea(goods.getReciverArea());
			g.setGoodsType(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", goods.getGoodsType()));
			g.setTruckType(DictUtils.getDictLabel(goods.getNeedTruckType(), "truck_type", goods.getNeedTruckType()));
			g.setTruckLength(DictUtils.getDictLabel(goods.getNeedTruckLength(), "truck_length", goods.getNeedTruckLength()));
			g.setGoodsNum(goods.getGoodsNum());
			g.setGoodsUnit(goods.getGoodsUnit());
		}
		return new AjaxBeanUtil("查询成功", true, list);
	}

	/**
	 * @description 查看竞价详情
	 * @author 文帅
	 * @date 2017年9月8日 下午2:26:14
	 * @param goodsId
	 * @param type
	 *            用于判断是竞价的经纪人发布的货物还是货主发布的货物（0-货主，1-经纪人）
	 * @return
	 */
	public AjaxBeanUtil lookBidDetail(String goodsId, String type) {
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		if ("0".equals(type)) {
			GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.findOrderByGoodId(goods.getId());
			Integer goodsownerOrderCount = goodsownerOrderMapper.findShipCount(goodsownerOrder.getGoodsownersId());
			Users user = usersMapper.findUserByGoodsownersId(goodsownerOrder.getGoodsownersId());
			goods.setPriceMin(goodsownerOrder.getPriceMin());
			goods.setPriceMax(goodsownerOrder.getPriceMax());
			goods.setGoodsownerName(user.getTruename());
			goods.setPhoneno(user.getPhoneno());
			goods.setShipCount(goodsownerOrderCount);
			goods.setPersonDescribe(user.getPersonDescribe());
			goods.setOrderId(goodsownerOrder.getId());
		} else if ("1".equals(type)) {
			AgentOrder agentOrder = agentOrderMapper.findOrderByGoodId(goods.getId());
			Users user1 = usersMapper.findAgentByAgentId(agentOrder.getAgentsId());
			if ("1".equals(agentOrder.getWeituoOrFabu())) {// 接受类型(1委托、0发布)
				GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.selectByPrimaryKey(agentOrder.getGoodsownerOrderid());
				Integer goodsownerOrderCount = goodsownerOrderMapper.findShipCount(goodsownerOrder.getGoodsownersId());
				Users user = usersMapper.findUserByGoodsownersId(goodsownerOrder.getGoodsownersId());
				goods.setPriceMin(goodsownerOrder.getPriceMin());
				goods.setPriceMax(goodsownerOrder.getPriceMax());
				goods.setGoodsownerName(user.getTruename());
				goods.setPhoneno(user.getPhoneno());
				goods.setShipCount(goodsownerOrderCount);
				goods.setPersonDescribe(user.getPersonDescribe());
			} else if ("0".equals(agentOrder.getWeituoOrFabu())) {
				goods.setPriceMin(agentOrder.getPriceMin());
				goods.setPriceMax(agentOrder.getPriceMax());
				goods.setGoodsownerName(user1.getTruename());
				goods.setPhoneno(user1.getPhoneno());
				/* goods.setShipCount(goodsownerOrderCount); */
				goods.setPersonDescribe(user1.getPersonDescribe());
			}
			goods.setOrderId(agentOrder.getId());
		}
		return new AjaxBeanUtil("查询成功", true, goods);
	}

	/**
	 * @description 出价
	 * @author 文帅
	 * @date 2017年9月8日 下午5:10:44
	 * @param orderId
	 * @param userId
	 * @param type
	 *            用于判断是经纪人/货主发布的竞价（0货主 1经纪人）
	 * @return
	 */
	public AjaxBeanUtil offerPrice(String orderId, String userId, String type, Double jingjiaPrice, String teamMemberId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		GoodsownerAgentJingjia gaj = new GoodsownerAgentJingjia();
		if (StringUtils.isEmpty(teamMemberId)) {// 说明没有指派车队员
			gaj.setUserId(userId);
		} else {// 指派了车队员
			Drivers drivers = driversMapper.selectByPrimaryKey(teamMemberId);
			gaj.setUserId(drivers.getUserid());
		}
		gaj.setId(IdGen.uuid());
		gaj.setOrderId(orderId);
		gaj.setJingjiaStatus("0");
		gaj.setJingjiaPrice(jingjiaPrice);
		gaj.setJingjiaType(type);
		gaj.setCreateBy(user.getTruename());
		gaj.setCreateDate(new Date());
		gaj.setUpdateBy(user.getTruename());
		gaj.setUpdateDate(new Date());
		goodsownerAgentJingjiaMapper.insertSelective(gaj);
		return new AjaxBeanUtil("出价成功", true);
	}

	/**
	 * @description 根据用户ID查询车队员
	 * @author 文帅
	 * @date 2017年9月8日 下午5:42:13
	 * @param userId
	 * @return
	 */
	public AjaxBeanUtil findTeamMembers(String pageNo, String userId) {
		int currentPage = Integer.parseInt(pageNo) * PageParam.DEFAULT_PAGESIZE;
		int pageSize = PageParam.DEFAULT_PAGESIZE;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("currentPage", currentPage);
		paramMap.put("pageSize", pageSize);
		Drivers drivers = driversMapper.findByUserId(userId);
		if (!"2".equals(drivers.getDriverType())) {
			return new AjaxBeanUtil("该用户不是车队长", false);
		} else {
			paramMap.put("driveFleetId", drivers.getDriveFleetId());
			List<Drivers> list = driversMapper.findTeamMembersList(paramMap);
			if (list.size() == 0) {
				return new AjaxBeanUtil("暂无更多数据", false);
			} else {
				for (Drivers d : list) {
					// 查询运输次数
					Integer transportCount = driveOrderMapper.findCountByDriverId(d.getId());
					d.setTransportCount(transportCount);
				}
			}
			return new AjaxBeanUtil("查询成功", true, list);
		}
	}
}
