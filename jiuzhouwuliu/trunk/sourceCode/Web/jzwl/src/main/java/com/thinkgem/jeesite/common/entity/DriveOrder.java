package com.thinkgem.jeesite.common.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.CommonEntity;

public class DriveOrder extends CommonEntity<DriveOrder>{
    //
	private static final long serialVersionUID = 1L;

	private String id;

    private String goodsId;

    private String driversId;

    private String userOrderId;

    private String userType;

    private Double deposit;
    
    private Date payDepositTime;

	private String jingjiaStatus;

    private Double jingjiaMoney;

    private String isAssign;

    private String assignDirverId;

    private String sendDriveName;

    private String goodsStatus;

    private String reciveGoodsImg1;

    private String reciveGoodsImg2;

    private String reciveGoodsImg3;

    private String reciveGoodsImg4;

    private String reciveGoodsImg5;

    private String backImg1;

    private String backImg2;

    private String expressNo;

    private String reciveGoodsName;
    
    //非数据库字段
    private String username;//用户名
    private String truename;//车主姓名
    private String phoneno;//车主手机号
    private String userSort;//用户类型
    private String orderNo;//订单编号
    private String beginDate;//开始时间
    private String endDate;//结束时间
    private String goodsType;//货物类型
    private	String shipperArea;//发货地址
    private String shipperAreaDetail;//详细发货地址
    private String reciverArea;//收货地址
    private String reciverAreaDetail;//详细收货地址
    private String totalMoney;//总支付金额
    
    private String goodsownersName;//货主姓名
    private String goodsownersPhoneno;//货主手机号
    private String integralScore;//车主获取的积分
    
    private String needTruckType;//需求车型
    private Double goodsNum;//数量
    private String goodsUnit;//单位
    private String needTruckLength;//需求车长
    private String zcYmd;//装车日期
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getDriversId() {
        return driversId;
    }

    public void setDriversId(String driversId) {
        this.driversId = driversId == null ? null : driversId.trim();
    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId == null ? null : userOrderId.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }
    
    public Date getPayDepositTime() {
  		return payDepositTime;
  	}

  	public void setPayDepositTime(Date payDepositTime) {
  		this.payDepositTime = payDepositTime;
  	}
    
    public String getJingjiaStatus() {
        return jingjiaStatus;
    }

    public void setJingjiaStatus(String jingjiaStatus) {
        this.jingjiaStatus = jingjiaStatus == null ? null : jingjiaStatus.trim();
    }

    public Double getJingjiaMoney() {
        return jingjiaMoney;
    }

    public void setJingjiaMoney(Double jingjiaMoney) {
        this.jingjiaMoney = jingjiaMoney;
    }

    public String getIsAssign() {
        return isAssign;
    }

    public void setIsAssign(String isAssign) {
        this.isAssign = isAssign == null ? null : isAssign.trim();
    }

    public String getAssignDirverId() {
        return assignDirverId;
    }

    public void setAssignDirverId(String assignDirverId) {
        this.assignDirverId = assignDirverId == null ? null : assignDirverId.trim();
    }

    public String getSendDriveName() {
        return sendDriveName;
    }

    public void setSendDriveName(String sendDriveName) {
        this.sendDriveName = sendDriveName == null ? null : sendDriveName.trim();
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus == null ? null : goodsStatus.trim();
    }

    public String getReciveGoodsImg1() {
        return reciveGoodsImg1;
    }

    public void setReciveGoodsImg1(String reciveGoodsImg1) {
        this.reciveGoodsImg1 = reciveGoodsImg1 == null ? null : reciveGoodsImg1.trim();
    }

    public String getReciveGoodsImg2() {
        return reciveGoodsImg2;
    }

    public void setReciveGoodsImg2(String reciveGoodsImg2) {
        this.reciveGoodsImg2 = reciveGoodsImg2 == null ? null : reciveGoodsImg2.trim();
    }

    public String getReciveGoodsImg3() {
        return reciveGoodsImg3;
    }

    public void setReciveGoodsImg3(String reciveGoodsImg3) {
        this.reciveGoodsImg3 = reciveGoodsImg3 == null ? null : reciveGoodsImg3.trim();
    }

    public String getReciveGoodsImg4() {
        return reciveGoodsImg4;
    }

    public void setReciveGoodsImg4(String reciveGoodsImg4) {
        this.reciveGoodsImg4 = reciveGoodsImg4 == null ? null : reciveGoodsImg4.trim();
    }

    public String getReciveGoodsImg5() {
        return reciveGoodsImg5;
    }

    public void setReciveGoodsImg5(String reciveGoodsImg5) {
        this.reciveGoodsImg5 = reciveGoodsImg5 == null ? null : reciveGoodsImg5.trim();
    }

    public String getBackImg1() {
        return backImg1;
    }

    public void setBackImg1(String backImg1) {
        this.backImg1 = backImg1 == null ? null : backImg1.trim();
    }

    public String getBackImg2() {
        return backImg2;
    }

    public void setBackImg2(String backImg2) {
        this.backImg2 = backImg2 == null ? null : backImg2.trim();
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo == null ? null : expressNo.trim();
    }

    public String getReciveGoodsName() {
        return reciveGoodsName;
    }

    public void setReciveGoodsName(String reciveGoodsName) {
        this.reciveGoodsName = reciveGoodsName == null ? null : reciveGoodsName.trim();
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

	public String getUserSort() {
		return userSort;
	}

	public void setUserSort(String userSort) {
		this.userSort = userSort;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getShipperArea() {
		return shipperArea;
	}

	public void setShipperArea(String shipperArea) {
		this.shipperArea = shipperArea;
	}

	public String getShipperAreaDetail() {
		return shipperAreaDetail;
	}

	public void setShipperAreaDetail(String shipperAreaDetail) {
		this.shipperAreaDetail = shipperAreaDetail;
	}

	public String getReciverArea() {
		return reciverArea;
	}

	public void setReciverArea(String reciverArea) {
		this.reciverArea = reciverArea;
	}

	public String getReciverAreaDetail() {
		return reciverAreaDetail;
	}

	public void setReciverAreaDetail(String reciverAreaDetail) {
		this.reciverAreaDetail = reciverAreaDetail;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getGoodsownersName() {
		return goodsownersName;
	}

	public void setGoodsownersName(String goodsownersName) {
		this.goodsownersName = goodsownersName;
	}

	public String getGoodsownersPhoneno() {
		return goodsownersPhoneno;
	}

	public void setGoodsownersPhoneno(String goodsownersPhoneno) {
		this.goodsownersPhoneno = goodsownersPhoneno;
	}

	public String getIntegralScore() {
		return integralScore;
	}

	public void setIntegralScore(String integralScore) {
		this.integralScore = integralScore;
	}

	public String getNeedTruckType() {
		return needTruckType;
	}

	public void setNeedTruckType(String needTruckType) {
		this.needTruckType = needTruckType;
	}

	public Double getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Double goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getNeedTruckLength() {
		return needTruckLength;
	}

	public void setNeedTruckLength(String needTruckLength) {
		this.needTruckLength = needTruckLength;
	}

	public String getZcYmd() {
		return zcYmd;
	}

	public void setZcYmd(String zcYmd) {
		this.zcYmd = zcYmd;
	}
	
}