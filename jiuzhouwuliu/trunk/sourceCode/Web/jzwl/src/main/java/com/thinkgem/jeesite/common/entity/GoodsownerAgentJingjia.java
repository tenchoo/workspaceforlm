package com.thinkgem.jeesite.common.entity;


import com.thinkgem.jeesite.common.persistence.CommonEntity;

public class GoodsownerAgentJingjia  extends CommonEntity<GoodsownerAgentJingjia>{
    //
	private static final long serialVersionUID = 1L;

	private String id;

    private String orderId;

    private String userId;

    private String jingjiaStatus;

    private Double jingjiaPrice;

    private String jingjiaRemark;

    private String isPass;

    private String jingjiaType;
    private String truename;//用户名
    private String headImg;//用户头像
    private String userSort;//用户类型(0货主、1车主、2经纪人)
    
    //非数据库字段
    private	String truckType;//车型
    private	String truckLength;//车长
    private	String truckMaxwight;//承重	
    private	Integer transportCount;//运输次数
    private	String isCollect;//是否收藏（0-否，1-是）
    private String companyName;//企业名称
    
    private String shipperArea;//发货地址
    private String reciverArea;//收货地址
    private String goodsType;//货物类型
    private Double goodsNum;//货物数量
    private String goodsUnit;//货物单位
    private String goodsId;//货物ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getJingjiaStatus() {
        return jingjiaStatus;
    }

    public void setJingjiaStatus(String jingjiaStatus) {
        this.jingjiaStatus = jingjiaStatus == null ? null : jingjiaStatus.trim();
    }

    public Double getJingjiaPrice() {
        return jingjiaPrice;
    }

    public void setJingjiaPrice(Double jingjiaPrice) {
        this.jingjiaPrice = jingjiaPrice;
    }

    public String getJingjiaRemark() {
        return jingjiaRemark;
    }

    public void setJingjiaRemark(String jingjiaRemark) {
        this.jingjiaRemark = jingjiaRemark == null ? null : jingjiaRemark.trim();
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass == null ? null : isPass.trim();
    }

    public String getJingjiaType() {
        return jingjiaType;
    }

    public void setJingjiaType(String jingjiaType) {
        this.jingjiaType = jingjiaType == null ? null : jingjiaType.trim();
    }

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserSort() {
		return userSort;
	}

	public void setUserSort(String userSort) {
		this.userSort = userSort;
	}

	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}

	public String getTruckLength() {
		return truckLength;
	}

	public void setTruckLength(String truckLength) {
		this.truckLength = truckLength;
	}

	public String getTruckMaxwight() {
		return truckMaxwight;
	}

	public void setTruckMaxwight(String truckMaxwight) {
		this.truckMaxwight = truckMaxwight;
	}

	public Integer getTransportCount() {
		return transportCount;
	}

	public void setTransportCount(Integer transportCount) {
		this.transportCount = transportCount;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getShipperArea() {
		return shipperArea;
	}

	public void setShipperArea(String shipperArea) {
		this.shipperArea = shipperArea;
	}

	public String getReciverArea() {
		return reciverArea;
	}

	public void setReciverArea(String reciverArea) {
		this.reciverArea = reciverArea;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
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

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
    
}