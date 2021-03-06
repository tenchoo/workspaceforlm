package com.thinkgem.jeesite.common.dao;

import java.util.List;

import com.thinkgem.jeesite.common.entity.DriverReciveMoney;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface DriverReciveMoneyMapper {
    int deleteByPrimaryKey(String id);

    int insert(DriverReciveMoney record);

    int insertSelective(DriverReciveMoney record);

    DriverReciveMoney selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DriverReciveMoney record);

    int updateByPrimaryKey(DriverReciveMoney record);
    /**
     * @description	查询垫资信息列表
     * @author 文帅
     * @date 2017年8月10日 下午5:19:00
     * @param driverReciveMoney
     * @return
     */
    List<DriverReciveMoney> findMatFundsList(DriverReciveMoney driverReciveMoney);
    /**
     * @description 根据ID查询垫资信息	
     * @author 文帅
     * @date 2017年8月15日 上午10:39:14
     * @param id
     * @return
     */
    DriverReciveMoney findById(String id);
}