package com.thinkgem.jeesite.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.entity.GoodsownerDeal;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface GoodsownerDealMapper {
    int deleteByPrimaryKey(String id);

    int insert(GoodsownerDeal record);

    int insertSelective(GoodsownerDeal record);

    GoodsownerDeal selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GoodsownerDeal record);

    int updateByPrimaryKey(GoodsownerDeal record);
    
    /**
     * @description	批量结算
     * @author 文帅
     * @date 2017年8月11日 下午5:46:01
     * @param ids
     */
    void plUpdateById(@Param("list") List<GoodsownerDeal> list);
}