package com.thinkgem.jeesite.modules.complaints.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.dao.ComplainRecordMapper;
import com.thinkgem.jeesite.common.entity.ComplainRecord;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Service
public class AppealService {

	@Autowired
	private ComplainRecordMapper complainRecordMapper;
	
	/**
	 * @desc 申述管理列表
	 * @author yaotengfei
	 * @date 2017年8月16日下午4:05:46
	 * @param page
	 * @param complainRecord
	 * @return
	 * @throws ParseException
	 */
	public Page<ComplainRecord> findComplainRecordListForPage(Page<ComplainRecord> page, ComplainRecord complainRecord) throws ParseException {
		complainRecord.setPage(page);
		List<ComplainRecord> complainRecordList = complainRecordMapper.findComplainRecordListForPage(complainRecord);
		if(complainRecordList.size() > 0){
			//查询字典缓存获取用户类型
			for(int i = 0; i < complainRecordList.size(); i++){
				String result = DictUtils.getDictLabels(complainRecordList.get(i).getUserSort(),"users_user_sort","未知");
				complainRecordList.get(i).setUserSortStr(result);
			}
		}
		page.setCount(complainRecordList.size());
		page.setList(complainRecordList);
		return page;	
	}
}
