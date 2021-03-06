package com.thinkgem.jeesite.modules.rule.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.dao.PlatformPayRuleMapper;
import com.thinkgem.jeesite.common.entity.PlatformPayRule;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/** 
 * @author 作者:cuiyg
 * @version 创建时间：2017年8月8日 下午2:17:51 
 * 类说明: 平台垫资付款设置service
 */
@Service("platformPayRuleService")
@Transactional
public class PlatformPayRuleService {
	@Autowired
	private PlatformPayRuleMapper payRuleMapper;

	/**
	 * 功能：平台垫资付款设置分页
	 * 作者：崔亚光
	 * 日期：2017-08-08
	 */
	public Page<PlatformPayRule> findPage(Page<PlatformPayRule> page,
			PlatformPayRule entity) {
		entity.setPage(page);
		page.setList(payRuleMapper.findList(entity));
		return page;
	}
	/**
	 * 获取平台垫资付款实体
	 * 崔亚光
	 * 2017-08-10
	 * @param id
	 * @return
	 */
	public PlatformPayRule getEntity(String id) {
		return payRuleMapper.selectByPrimaryKey(id);
	}
	/**
	 * 功能：平台垫资付款设置保存
	 * 作者：崔亚光
	 * 日期：2017-08-09
	 */
	public void save(PlatformPayRule entity) {
		if(StringUtils.isNotBlank(entity.getId())){
			 entity.setUpdateBy(UserUtils.getUser().getLoginName());
			 entity.setUpdateDate(new Date());
			payRuleMapper.updateByPrimaryKeySelective(entity);
		}
		else{
			 entity.setId(IdGen.uuid());
			 entity.setCreateBy(UserUtils.getUser().getLoginName());
			 entity.setCreateDate(new Date());
			 entity.setUpdateBy(UserUtils.getUser().getLoginName());
			 entity.setUpdateDate(new Date());
			 payRuleMapper.insert(entity);
		}
	}
	/**
	  * 删除
	  * 崔亚光
	  * 2017-08-16
	  */
	public void delete(String id) {
		payRuleMapper.deleteByPrimaryKey(id);
	}

}
