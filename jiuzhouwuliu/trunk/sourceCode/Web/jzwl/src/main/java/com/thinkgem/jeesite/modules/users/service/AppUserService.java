/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.users.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.dao.AgentsMapper;
import com.thinkgem.jeesite.common.dao.DriversMapper;
import com.thinkgem.jeesite.common.dao.GoodsownersMapper;
import com.thinkgem.jeesite.common.dao.IntegralRuleMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.Agents;
import com.thinkgem.jeesite.common.entity.Drivers;
import com.thinkgem.jeesite.common.entity.Goodsowners;
import com.thinkgem.jeesite.common.entity.IntegralRule;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.users.util.AppUserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class AppUserService extends BaseService {
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private IntegralRuleMapper integralRuleMapper;
	@Autowired
	private GoodsownersMapper goodsownersMapper;
	@Autowired
	private DriversMapper driversMapper;
	@Autowired
	private AgentsMapper agentsMapper;
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private SessionDAO sessionDao;

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public Users getUser(String id) {
		return AppUserUtils.selectByPrimaryKey(id, false);
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public Users getUserByPhone(String phone, String userSort, boolean isNewLoad) {
		Users users = AppUserUtils.selectByPhone(phone, userSort, isNewLoad);
		return users;
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	public Collection<Session> getActiveSessions() {
		return sessionDao.getActiveSessions(false);
	}

	/**
	 * @description ：获取用户登录信息
	 * @author pangchengxiang
	 * @date 2017年9月13日 下午3:56:48
	 * @return AjaxBeanUtil
	 */
	@Transactional(readOnly = false)
	public AjaxBeanUtil getUserLoginInfo() {
		// TODO Auto-generated method stub
		Users users = AppUserUtils.getUser(false);
		if(null==users){
			return new AjaxBeanUtil("该号码未注册", false);
		}
		// 每日首次登录送积分
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 获取积分规则
		IntegralRule integralRule = integralRuleMapper.findEntity();
		// 获取用户当前积分
		int currentPoint = users.getCurrentPoint();
		// 获取上次登录时间
		Date lastLoginTime = users.getLastLoginTime();
		if (lastLoginTime == null) {
			// 首次登录
			users.setCurrentPoint(currentPoint + integralRule.getUserLogin());
		} else {
			// 非首次登录
			String lastLoginTimeStr = timeFormat.format(lastLoginTime);
			// 获取当前时间
			String currentStr = timeFormat.format(new Date());
			if (!lastLoginTimeStr.equals(currentStr)) {
				// 每日首次登录送积分
				users.setCurrentPoint(currentPoint + integralRule.getUserLogin());
			}
		}
		// 获取用户类型
		String userSortStr = DictUtils.getDictValue(users.getUserSort(), "users_user_sort", "未知");
		users.setUserSortStr(userSortStr);
		users.setLastLoginTime(new Date());
		users.preAppUpdate();
		usersMapper.updateByPrimaryKeySelective(users);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取用户类型Str
		String sortStr = DictUtils.getDictLabels(users.getUserSort(), "users_user_sort", "未知");
		users.setUserSortStr(sortStr);
		resultMap.put("userInfo", users);
		// 根据用户类型，获取相关身份信息
		if ("0".equals(users.getUserSort())) {
			// 货主
			Goodsowners goodsowners = goodsownersMapper.findGoodsownersByUserId(users.getId());
			// 获取货主身份信息
			String result = DictUtils.getDictLabels(goodsowners.getIdentityType(), "goodsowners_identity_type", "未认证");
			goodsowners.setIdentityTypeStr(result);
			resultMap.put("sortInfo", goodsowners);
		} else if ("1".equals(users.getUserSort())) {
			// 车主
			Drivers drivers = driversMapper.findDriverByUserId(users.getId());
			resultMap.put("sortInfo", drivers);
		} else if ("2".equals(users.getUserSort())){
			// 经纪人
			Agents agents = agentsMapper.findAgentsByUserId(users.getId());
			// 获取经纪人身份信息
			String result = DictUtils.getDictLabels(agents.getIdentityType(), "goodsowners_identity_type", "未认证");
			agents.setIdentityTypeStr(result);
			resultMap.put("sortInfo", agents);
		}
		return new AjaxBeanUtil("success", true, resultMap);
	}

}
