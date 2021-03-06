package com.thinkgem.jeesite.common.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.entity.DriveOrder;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface DriveOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(DriveOrder record);

    int insertSelective(DriveOrder record);

    DriveOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DriveOrder record);

    int updateByPrimaryKey(DriveOrder record);
    /**
     * @description	查询信息费列表
     * @author 文帅
     * @date 2017年8月9日 下午2:56:38
     * @param driveOrder
     * @return
     */
    List<DriveOrder> findInfoFeeList(DriveOrder driveOrder);
    /**
     * @description	通过车主ID查询车主订单明细
     * @author 文帅
     * @date 2017年8月19日 上午11:09:47
     * @param driversId
     * @return
     */
    List<DriveOrder> findListByDriversId(DriveOrder driveOrder);
    /**
     * @description 根据车主ID查询该车主运输数量	
     * @author 文帅
     * @date 2017年8月25日 上午11:46:12
     * @param driversId
     * @return
     */
    Integer findCountByDriverId(String driversId);
    
    /**
     * 根据经纪人订单或货主订单查询车主订单
     *   
     * @author 张彦学
     * 日期 2017年9月2日
     * @param id
     * @return
     */
    List<DriveOrder> findDriveOrderByOrderId(String id);
    /**
     * @description	查询订单列表
     * @author 文帅
     * @date 2017年9月11日 下午3:18:29
     * @param paramMap
     * @return
     */
    List<DriveOrder> findDriverOrderList(Map<String, Object> paramMap);
    /**
     * @description	根据货物ID查询订单信息
     * @author 文帅
     * @date 2017年9月12日 下午3:50:24
     * @param goodsId
     * @return
     */
    DriveOrder findOrderByGoodsId(String goodsId);
    
    List<DriveOrder> findOrderListByGoodsId(String goodsId);
}