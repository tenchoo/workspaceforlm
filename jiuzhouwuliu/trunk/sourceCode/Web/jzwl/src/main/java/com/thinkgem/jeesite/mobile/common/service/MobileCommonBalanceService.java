package com.thinkgem.jeesite.mobile.common.service;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.mobile.utils.alipay.AlipayCore;
import com.thinkgem.jeesite.mobile.utils.UtilDate;
import com.thinkgem.jeesite.mobile.utils.alipay.AlipayConfig;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.common.dao.UserWithdrawCashMapper;
import com.thinkgem.jeesite.common.dao.UsersAccountOperateMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.UserWithdrawCash;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.entity.UsersAccountOperate;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.IdGen;



@Service
@Transactional
/**
 * 余额 -- Service
 * @author liusiyu
 * @date 2017-9-4
 */
public class MobileCommonBalanceService {
	@Resource
	private UsersAccountOperateMapper usersAccountOperateMapper;
	
	@Resource
	private UsersMapper usersMapper;
	
	@Resource
	private UserWithdrawCashMapper userWithdrawCashMapper;
	
	/**
	 * 获取最新提现&充值记录
	 * @author liusiyu
	 * @date 2017-9-5
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil getUserLateBalance(HttpServletRequest request){
		String userId = request.getParameter("userId");
		UsersAccountOperate Withdraw = usersAccountOperateMapper.findLateWithdrawCashInfo(userId);
		UsersAccountOperate recharge = usersAccountOperateMapper.findLateRechargeInfo(userId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("withdraw",Withdraw);
		resultMap.put("recharge", recharge);
		if(Withdraw == null && recharge == null){
			return new AjaxBeanUtil("暂无数据",false);
		}
		return new AjaxBeanUtil("success",true,resultMap);
	}
	
	/**
	 * 获取用户消费/充值操作记录
	 * @author liusiyu
	 * @date 2017-9-4
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil findAccountOperateList(HttpServletRequest request){
		String userId = request.getParameter("userId");
		List<UsersAccountOperate> result = usersAccountOperateMapper.findInfoListByUserId(userId);
		if(result.size() == 0){
			return new AjaxBeanUtil("暂无更多数据",false);
		}
		//从字典获取支付方式，钱包操作类型
		for(int i = 0; i < result.size(); i++){
			String payStyleStr = DictUtils.getDictLabel(result.get(i).getPayStyle(), "goodsowner_recive_money_pay_style", "未知");
			result.get(i).setPayStyleStr(payStyleStr);
			String operateTypeStr = DictUtils.getDictLabel(result.get(i).getOperateType(), "users_account_operate_type", "未知");
			result.get(i).setOperateTypeStr(operateTypeStr);
		}
		return new AjaxBeanUtil("success",true,result);
	}
	
	/**
	 * 用户充值
	 * @author liusiyu
	 * @date 2017-9-5
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil userRecharged(HttpServletRequest request){
		String userId = request.getParameter("userId");//用户id
		String bankId = request.getParameter("bankId");//银行卡id
		Double operatMoney = Double.valueOf(request.getParameter("operatMoney"));//充值金额
		String payStyle = request.getParameter("payStyle");//支付方式
		UsersAccountOperate usersAccountOperate = null;
		Users users = null;
		String linkString = null;
		Double integralScore = Double.valueOf("3");//获得的积分
		Double restMoney,totalScore;
		users = usersMapper.selectByPrimaryKey(userId);
//		List<UsersAccountOperate> resultList = usersAccountOperateMapper.findInfoListByUserId(userId);
		String orderNum = UtilDate.getOrderNum();//充值订单号暂用时间戳
		usersAccountOperate = new UsersAccountOperate();
		usersAccountOperate.setId(IdGen.uuid());
		usersAccountOperate.setUserid(userId);
		usersAccountOperate.setCreateDate(new Date());
		usersAccountOperate.setCreateBy(users.getTruename());
		usersAccountOperate.setUpdateDate(new Date());
		usersAccountOperate.setUpdateBy(users.getTruename());
		usersAccountOperate.setOrderNo(orderNum);
		usersAccountOperate.setDelFlag("0");
		usersAccountOperate.setBankCardNo(bankId);
		usersAccountOperate.setIntegralScore(integralScore);
		usersAccountOperate.setOperatMoney(operatMoney);
		usersAccountOperate.setOperateType("2");//类型：2--充值
		usersAccountOperate.setPayStyle(payStyle);
		restMoney = users.getAccountMoney()+operatMoney;//充值后的余额
		totalScore = users.getCurrentPoint()+integralScore;//增加后的总积分
		usersAccountOperate.setTotalScore(totalScore);
		usersAccountOperate.setRestMoney(restMoney);
		users.setAccountMoney(restMoney);
		//支付
		Map<String, String> resultMap = new HashMap<String, String>();
		if("01".equals(payStyle)){
			//支付宝支付							//new BigDecimal(orderAmount)
			String orderInfo = AlipayCore.getOrderInfo("九州物流支付", "九州物流", new BigDecimal("0.01"), orderNum);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			try {
				sign = URLEncoder.encode(sign, "UTF-8");
				//增加充值表记录
				usersAccountOperateMapper.insertSelective(usersAccountOperate);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			linkString = orderInfo + "&sign=\"" + sign + "\"&"+"sign_type=\"RSA\"";
			System.out.println("*****************"+linkString);
			resultMap.put("linkString", linkString);
			usersAccountOperateMapper.insertSelective(usersAccountOperate);
			users.setCurrentPoint(Integer.parseInt(totalScore.toString()));
			usersMapper.updateByPrimaryKey(users);
			return new AjaxBeanUtil("充值成功",true,resultMap);
		}
		return new AjaxBeanUtil("充值失败",false);
	}
	
	/**
	 * 用户提现
	 * @author liusiyu
	 * @date 2017-9-12
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil userWithdrawCash(HttpServletRequest request){
		String userId = request.getParameter("userId");
		Double appayMoney = Double.valueOf(request.getParameter("appayMoney"));
		String bankCardId = request.getParameter("bankCardId");
		Users users = usersMapper.selectByPrimaryKey(userId);
		UserWithdrawCash userWithdrawCash = new UserWithdrawCash();
//		List<UsersAccountOperate> resultList = usersAccountOperateMapper.findInfoListByUserId(userId);
		if(users.getAccountMoney() == 0){
			return new AjaxBeanUtil("无可提现余额",false);
		}
		if(users.getAccountMoney()-appayMoney < 0){
			return new AjaxBeanUtil("超额提现",false);
		}
		userWithdrawCash.setId(IdGen.uuid());
		userWithdrawCash.setUserid(userId);
		userWithdrawCash.setAppayMoney(appayMoney);
		userWithdrawCash.setBankCardId(bankCardId);
		userWithdrawCash.setIsPass("0");
		userWithdrawCash.setDelFlag("0");
		userWithdrawCash.setUpdateDate(new Date());
		userWithdrawCash.setUpdateBy(users.getTruename());
		userWithdrawCash.setCreateDate(new Date());
		userWithdrawCash.setCreateBy(users.getTruename());
		userWithdrawCashMapper.insertSelective(userWithdrawCash);
		users.setAccountMoney(users.getAccountMoney()-appayMoney);
		usersMapper.updateByPrimaryKey(users);
		return new AjaxBeanUtil("申请提现成功",true,users);
	}
}
