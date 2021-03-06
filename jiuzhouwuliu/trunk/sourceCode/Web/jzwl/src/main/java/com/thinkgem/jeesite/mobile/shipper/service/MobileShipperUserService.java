package com.thinkgem.jeesite.mobile.shipper.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.thinkgem.jeesite.common.dao.GoodsownersMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.Goodsowners;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.mobile.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.mobile.utils.PinYinHeadCharUtil;
import com.thinkgem.jeesite.mobile.utils.SaveFileUtil;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Service
@Transactional
public class MobileShipperUserService {

	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private GoodsownersMapper goodsownersMapper;
	
	/**
	 * @desc 根据userid获取货主信息
	 * @author yaotengfei
	 * @date 2017年9月5日上午9:18:33
	 * @param userId
	 * @return
	 */
	public AjaxBeanUtil getUserInfoByUserId(String userId){
		Goodsowners goodsownersInfo = goodsownersMapper.findByUserId(userId);
		if(goodsownersInfo == null){
			return new AjaxBeanUtil("未知用户", false);
		}
		return new AjaxBeanUtil("success", true, goodsownersInfo);
	}
	
	/**
	 * @desc 获取货主身份
	 * @author yaotengfei
	 * @date 2017年8月21日下午5:12:27
	 * @return
	 */
	public AjaxBeanUtil getShipperIdentity(){
		//从字典缓存获取货主身份
		List<Dict> identityList = DictUtils.getDictList("goodsowners_identity_type");
		if(identityList == null){
			return new AjaxBeanUtil("缓存为空", false);
		}
		return new AjaxBeanUtil("success", true, identityList);
	}
	
	/**
	 * @desc 货主身份认证
	 * @author yaotengfei
	 * @date 2017年8月21日下午5:24:51
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public AjaxBeanUtil identityAuthentication(HttpServletRequest request) throws IllegalStateException, IOException{
		String userId = request.getParameter("userId");	//用户id
		String identityType = request.getParameter("identityType");	//身份选择
		String truename = request.getParameter("truename");	//姓名
		String cardno = request.getParameter("cardno");		//身份证号
		String companyName = request.getParameter("companyName");	//企业名称
		String cardFaceImg = null;	//身份证正面照
		String cardOtherImg = null;	//身份证反面照
		String businessLicenseFaceImg = null;	//企业营业执照正面
		String businessLicenseOtherImg = null;	//企业营业执照反面
		String legalpersonFaceImg = null;	//企业法人身份证正面照
		String legalpersonOtherImg = null;	//企业法人身份证反面照
		String imgPath = null;	//图片保存路径
		//创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();
            //图片存放路径
            String tempURL = "/upload/Img/IdentityAuthentication/GoodOwners";
        	//获取在Web服务器上的 绝对路径  
 	        String path =request.getSession().getServletContext().getRealPath("/")+tempURL;
 	        //创建文件夹
 	  		File folders=new File(path);
 	  	    if(!folders.exists() && !folders.isDirectory()){
 	  	    	folders.mkdirs();
 	  	    }
            while(iter.hasNext()){
            	//取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());
                imgPath = SaveFileUtil.saveImg(request,file,tempURL);
                if(imgPath != null){
                	switch (file.getName()) {
						case "cardFaceImg":
							cardFaceImg = imgPath;
							break;
						case "cardOtherImg":
							cardOtherImg = imgPath;
							break;
						case "businessLicenseFaceImg":
							businessLicenseFaceImg = imgPath;
							break;
						case "businessLicenseOtherImg":
							businessLicenseOtherImg = imgPath;
							break;
						case "legalpersonFaceImg":
							legalpersonFaceImg = imgPath;
							break;
						case "legalpersonOtherImg":
							legalpersonOtherImg = imgPath;
							break;
						default:
							break;
	            	}
                }
                //更新user表数据
            		//获取用户信息
                Users users = usersMapper.selectByPrimaryKey(userId);
                users.setCardno(cardno);
                users.setCardFaceImg(cardFaceImg);
                users.setCardOtherImg(cardOtherImg);
                users.setTruename(truename);
                users.setSpell(PinYinHeadCharUtil.getPinYinHeadChar(truename));
                users.preUpdate();
                usersMapper.updateByPrimaryKeySelective(users);
                //更新goodsowners表信息
                	//根据userid获取货主信息
                Goodsowners goodsowners = goodsownersMapper.findGoodsownersByUserId(users.getId());
                goodsowners.setIdentityType(identityType);
                goodsowners.setBusinessLicenseFaceImg(businessLicenseFaceImg);
                goodsowners.setBusinessLicenseOtherImg(businessLicenseOtherImg);
                goodsowners.setLegalpersonFaceImg(legalpersonFaceImg);
                goodsowners.setLegalpersonOtherImg(legalpersonOtherImg);
                goodsowners.setCompanyName(companyName);
                goodsowners.preUpdate();
                goodsownersMapper.updateByPrimaryKeySelective(goodsowners);
            }  
        }
		return new AjaxBeanUtil("认证信息已提交", true);
	}
}
