package com.thinkgem.jeesite.modules.users.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.entity.DriverMasterChange;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.users.service.CaptainTransferService;

/**
 * 车队长身份转让审核
 *   
 * @author 张彦学
 * 日期 2017年8月11日
 */
@Controller
@RequestMapping(value = "${adminPath}/users/captainTransfer")
public class CaptainTransferController {
	@Resource
	private CaptainTransferService captainTransferService;
	/**
	 * 车队长转让列表
	 *   
	 * @author 张彦学
	 * 日期 2017年8月11日
	 * @return
	 */
	@RequestMapping(value = "/captainTransferList")
		public String captainTransferList(DriverMasterChange driverMasterChange, Model model,
				HttpServletRequest request, HttpServletResponse response){
				Page<DriverMasterChange> page = captainTransferService.findPage(new Page<DriverMasterChange>(request, response), driverMasterChange);
				model.addAttribute("page", page);
			
		    return "modules/users/captainTransferList";
	}
	/**
	 * 转让 审核页面
	 */
	@RequestMapping(value = "/captainTransferDetail")
	public String captainTransferDetail(String id,Model model,String checkOut){
		DriverMasterChange driverMasterChange = captainTransferService.selectDetail(id);
		driverMasterChange.setReciverAreaDetail(DictUtils.getDictLabel(driverMasterChange.getReciverAreaDetail(), "car_level", driverMasterChange.getReciverAreaDetail()));
		driverMasterChange.setNeedTruckType(DictUtils.getDictLabel(driverMasterChange.getNeedTruckType(), "car_level", driverMasterChange.getNeedTruckType()));
		model.addAttribute("driverMasterChange", driverMasterChange);
		model.addAttribute("checkOut", checkOut);
		return "modules/users/captainTransferDetail";
	}
	/**
	 * 审核
	 * @return 
	 */
	@RequestMapping(value = "/captainTransferCheck")
	@ResponseBody
	public AjaxBeanUtil checkResult(DriverMasterChange driverMasterChange,Model model){
		if(StringUtils.isNotEmpty(driverMasterChange.getAuditingRemark())){
			if(driverMasterChange.getAuditingRemark().length()>=100){
				return new AjaxBeanUtil("审核失败，审核意见不能超过一百个字符。", false);
			}
		}
		captainTransferService.bidAuditCheck(driverMasterChange);
		if(driverMasterChange.getAuditingStatus().equals("1")){
			return new AjaxBeanUtil("审核通过成功", true);
		}
		return new AjaxBeanUtil("驳回成功", true);
	}
}
