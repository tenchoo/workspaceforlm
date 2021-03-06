package com.thinkgem.jeesite.common.entity;

import com.thinkgem.jeesite.common.persistence.CommonEntity;

public class UsersIntegralRecord extends CommonEntity<UsersIntegralRecord> {
	//
	private static final long serialVersionUID = 1L;

	private String id;

	private String userid;
	private String params;

	private String integralDescribe;

	private Integer integralScore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getUserid() {
		return userid;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params == null ? null : params.trim();
	}

	public void setUserid(String userid) {
		this.userid = userid == null ? null : userid.trim();
	}

	public String getIntegralDescribe() {
		return integralDescribe;
	}

	public void setIntegralDescribe(String integralDescribe) {
		this.integralDescribe = integralDescribe == null ? null : integralDescribe.trim();
	}

	public Integer getIntegralScore() {
		return integralScore;
	}

	public void setIntegralScore(Integer integralScore) {
		this.integralScore = integralScore;
	}

}