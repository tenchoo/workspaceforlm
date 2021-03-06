package com.thinkgem.jeesite.common.dao;

import java.util.List;

import com.thinkgem.jeesite.common.entity.GoodsBilling;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface GoodsBillingMapper {
    int deleteByPrimaryKey(String id);

    int insert(GoodsBilling record);

    int insertSelective(GoodsBilling record);

    GoodsBilling selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GoodsBilling record);

    int updateByPrimaryKey(GoodsBilling record);
    /**
     * @description	查询开票列表
     * @author 文帅
     * @date 2017年8月10日 上午9:27:56
     * @param goodsBilling
     * @return
     */
    List<GoodsBilling> findBillingList(GoodsBilling goodsBilling);
    /**
     * @description	根据开票信息id查询实体
     * @author 文帅
     * @date 2017年8月10日 上午11:31:16
     * @param id
     * @return
     */
    GoodsBilling findBillingById(String id);
}