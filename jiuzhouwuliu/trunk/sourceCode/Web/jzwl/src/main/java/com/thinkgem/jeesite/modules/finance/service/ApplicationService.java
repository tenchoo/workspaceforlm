package com.thinkgem.jeesite.modules.finance.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.dao.UserWithdrawCashMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.UserWithdrawCash;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
/**
 * @description	提现申请管理
 * @author 文帅
 * @date 2017年8月8日 下午3:56:24
 */
@Service
@Transactional
public class ApplicationService {
	@Autowired
	private UserWithdrawCashMapper userWithdrawCashMapper;
	@Autowired
	private UsersMapper usersMapper;
	/**
	 * @description	查询提现申请列表
	 * @author 文帅
	 * @date 2017年8月8日 下午4:01:49
	 * @param page
	 * @param userWithdrawCash
	 * @return
	 */
	public Page<UserWithdrawCash> findWithdrawCashList(Page<UserWithdrawCash> page, UserWithdrawCash  userWithdrawCash) {
		userWithdrawCash.setPage(page);
		page.setList(userWithdrawCashMapper.findWithdrawCashList(userWithdrawCash));
		return page;	
	}
	
	/**
	 * @description 通过提现记录ID查询实体	
	 * @author 文帅
	 * @date 2017年8月8日 下午4:55:29
	 * @param id
	 * @return
	 */
	public UserWithdrawCash findWithdrawCashById(String id){
		return userWithdrawCashMapper.selectByPrimaryKey(id);
	}
	/**
	 * @description	通过userId查询用户实体
	 * @author 文帅
	 * @date 2017年8月8日 下午4:58:42
	 * @param id
	 * @return
	 */
	public Users findUserById(String id){
		return usersMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * @description	审核通过
	 * @author 文帅
	 * @date 2017年8月8日 下午5:50:52
	 * @param id
	 * @return
	 */
	public AjaxBeanUtil check(UserWithdrawCash userWithdrawCash,String status){
		Principal principal = UserUtils.getPrincipal();
		try {
			if("1".equals(status)){
				userWithdrawCash.setIsPass("1");
				userWithdrawCash.setUpdateDate(new Date());
				userWithdrawCash.setUpdateBy(principal.getName());
				userWithdrawCashMapper.updateByPrimaryKeySelective(userWithdrawCash);
				return new AjaxBeanUtil("审核通过", true);
			}else if("2".equals(status)){
				userWithdrawCash.setIsPass("2");
				userWithdrawCash.setUpdateDate(new Date());
				userWithdrawCash.setUpdateBy(principal.getName());
				userWithdrawCashMapper.updateByPrimaryKeySelective(userWithdrawCash);
				Users user=usersMapper.selectByPrimaryKey(userWithdrawCash.getUserid());
				//更新用户账户金额
				user.setAccountMoney(user.getAccountMoney()+userWithdrawCash.getAppayMoney());
				usersMapper.updateByPrimaryKeySelective(user);
				return new AjaxBeanUtil("审核驳回", true);
			}
			return new AjaxBeanUtil("审核成功", true);
		} catch (Exception e) {
			e.printStackTrace();
			return new AjaxBeanUtil("系统异常", false);
		}
	}
	/**
	 * 查询列表
	 * 王瀚
	 * @param users
	 * @return
	 */
	public List<UserWithdrawCash> findList( UserWithdrawCash  userWithdrawCash){
		return userWithdrawCashMapper.findList(userWithdrawCash);
	}
}
