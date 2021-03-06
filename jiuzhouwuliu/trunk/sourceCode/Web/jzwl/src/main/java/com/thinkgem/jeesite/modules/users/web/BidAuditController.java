package com.thinkgem.jeesite.modules.users.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.entity.Goods;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.users.service.BidAuditService;
import com.thinkgem.jeesite.modules.users.service.HighValuationOfGoodsService;
import com.thinkgem.jeesite.modules.users.service.UsersService;

/**
 * @desc 发布竞价审核 --Controller
 * @author 张彦学
 * @date 2017年8月11日上午10:08:48
 */
@Controller
@RequestMapping(value = "${adminPath}/users/bidAudit")
public class BidAuditController {

	@Resource
	private BidAuditService bidAuditService;
	
	@Resource
	private UsersService usersService;
	
	@Resource
	private HighValuationOfGoodsService highValuationOfGoodsService;
	
	/**
	 * @desc 发布竞价审核列表
	 * @author yaotengfei
	 * @date 2017年8月11日上午10:10:33
	 * @return
	 */
	@RequestMapping("/bidAuditList")
	public String bidAuditList(Goods goods, Model model, HttpServletRequest request, HttpServletResponse response){
		Page<Goods> page = bidAuditService.findPage(new Page<Goods>(request, response), goods);
		model.addAttribute("page", page);
		return "modules/users/bidAuditList";
	}
	
	/**
	 * @desc 审核
	 * @author yaotengfei
	 * @date 2017年8月11日下午2:25:31
	 * @return
	 */
	@RequestMapping("/check")
	public String check(Goods goods, Model model){
		Users user =null;
		if(StringUtils.isNotEmpty(goods.getUserId())){
			user = usersService.findUserById(goods.getUserId());
		}
		goods = highValuationOfGoodsService.findGoodsById(goods.getId());
		if(null!=user){
			goods.setUserName(user.getTruename());
			goods.setUserPhone(user.getPhoneno());
		}
		goods.setGoodsType(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", ""));
		goods.setNeedTruckType(DictUtils.getDictLabel(goods.getNeedTruckType(), "truck_type", ""));
		model.addAttribute("goods", goods);
		model.addAttribute("imgList", imgList(goods));
		return "modules/users/bidAuditDetail";
	}
	
	/**
	 * 保存审核结果
	 *   
	 * @author 张彦学
	 * 日期 2017年8月14日
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/checkResult")
	@ResponseBody
	public AjaxBeanUtil checkResult(Goods goods, Model model){
	
		if(goods.getCheckRemark()!=null&&goods.getCheckRemark().length()>200){
			goods.setCheckRemark(StringUtils.replaceHtml(goods.getCheckRemark()));
			return new AjaxBeanUtil("审核内容过长，200个字符以内", false);
		}
		
		bidAuditService.bidAuditCheck(goods);
		if(goods.getIsCheckPass().equals("1")){
			return new AjaxBeanUtil("审核通过成功", true);
		}
		return new AjaxBeanUtil("驳回成功", true);
	}
	
	/**
	 * 详情页面
	 *   
	 * @author 张彦学
	 * 日期 2017年8月18日
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(Goods goods, Model model){
		Users user =null;
		if(StringUtils.isNotEmpty(goods.getUserId())){
			user = usersService.findUserById(goods.getUserId());
		}
		goods = highValuationOfGoodsService.findGoodsById(goods.getId());
		if(null!=user){
			goods.setUserName(user.getTruename());
			goods.setUserPhone(user.getPhoneno());
		}
		goods.setGoodsType(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", ""));
		goods.setNeedTruckType(DictUtils.getDictLabel(goods.getNeedTruckType(), "truck_type", ""));
		model.addAttribute("goods", goods);
		model.addAttribute("imgList", imgList(goods));
		return "modules/users/bidAuditview";
	}
	private List<String> imgList(Goods goods){
		List<String> list = new ArrayList<String>();
		if(StringUtils.isNotEmpty(goods.getGoodsImg1())){
			list.add(goods.getGoodsImg1());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg2())){
			list.add(goods.getGoodsImg2());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg3())){
			list.add(goods.getGoodsImg3());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg4())){
			list.add(goods.getGoodsImg4());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg5())){
			list.add(goods.getGoodsImg5());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg6())){
			list.add(goods.getGoodsImg6());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg7())){
			list.add(goods.getGoodsImg7());
		}
		if(StringUtils.isNotEmpty(goods.getGoodsImg8())){
			list.add(goods.getGoodsImg8());
		}
		return list;
	}
}
