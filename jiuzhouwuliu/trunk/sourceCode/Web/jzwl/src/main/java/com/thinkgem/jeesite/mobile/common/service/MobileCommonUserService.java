package com.thinkgem.jeesite.mobile.common.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.thinkgem.jeesite.common.dao.AgentsMapper;
import com.thinkgem.jeesite.common.dao.DriversMapper;
import com.thinkgem.jeesite.common.dao.GoodsownersMapper;
import com.thinkgem.jeesite.common.dao.IntegralRuleMapper;
import com.thinkgem.jeesite.common.dao.UsersIntegralRecordMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.Agents;
import com.thinkgem.jeesite.common.entity.Drivers;
import com.thinkgem.jeesite.common.entity.Goodsowners;
import com.thinkgem.jeesite.common.entity.IntegralRule;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.entity.UsersIntegralRecord;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.mobile.utils.SaveFileUtil;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Service
@Transactional
public class MobileCommonUserService {

	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private IntegralRuleMapper integralRuleMapper;
	@Autowired
	private GoodsownersMapper goodsownersMapper;
	@Autowired
	private DriversMapper driversMapper;
	@Autowired
	private AgentsMapper agentsMapper;
	@Autowired
	private UsersIntegralRecordMapper usersIntegralRecordMapper;
	
	/**
	 * @desc 用户注册
	 * @author yaotengfei
	 * @date 2017年8月10日上午11:02:33
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil userReg(HttpServletRequest request){
		String phoneno = request.getParameter("phoneno");
		String password = request.getParameter("password");
		String userSort = request.getParameter("userSort");//用户类型(0货主、1车主、2经纪人)
		//查询用户是否已注册
		Users users = usersMapper.selectByPhone(phoneno,userSort);
		if(users != null){
			return new AjaxBeanUtil("用户已注册", false); 
		}
		//判断验证码是否正确
		
		//添加注册用户
			//获取积分规则
		IntegralRule integralRule = integralRuleMapper.findEntity();
		Users user = new Users();
		user.setId(IdGen.uuid());
		user.setPhoneno(phoneno);
		user.setPassword(SystemService.entryptPassword(password));
		user.setLevel("");
		user.setExamineStatus("3");
		user.setIsTruename("0");
		user.setStatus("0");
		if(integralRule != null){
			if("0".equals(userSort)){		//货主积分
				user.setCurrentPoint(integralRule.getGoodsownerRegister());
			}else if("1".equals(userSort)){	//车主
				user.setCurrentPoint(integralRule.getDriverRegister());
			}else if("2".equals(userSort)){	//经纪人
				user.setCurrentPoint(integralRule.getAgentRegister());
			}
		}else{
			user.setCurrentPoint(0);
		}
		user.setAccountMoney(0.0);
		user.setUserSort(userSort);
		user.setIsTruename("0");
		user.setDelFlag("0");
		user.preInsert();
		usersMapper.insertSelective(user);
		//插入积分记录
		UsersIntegralRecord usersIntegralRecord = new UsersIntegralRecord();
		usersIntegralRecord.setId(IdGen.uuid());
		usersIntegralRecord.setUserid(user.getId());
		usersIntegralRecord.setIntegralDescribe("注册送积分");
		usersIntegralRecord.setIntegralScore(user.getCurrentPoint());
		usersIntegralRecord.setDelFlag("0");
		usersIntegralRecord.preAppInsert();
		usersIntegralRecordMapper.insertSelective(usersIntegralRecord);
		//根据userSort插入记录
		if("0".equals(userSort)){		//货主
			Goodsowners goodsowners = new Goodsowners();
			goodsowners.setId(IdGen.uuid());
			goodsowners.setUserId(user.getId());
			goodsowners.setIsTruecommpany("0");
			goodsowners.setIdentityType("");	
			goodsowners.setDelFlag("0");
			goodsowners.preInsert();
			goodsownersMapper.insertSelective(goodsowners);
		}else if("1".equals(userSort)){	//车主
			Drivers drivers = new Drivers();
			drivers.setId(IdGen.uuid());
			drivers.setUserid(user.getId());
			drivers.setDriverType("0");
			drivers.setIsHasCar("0");
			drivers.setIsTrueDrive("0");
			drivers.setIsTrueMove("0");
			drivers.setDelFlag("0");
			drivers.preInsert();
			driversMapper.insertSelective(drivers);
		}else if("2".equals(userSort)){	//经纪人
			Agents agents = new Agents();
			agents.setId(IdGen.uuid());
			agents.setUserid(user.getId());
			agents.setIdentityType("");
			agents.setIsTruecommpany("0");
			agents.setDelFlag("0");
			agents.preInsert();
			agentsMapper.insertSelective(agents);
		}
		return new AjaxBeanUtil("注册成功", true);
	}
	
	/**
	 * @desc 登录
	 * @author yaotengfei
	 * @date 2017年8月10日下午2:29:26
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil userLogin(HttpServletRequest request){
		String phoneno = request.getParameter("phoneno");
		String password = request.getParameter("password");
		String userSort = request.getParameter("userSort");	//用户类型(0货主、1车主、2经纪人)
		//判断用户是否注册
		Users users = usersMapper.selectByPhone(phoneno,userSort);
		if(users == null){
			return new AjaxBeanUtil("该号码未注册", false);
		}
		//判断密码是否正确
		if(!SystemService.validatePassword(password,users.getPassword())){
			return new AjaxBeanUtil("密码错误", false);
		}
		//判断用户是否被锁定
		if("1".equals(users.getStatus())){
			return new AjaxBeanUtil("该账号已被锁定", false);
		}
		//更新gps信息
		//users.setGpsInfo(gpsInfo);
		//每日首次登录送积分
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
		//获取积分规则
		IntegralRule integralRule = integralRuleMapper.findEntity();
		//获取用户当前积分
		int currentPoint = users.getCurrentPoint();
		//获取上次登录时间
		Date lastLoginTime = users.getLastLoginTime();
		if(lastLoginTime == null){
			//首次登录
			users.setCurrentPoint(currentPoint+integralRule.getUserLogin());
		}else{
			//非首次登录
			String lastLoginTimeStr = timeFormat.format(lastLoginTime);
			//获取当前时间
			String currentStr = timeFormat.format(new Date());
			if(!lastLoginTimeStr.equals(currentStr)){
				//每日首次登录送积分
				users.setCurrentPoint(currentPoint+integralRule.getUserLogin());
				//插入积分记录
				UsersIntegralRecord usersIntegralRecord = new UsersIntegralRecord();
				usersIntegralRecord.setId(IdGen.uuid());
				usersIntegralRecord.setUserid(users.getId());
				usersIntegralRecord.setIntegralDescribe("每日登录送积分");
				usersIntegralRecord.setIntegralScore(integralRule.getUserLogin());
				usersIntegralRecord.setDelFlag("0");
				usersIntegralRecord.preAppInsert();
				usersIntegralRecordMapper.insertSelective(usersIntegralRecord);
			}
		}
		//获取用户类型
		String userSortStr = DictUtils.getDictValue(users.getUserSort(), "users_user_sort", "未知");
		users.setUserSortStr(userSortStr);
		users.setLastLoginTime(new Date());
		users.preUpdate();
		usersMapper.updateByPrimaryKeySelective(users);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取用户类型Str
		String sortStr = DictUtils.getDictLabels(userSort, "users_user_sort", "未知");
		users.setUserSortStr(sortStr);
		resultMap.put("userInfo", users);
		//根据用户类型，获取相关身份信息
		if(userSort.equals("0")){
			//货主
			Goodsowners goodsowners = goodsownersMapper.findGoodsownersByUserId(users.getId());
			//获取货主身份信息
			String result = DictUtils.getDictLabels(goodsowners.getIdentityType(), "goodsowners_identity_type", "未认证");
			goodsowners.setIdentityTypeStr(result);
			resultMap.put("sortInfo", goodsowners);
		}else if(userSort.equals("1")){
			//车主
			Drivers drivers = driversMapper.findDriverByUserId(users.getId());
			resultMap.put("sortInfo", drivers);
		}else{
			//经纪人
			Agents agents = agentsMapper.findAgentsByUserId(users.getId());
			//获取经纪人身份信息
			String result = DictUtils.getDictLabels(agents.getIdentityType(), "goodsowners_identity_type", "未认证");
			agents.setIdentityTypeStr(result);
			resultMap.put("sortInfo", agents);
		}
		return new AjaxBeanUtil("success", true, resultMap);
	}
	
	/**
	 * @desc 修改密码
	 * @author yaotengfei 
	 * @date 2017年8月11日下午4:53:55
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil changePassword(HttpServletRequest request){
		String userId = request.getParameter("userId");
		String newPw = request.getParameter("newPassword");
		String oldPw = request.getParameter("oldPassword");
		//根据userId查询用户信息  
		Users users = usersMapper.selectByPrimaryKey(userId);
		if(users == null){
			return new AjaxBeanUtil("未知用户", false);
		}
		if(!SystemService.validatePassword(oldPw,users.getPassword())){
			return new AjaxBeanUtil("原密码错误", false);
		}
		if(newPw.equals(oldPw)){
			return new AjaxBeanUtil("新密码不能与原密码相同", false);
		}
		users.setPassword(SystemService.entryptPassword(newPw));
		users.preUpdate();
		usersMapper.updateByPrimaryKeySelective(users);
		return new AjaxBeanUtil("密码修改成功", true);
	}
	
	/**
	 * @desc 忘记密码
	 * @author yaotengfei
	 * @date 2017年8月21日下午3:29:56
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil forgetPassword(HttpServletRequest request){
		String phoneno = request.getParameter("phoneno");
		String newPw = request.getParameter("newPw");
		String userSort = request.getParameter("userSort");//用户类型(0货主、1车主、2经纪人)
		//验证用户是否注册
		Users users = usersMapper.selectByPhone(phoneno,userSort);
		if(users == null){
			return new AjaxBeanUtil("该号码未注册", false);
		}
		//判断验证码是否正确
		
		//密码重置
		users.preUpdate();
		users.setPassword(SystemService.entryptPassword(newPw));
		usersMapper.updateByPrimaryKeySelective(users);
		return new AjaxBeanUtil("密码修改成功", true);
	}
	
	/**
	 * @desc 修改头像
	 * @author yaotengfei
	 * @date 2017年8月11日下午5:08:31
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public AjaxBeanUtil changeHeadImg(HttpServletRequest request) throws IllegalStateException, IOException{
		String userId = request.getParameter("userId");
		//根据userId查询用户信息  
		Users users = usersMapper.selectByPrimaryKey(userId);
		if(users == null){
			return new AjaxBeanUtil("未知用户", false);
		}
		//创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();
            //图片存放路径
            String tempURL = "/upload/Img/APPUserHeadImg/";
            while(iter.hasNext()){
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                String imgPath = SaveFileUtil.saveImg(request,file,tempURL);
                users.setHeadImg(imgPath);
                users.preUpdate();
                usersMapper.updateByPrimaryKeySelective(users);
            }  
        }
        return new AjaxBeanUtil("上传成功", true, users);
	}
	
	/**
	 * 获取个人资料
	 * @date 2017-8-30
	 * @author liusiyu
	 * @param request
	 * @return
	 */
	public AjaxBeanUtil getUserInfo(HttpServletRequest request){
		String userId = request.getParameter("userId");
		try{
			Users users = usersMapper.selectByPrimaryKey(userId);
			return new AjaxBeanUtil("success",true,users);
		}catch(Exception e){
			e.printStackTrace();
			return new AjaxBeanUtil("系统错误",false);
		}
	}

	/**
	 * @description	：更新GPS位置信息
	 * @author pangchengxiang
	 * @date 2017年9月19日 上午10:19:01
	 * @return AjaxBeanUtil
	 */
	public AjaxBeanUtil setUserGpsInfo(Users user) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(user.getId())){
			user.setUpdateBy(user.getId());
			user.setUpdateDate(new Date());
			usersMapper.updateByPrimaryKeySelective(user);
			return new AjaxBeanUtil("GPS位置信息更新成功！",true);
		}
		return new AjaxBeanUtil("GPS位置信息更新失败！",false);
	}
	
}
