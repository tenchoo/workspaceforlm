package com.thinkgem.jeesite.mobile.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.mobile.common.service.MobileCommonUserService;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;

/**
 * @desc 关于用户的公用功能接口
 * @author yaotengfei
 * @date 2017年8月10日上午10:59:20
 */
@Controller
@RequestMapping(value="/MobileCommonUser")
public class MobileCommonUserController {

	@Autowired
	private MobileCommonUserService mobileCommonUserService;
	
	
	/**
	 * @description	测试页面
	 * @author 庞成祥
	 * @return
	 */
	@RequestMapping(value="goMobileCommonUserTest")
	public String goBankCardTest(){
		return "mobile/test/mobileCommonUserTest";
	}
	
	/**
	 * @desc 用户注册
	 * @author yaotengfei
	 * @date 2017年8月10日上午11:02:48
	 * @param phoneno 手机号
	 * @param password 密码
	 * @param userCode 用户提交到验证码
	 * @return
	 */
	@RequestMapping(value="/userReg", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil userReg(HttpServletRequest request){
		return mobileCommonUserService.userReg(request);
	}
	
	/**
	 * @desc 登录
	 * @author yaotengfei
	 * @date 2017年8月10日下午2:29:03
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/userLogin", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil userLogin(HttpServletRequest request){
		return mobileCommonUserService.userLogin(request);
	}
	
	/** 
	 * @desc 修改密码
	 * @author yaotengfei
	 * @date 2017年8月11日下午4:53:06
	 * @param userId -- 用户id
	 * @param newPassword -- 新密码
	 * @param oldPassword -- 旧密码
	 * @return
	 */
	@RequestMapping(value="/changePassword", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil changePassword(HttpServletRequest request){
		return mobileCommonUserService.changePassword(request);
	}
	
	/**
	 * @desc 忘记密码
	 * @author yaotengfei
	 * @date 2017年8月21日下午3:18:58
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/forgetPassword", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil forgetPassword(HttpServletRequest request){
		return mobileCommonUserService.forgetPassword(request);
	}
	
	/** 
	 * @desc 修改头像
	 * @author yaotengfei 
	 * @date 2017年8月11日下午5:07:46
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value="/changeHeadImg", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil changeHeadImg(HttpServletRequest request) throws IllegalStateException, IOException{
		return mobileCommonUserService.changeHeadImg(request);
	}
	
	/**
	 * 获取个人资料
	 * @date 2017-8-30
	 * @author liusiyu
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUserInfo", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil getUserInfo(HttpServletRequest request){
		return mobileCommonUserService.getUserInfo(request);
	}
	
	/**
	 * @description	：更新用户GPS位置信息
	 * @author pangchengxiang
	 * @date 2017年9月19日 上午10:17:56
	 * @return AjaxBeanUtil
	 */
	@RequestMapping(value="/setUserGpsInfo", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil setUserGpsInfo(Users user){
		return mobileCommonUserService.setUserGpsInfo(user);
	}
	
	/**
	 * @description	：GPS自动更新时间
	 * @author pangchengxiang
	 * @date 2017年9月19日 下午3:29:25
	 * @return String
	 */
	@RequestMapping(value="/getPositionRefreshTime")
	@ResponseBody
	@RequiresGuest
	public String getPositionRefreshTime(){
		String t = Global.getConfig("gps.userPositionRefreshTime");
		if(StringUtils.isBlank(t)){
			t="30";
		}
		return t;
	}
}
