package com.thinkgem.jeesite.common.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.CommonEntity;

public class DriverReciveMoney extends CommonEntity<DriverReciveMoney> {
	//
	private static final long serialVersionUID = 1L;

	private String id;

	private String driverOrderId;

	private Double firstReciveMoney;

	private Double firstReciveOil;

	private Date firstReciveTime;

	private Double secondReciveMoney;

	private Double secondReciveOil;

	private Date secondReciveTime;
	//非数据库字段
	private String username;//用户名
	private String truename;//姓名
	private String phoneno;//电话
	private String level;//等级
	private Double carryFee;//运费
	private Double taxFee;//税费
	private String orderNo;//订单号
	private Double totalMoney;//垫资总额
	private String isSettleAccounts;//是否结算（0-未结算，1-已结算）
	private String beginDate;//开始时间
    private String endDate;//结束时间
	private String goodsownerDealId;//货主单详情ID
	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Double getCarryFee() {
		return carryFee;
	}

	public void setCarryFee(Double carryFee) {
		this.carryFee = carryFee;
	}

	public Double getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getDriverOrderId() {
		return driverOrderId;
	}

	public void setDriverOrderId(String driverOrderId) {
		this.driverOrderId = driverOrderId == null ? null : driverOrderId.trim();
	}

	public Double getFirstReciveMoney() {
		return firstReciveMoney;
	}

	public void setFirstReciveMoney(Double firstReciveMoney) {
		this.firstReciveMoney = firstReciveMoney;
	}

	public Double getFirstReciveOil() {
		return firstReciveOil;
	}

	public void setFirstReciveOil(Double firstReciveOil) {
		this.firstReciveOil = firstReciveOil;
	}

	public Date getFirstReciveTime() {
		return firstReciveTime;
	}

	public void setFirstReciveTime(Date firstReciveTime) {
		this.firstReciveTime = firstReciveTime;
	}

	public Double getSecondReciveMoney() {
		return secondReciveMoney;
	}

	public void setSecondReciveMoney(Double secondReciveMoney) {
		this.secondReciveMoney = secondReciveMoney;
	}

	public Double getSecondReciveOil() {
		return secondReciveOil;
	}

	public void setSecondReciveOil(Double secondReciveOil) {
		this.secondReciveOil = secondReciveOil;
	}

	public Date getSecondReciveTime() {
		return secondReciveTime;
	}

	public void setSecondReciveTime(Date secondReciveTime) {
		this.secondReciveTime = secondReciveTime;
	}

	public String getIsSettleAccounts() {
		return isSettleAccounts;
	}

	public void setIsSettleAccounts(String isSettleAccounts) {
		this.isSettleAccounts = isSettleAccounts;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getGoodsownerDealId() {
		return goodsownerDealId;
	}

	public void setGoodsownerDealId(String goodsownerDealId) {
		this.goodsownerDealId = goodsownerDealId;
	}
	
}