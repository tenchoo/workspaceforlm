package com.thinkgem.jeesite.common.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.entity.UsersIntegralRecord;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface UsersIntegralRecordMapper {
    int insert(UsersIntegralRecord record);

    int insertSelective(UsersIntegralRecord record);
    /**
     * @description	根据用户ID查询该用户的积分列表
     * @author 文帅
     * @date 2017年9月4日 上午10:54:47
     * @param paramMap
     * @return
     */
    List<UsersIntegralRecord> findInseranceList(Map<String, Object> paramMap);
    
    /**
     * @description	：获取用户总积分
     * @author pangchengxiang
     * @date 2017年9月21日 下午3:07:41
     * @return Integer
     */
    Integer findUserAllInserance(String userId);
}