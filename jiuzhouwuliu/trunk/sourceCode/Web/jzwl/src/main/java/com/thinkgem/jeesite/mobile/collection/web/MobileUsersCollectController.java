package com.thinkgem.jeesite.mobile.collection.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.entity.UsersCollect;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.mobile.collection.service.MobileUsersCollectService;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;

/**
 * @desc 我的收藏相关接口
 * @author 崔亚光
 * @date 2017年9月4日上午10:35:40
 */
@Controller
@RequestMapping(value="/mobileUsersCollect")
public class MobileUsersCollectController extends BaseController{

	
	@Autowired
	private MobileUsersCollectService mobileUsersCollectService;
	
	/**
	 * 测试页面
	 * 崔亚光
	 * 2017-09-04
	 */
	@RequestMapping(value="/test")
	public String test(){
		return "/mobile/test/usersCollectTest";
	}
	/**
	 * @desc 获取收藏列表
	 * @author 崔亚光
	 * @date 2017年9月4日上午10:36:40
	 * @return
	 */
	@RequestMapping(value="/list", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil list(String currentPage,String userid){
		return mobileUsersCollectService.getList(currentPage,userid);
	}
	/**
	 * 添加收藏列表
	 * 崔亚光
	 * 2017-09-14
	 */
	@RequestMapping(value="/addCollectList", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil addCollectList(String currentPage,String userid,String areaCode){
		return mobileUsersCollectService.addCollectList(currentPage,userid,areaCode);
	}
	/**
	 * 添加收藏/取消收藏
	 * 崔亚光
	 * 2017-09-14
	 */
	@RequestMapping(value="/addCollect")
	@ResponseBody
	public AjaxBeanUtil addCollect(UsersCollect record){
		return mobileUsersCollectService.addCollect(record);
	}
	/**
	 * 根据用户id查询收藏
	 * 崔亚光
	 * 2017-09-15
	 * @param collectUserid
	 * @return
	 */
	@RequestMapping(value="/getUserCollect", method= RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil getUserCollect(String userid,String collectUserid){
		return mobileUsersCollectService.getUserCollect(userid,collectUserid);
	}
}
