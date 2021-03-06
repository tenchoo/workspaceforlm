package com.thinkgem.jeesite.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.entity.Drivers;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.mobile.utils.PageParam;
@MyBatisDao
public interface DriversMapper {
    int deleteByPrimaryKey(String id);

    int insert(Drivers record);

    int insertSelective(Drivers record);

    Drivers selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Drivers record);

    int updateByPrimaryKey(Drivers record);
    //查询车主列表
    List<Drivers>findList(Drivers drivers);
    
    Drivers selectByPrimaryKeyId(String id);
    
    Drivers   findByUserId(String userId);
    //查询订单信息
    List<Drivers>findOrderList(Drivers drivers);
    
    Drivers findOrderListId(String id);

	void update(Drivers d);
    
	Drivers findDriverByUserId(String userId);
	
	  int updateByPrimaryKeySelectiveByUserId(Drivers record);

	/**
	 * @description	：通过用户编号查询车主
	 * @author pangchengxiang
	 * @date 2017年8月23日 下午2:42:21
	 * @return Drivers
	 */
	Drivers findDriverByUserno(Drivers drivers);
	
	/**
	 * 根据货主订单id查询车主信息
	 *   
	 * @author 张彦学
	 * 日期 2017年9月2日
	 * @param id 订单id
	 * @return
	 */
	List<Drivers> findDriverByGoodsownerOrder(String id);
	//查询车牌号
	Drivers  getDriversByTruckNo(@Param("truckNo")String truckNo,@Param("driversId")String driversId);
	/**
	 * @description	根据车队长ID查询该车队的队员列表
	 * @author 文帅
	 * @date 2017年9月9日 上午9:56:23
	 * @param paramMap
	 * @return
	 */
	List<Drivers> findTeamMembersList(Map<String, Object> paramMap);
	List<Drivers> findDriversList(Map<String, Object> paramMap);//获取车主车队的车辆列表 崔亚光
	void addCar(Drivers drivers);//添加车辆   崔亚光

	List<Drivers> findInviteList(Map<String, Object> paramMap);//获取普通车主（邀请）   崔亚光

	void exitDriveFleet(Drivers record);//退出车队   崔亚光

	List<Drivers> changeBodyList(Map<String, Object> paramMap);//转让身份车队人员列表  崔亚光

	void changeBody(String id);//转让身份  崔亚光

	void driveLine(String id);// 线路记录  崔亚光

	Drivers selectByUserid(String userid);//获取我的车辆   崔亚光
	
	/**
	 * @desc 根据条件查询司机
	 * @author yaotengfei
	 * @date 2017年9月20日下午3:57:51
	 * @param drivers
	 * @return
	 */
	List<Drivers> getDriverByCons(@Param("PageParam")PageParam pageParam, @Param("Drivers")Drivers drivers,@Param("areaCode")String areaCode);
    
}