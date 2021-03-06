package com.thinkgem.jeesite.modules.finance;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.CommonEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * @description	开票费导出模板
 * @author 文帅
 * @date 2017年8月14日 上午11:12:12
 */
public class ExportGoodsBilling extends CommonEntity<ExportGoodsBilling>{

		/**
		 *   
		 * @author 文帅
		 * 日期 2017年8月14日
		 */
		private static final long serialVersionUID = -6743994415978427142L;
		@ExcelField(title="开票单位", align=2, sort=1)
		private String companyName;//开票单位
		  
		@ExcelField(title="订单编号", align=2, sort=2)
		private String orderNo;//订单编号
		  
		@ExcelField(title="金额", align=2, sort=3)
		private Double payMoney;//金额
		
		@ExcelField(title="货主", align=2, sort=4)
	    private String truename;//货主
	    
		@ExcelField(title="货物类型", align=2, sort=5)
	    private String goodsType;//货物类型
	    
		@ExcelField(title="时间", align=2, sort=6)
	    private String time;//时间
	  
		@ExcelField(title="开票金额", align=2, sort=7)
	    private Double billingMoney;//开票金额
		
		@ExcelField(title="发票编号", align=2, sort=8)
		private String billingNo;//发票编号
		
		@ExcelField(title="是否已结算", align=2, sort=9)
		private String isSettleAccounts;//是否已结算
		
		@ExcelField(title="是否已开票", align=2, sort=10)
	    private String isHasBill;//是否已开票

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public Double getPayMoney() {
			return payMoney;
		}

		public void setPayMoney(Double payMoney) {
			this.payMoney = payMoney;
		}

		public String getTruename() {
			return truename;
		}

		public void setTruename(String truename) {
			this.truename = truename;
		}

		public String getGoodsType() {
			return goodsType;
		}

		public void setGoodsType(String goodsType) {
			this.goodsType = goodsType;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public Double getBillingMoney() {
			return billingMoney;
		}

		public void setBillingMoney(Double billingMoney) {
			this.billingMoney = billingMoney;
		}

		public String getBillingNo() {
			return billingNo;
		}

		public void setBillingNo(String billingNo) {
			this.billingNo = billingNo;
		}

		public String getIsHasBill() {
			return isHasBill;
		}

		public void setIsHasBill(String isHasBill) {
			this.isHasBill = isHasBill;
		}

		public String getIsSettleAccounts() {
			return isSettleAccounts;
		}

		public void setIsSettleAccounts(String isSettleAccounts) {
			this.isSettleAccounts = isSettleAccounts;
		}
		
}
