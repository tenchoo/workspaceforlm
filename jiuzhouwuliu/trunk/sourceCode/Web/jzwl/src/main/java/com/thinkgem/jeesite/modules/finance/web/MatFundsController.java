package com.thinkgem.jeesite.modules.finance.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.entity.DriverReciveMoney;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.modules.finance.entity.ExportDriverReciveMoney;
import com.thinkgem.jeesite.modules.finance.service.MatFundsService;

/**
 * @description	垫资管理
 * @author 文帅
 * @date 2017年8月7日 下午2:56:51
 */
@Controller
@RequestMapping(value="${adminPath}/finance/matFunds")
public class MatFundsController {
	@Autowired
	private MatFundsService matFundsService;
	/**
	 * @description	跳转到垫资管理列表页面
	 * @author 文帅
	 * @date 2017年8月7日 下午2:18:10
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(HttpServletRequest request,HttpServletResponse response,Model model,DriverReciveMoney driverReciveMoney) {
		Page<DriverReciveMoney> page = matFundsService.findMatFundsList(new Page<DriverReciveMoney>(request, response), driverReciveMoney);
		double xjyf=0;//小计运费
		double xjsf=0;//小计税费
		double xjFirstDz=0;//小计第一次垫资
		double xjFirstYk=0;//小计第一次油卡
		double xjSecondDz=0;//小计第二次垫资
		double xjSecondYk=0;//小计第二次油卡
		double xjTotal=0;//小计垫资总额
		for(DriverReciveMoney d:page.getList()){
			xjyf+=d.getCarryFee();
			xjsf+=d.getTaxFee();
			xjFirstDz+=d.getFirstReciveMoney();
			xjFirstYk+=d.getFirstReciveOil();
			xjSecondDz+=d.getSecondReciveMoney();
			xjSecondYk+=d.getSecondReciveOil();
			xjTotal+=d.getTotalMoney();
		}
		model.addAttribute("xjyf",xjyf);
		model.addAttribute("xjsf",xjsf);
		model.addAttribute("xjFirstDz",xjFirstDz);
		model.addAttribute("xjFirstYk",xjFirstYk);
		model.addAttribute("xjSecondDz",xjSecondDz);
		model.addAttribute("xjSecondYk",xjSecondYk);
		model.addAttribute("xjTotal",xjTotal);
		model.addAttribute("page",page);
		return "modules/finance/matFundsList";
	}
	
	/**
	 * @description	跳转到结算详情页面
	 * @author 文帅
	 * @date 2017年8月7日 下午4:55:17
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"goSettlementDetail", ""})
	public String goSettlementDetail(HttpServletRequest request,String id) {
		DriverReciveMoney driverReciveMoney=matFundsService.findById(id);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String DateStr=sdf.format(driverReciveMoney.getCreateDate());
		String DateSub=DateStr.substring(5, 7);
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		int year=Integer.parseInt(sdf2.format(driverReciveMoney.getCreateDate()));
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){//是闰年
			if("02".equals(DateSub)){
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-29");
			}else if("04".equals(DateSub)||"06".equals(DateSub)||"09".equals(DateSub)||"11".equals(DateSub)){
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-30");
			}else{
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-31");
			}
		}else{//不是闰年
			if("02".equals(DateSub)){
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-28");
			}else if("04".equals(DateSub)||"06".equals(DateSub)||"09".equals(DateSub)||"11".equals(DateSub)){
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-30");
			}else{
				driverReciveMoney.setBeginDate(sdf1.format(driverReciveMoney.getCreateDate())+"-01");
				driverReciveMoney.setEndDate(sdf1.format(driverReciveMoney.getCreateDate())+"-31");
			}
		}
		request.setAttribute("driverReciveMoney", driverReciveMoney);
		return "modules/finance/matSettlementDetail";
	}
	
	/**
	 * @description	根据垫资ID进行批量结算
	 * @author 文帅
	 * @date 2017年8月11日 下午3:20:36
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="jiesuan",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil jiesuan(HttpServletRequest request,String ids){
		return matFundsService.plJieSuan(ids);
	}
	
	/**
	 * @description	导出
	 * @author 文帅
	 * @date 2017年8月15日 上午10:14:23
	 * @param driverReciveMoney
	 * @param response
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/export")
	public String export(DriverReciveMoney driverReciveMoney,HttpServletResponse response,HttpServletRequest request, RedirectAttributes redirectAttributes){
		List<DriverReciveMoney> driverReciveMoneyList= matFundsService.findList(driverReciveMoney);
		List<ExportDriverReciveMoney> list=new ArrayList<ExportDriverReciveMoney>();
		double xjyf=0;//小计运费
		double xjsf=0;//小计税费
		double xjFirstDz=0;//小计第一次垫资
		double xjFirstYk=0;//小计第一次油卡
		double xjSecondDz=0;//小计第二次垫资
		double xjSecondYk=0;//小计第二次油卡
		double xjTotal=0;//小计垫资总额
		for(DriverReciveMoney d:driverReciveMoneyList){
			xjyf+=d.getCarryFee();
			xjsf+=d.getTaxFee();
			xjFirstDz+=d.getFirstReciveMoney();
			xjFirstYk+=d.getFirstReciveOil();
			xjSecondDz+=d.getSecondReciveMoney();
			xjSecondYk+=d.getSecondReciveOil();
			xjTotal+=d.getTotalMoney();
			ExportDriverReciveMoney exportDriverReciveMoney=new ExportDriverReciveMoney();
			exportDriverReciveMoney.setUsername(d.getUsername());
			exportDriverReciveMoney.setTruename(d.getTruename());
			exportDriverReciveMoney.setPhoneno(d.getPhoneno());
			exportDriverReciveMoney.setLevel(d.getLevel());
			exportDriverReciveMoney.setCarryFee(StringUtils.formatNumberToString(d.getCarryFee(), "0.00"));
			exportDriverReciveMoney.setTaxFee(StringUtils.formatNumberToString(d.getTaxFee(), "0.00"));
			exportDriverReciveMoney.setFirstReciveMoney(StringUtils.formatNumberToString(d.getFirstReciveMoney(), "0.00"));
			exportDriverReciveMoney.setFirstReciveOil(StringUtils.formatNumberToString(d.getFirstReciveOil(), "0.00"));
			exportDriverReciveMoney.setSecondReciveMoney(StringUtils.formatNumberToString(d.getSecondReciveMoney(), "0.00"));
			exportDriverReciveMoney.setSecondReciveOil(StringUtils.formatNumberToString(d.getSecondReciveOil(), "0.00"));
			exportDriverReciveMoney.setTotalMoney(StringUtils.formatNumberToString(d.getTotalMoney(), "0.00"));
			exportDriverReciveMoney.setOrderNo(d.getOrderNo());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");  
			String DateStr=sdf.format(d.getCreateDate());
			exportDriverReciveMoney.setTime(DateStr);
			if("0".equals(d.getIsSettleAccounts())){
				exportDriverReciveMoney.setIsSettleAccounts("未结算");
			}else if("1".equals(d.getIsSettleAccounts())){
				exportDriverReciveMoney.setIsSettleAccounts("已结算");
			}
			list.add(exportDriverReciveMoney);
		}
		try {
			ExportDriverReciveMoney d = new ExportDriverReciveMoney();
			d.setUsername("");
			d.setTruename("小计");
			d.setPhoneno("");
			d.setLevel("");
			d.setCarryFee(StringUtils.formatNumberToString(xjyf, "0.00"));
			d.setTaxFee(StringUtils.formatNumberToString(xjsf, "0.00"));
			d.setFirstReciveMoney(StringUtils.formatNumberToString(xjFirstDz, "0.00"));
			d.setFirstReciveOil(StringUtils.formatNumberToString(xjFirstYk, "0.00"));
			d.setSecondReciveMoney(StringUtils.formatNumberToString(xjSecondDz, "0.00"));
			d.setSecondReciveOil(StringUtils.formatNumberToString(xjSecondYk, "0.00"));
			d.setTotalMoney(StringUtils.formatNumberToString(xjTotal, "0.00"));
			d.setOrderNo("");
			d.setTime("");
			d.setIsSettleAccounts("");
			list.add(d);
			new ExportExcel("垫资管理", ExportDriverReciveMoney.class, 2).setDataList(list).write(response, "垫资管理.xlsx").dispose();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect: ${adminPath}/finance/matFunds/list";
	}
	
	/**
	 * @description	详情结算
	 * @author 文帅
	 * @date 2017年8月16日 下午5:22:30
	 * @param driverReciveMoney
	 * @return
	 */
	@RequestMapping(value="detailJieSuan",method=RequestMethod.POST)
	@ResponseBody
	public AjaxBeanUtil detailJieSuan(DriverReciveMoney driverReciveMoney){
		return matFundsService.detailJieSuan(driverReciveMoney);
	}
}
