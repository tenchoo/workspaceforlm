package com.thinkgem.jeesite.common.dao;

import java.util.List;

import com.thinkgem.jeesite.common.entity.Goodsowners;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface GoodsownersMapper {
    int deleteByPrimaryKey(String id);

    int insert(Goodsowners record);

    int insertSelective(Goodsowners record);

    Goodsowners selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Goodsowners record);

    int updateByPrimaryKey(Goodsowners record);
    
    List<Goodsowners>findList(Goodsowners goodsowners);
    
    Goodsowners selectByPrimaryKeyId(String id);

   
    
    Goodsowners findOrderListId(String id);

	Goodsowners findByUserId(String userId);

	void update(Goodsowners g);//更新货主认证信息

	/**
	 * @description	查询货主列表
	 * @author 文帅
	 * @date 2017年8月21日 下午5:46:51
	 * @return
	 */
	List<Goodsowners> findShipperList();

	
	Goodsowners findGoodsownersByUserId(String userId);

}