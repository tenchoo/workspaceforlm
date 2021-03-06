package com.thinkgem.jeesite.mobile.driver.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.thinkgem.jeesite.common.dao.AgentOrderMapper;
import com.thinkgem.jeesite.common.dao.DriveOrderMapper;
import com.thinkgem.jeesite.common.dao.DriverLogisticsPositionMapper;
import com.thinkgem.jeesite.common.dao.GoodsMapper;
import com.thinkgem.jeesite.common.dao.GoodsownerOrderMapper;
import com.thinkgem.jeesite.common.dao.GoodsownersMapper;
import com.thinkgem.jeesite.common.dao.IntegralRuleMapper;
import com.thinkgem.jeesite.common.dao.UsersAccountOperateMapper;
import com.thinkgem.jeesite.common.dao.UsersMapper;
import com.thinkgem.jeesite.common.entity.AgentOrder;
import com.thinkgem.jeesite.common.entity.DriveOrder;
import com.thinkgem.jeesite.common.entity.DriverLogisticsPosition;
import com.thinkgem.jeesite.common.entity.Goods;
import com.thinkgem.jeesite.common.entity.GoodsownerOrder;
import com.thinkgem.jeesite.common.entity.Goodsowners;
import com.thinkgem.jeesite.common.entity.IntegralRule;
import com.thinkgem.jeesite.common.entity.Users;
import com.thinkgem.jeesite.common.entity.UsersAccountOperate;
import com.thinkgem.jeesite.common.utils.AjaxBeanUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.mobile.utils.PageParam;
import com.thinkgem.jeesite.mobile.utils.UtilDate;
import com.thinkgem.jeesite.mobile.utils.alipay.AlipayConfig;
import com.thinkgem.jeesite.mobile.utils.alipay.AlipayCore;
import com.thinkgem.jeesite.mobile.utils.wxpay.WxPayConfig;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * @description 车主订单
 * @author 文帅
 * @date 2017年9月11日 下午2:37:17
 */
@Service
@Transactional
public class MobileDriverOrderService {
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private DriveOrderMapper driveOrderMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private IntegralRuleMapper integralRuleMapper;
	@Autowired
	private UsersAccountOperateMapper usersAccountOperateMapper;
	@Autowired
	private GoodsownerOrderMapper goodsownerOrderMapper;
	@Autowired
	private GoodsownersMapper goodsownersMapper;
	@Autowired
	private AgentOrderMapper agentOrderMapper;
	@Autowired
	private DriverLogisticsPositionMapper driverLogisticsPositionMapper;

	/**
	 * @description 查询订单列表
	 * @author 文帅
	 * @date 2017年9月11日 下午3:13:09
	 * @param currentPage
	 * @param userId
	 * @param type
	 *            用于判断订单状态（当前订单-0,历史订单-1,全部订单-不传）
	 * @return
	 */
	public AjaxBeanUtil findDriverOrderList(String pageNo, String driverId, String type) {
		int currentPage = Integer.parseInt(pageNo) * PageParam.DEFAULT_PAGESIZE;
		int pageSize = PageParam.DEFAULT_PAGESIZE;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("driverId", driverId);
		paramMap.put("type", type);
		paramMap.put("currentPage", currentPage);
		paramMap.put("pageSize", pageSize);
		List<DriveOrder> list = driveOrderMapper.findDriverOrderList(paramMap);
		return new AjaxBeanUtil("查询成功", true, list);
	}

	/**
	 * @description 支付信息费
	 * @author 文帅
	 * @date 2017年9月12日 上午10:20:44
	 * @param driverOrderId
	 * @param payMethod
	 *            支付方式（1-余额，2-支付宝，3-微信）
	 * @param deposit
	 *            信息费
	 * @return
	 */
	public AjaxBeanUtil payInfoFee(String driverOrderId, String payMethod, Double deposit, String userId, HttpServletRequest request) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		DriveOrder driveOrder = driveOrderMapper.selectByPrimaryKey(driverOrderId);
		Goods goods = goodsMapper.selectByPrimaryKey(driveOrder.getId());
		if (StringUtils.isEmpty(payMethod)) {
			return new AjaxBeanUtil("请选择支付方式", false);
		}
		UsersAccountOperate usersAccountOperate = new UsersAccountOperate();
		String orderNo = UtilDate.getOrderNum();// 订单号暂用时间戳
		usersAccountOperate.setId(orderNo);
		usersAccountOperate.setUserid(user.getId());
		usersAccountOperate.setOperateType("0");
		usersAccountOperate.setOperatMoney(deposit);
		usersAccountOperate.setOrderNo(goods.getOrderNo());
		IntegralRule IntegralRule = integralRuleMapper.findEntity();
		if (IntegralRule != null) {
			usersAccountOperate.setIntegralScore((double) IntegralRule.getCompleteOrder());
		} else {
			usersAccountOperate.setIntegralScore(0.0);
		}
		usersAccountOperate.setIsDealOk("0");
		usersAccountOperate.setCreateBy(user.getTruename());
		usersAccountOperate.setCreateDate(new Date());
		usersAccountOperate.setUpdateBy(user.getTruename());
		usersAccountOperate.setUpdateDate(new Date());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("1".equals(payMethod)) {// 余额
			usersAccountOperate.setPayStyle("1");
			if (user.getAccountMoney() < deposit) {
				return new AjaxBeanUtil("账户余额不足", false);
			}
			// 插入账户记录
			usersAccountOperateMapper.insertSelective(usersAccountOperate);
			// 交付成功之后修改订单状态
			updateDriverOrder(usersAccountOperate.getId());
			return new AjaxBeanUtil("支付成功", true);
		} else if ("2".equals(payMethod)) {// 支付宝
			String orderInfo = AlipayCore.getOrderInfo("九州物流信息费支付", "九州物流", new BigDecimal("0.01"), orderNo);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			try {
				sign = URLEncoder.encode(sign, "UTF-8");
				// 插入账户记录
				usersAccountOperateMapper.insertSelective(usersAccountOperate);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String linkString = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
			System.out.println("*****************" + linkString);
			resultMap.put("result", linkString);
		} else if ("3".equals(payMethod)) {// 微信
			WxPayConfig wx = new WxPayConfig();
			String ip = request.getRemoteAddr();
			String payJson = wx.getpPepay_id(orderNo, ip, "0.01", "jiuzhouwuliu");
			System.out.println("payJson============" + payJson);
			if (payJson == null) {
				return new AjaxBeanUtil("支付回话创建失败", false);
			}
			// 插入账户记录
			usersAccountOperateMapper.insertSelective(usersAccountOperate);
			resultMap.put("result", payJson);
		}
		return new AjaxBeanUtil("支付回话创建成功", true, resultMap);
	}

	/**
	 * @description 交付信息费成功之后修改订单信息
	 * @author 文帅
	 * @date 2017年9月12日 下午3:29:56
	 * @param id
	 *            users_account_operate表中的id(相当于订单号)
	 * @return
	 */
	public AjaxBeanUtil updateDriverOrder(String id) {
		UsersAccountOperate usersAccountOperate = usersAccountOperateMapper.selectByPrimaryKey(id);
		Users user = usersMapper.selectByPrimaryKey(usersAccountOperate.getUserid());
		// 当前积分
		double currentPoint = user.getCurrentPoint() + usersAccountOperate.getIntegralScore();
		// 账户余额
		double accountMoney = user.getAccountMoney() - usersAccountOperate.getOperatMoney();
		// 更新用户账户操作表
		usersAccountOperate.setRestMoney(accountMoney);
		usersAccountOperate.setTotalScore(currentPoint);
		usersAccountOperate.setIsDealOk("1");
		usersAccountOperate.setUpdateBy(user.getTruename());
		usersAccountOperate.setUpdateDate(new Date());
		usersAccountOperateMapper.updateByPrimaryKeySelective(usersAccountOperate);
		// 修改车主订单信息
		Goods goods = goodsMapper.findGoodsByOrderNo(usersAccountOperate.getOrderNo());
		DriveOrder driveOrder = driveOrderMapper.findOrderByGoodsId(goods.getId());
		driveOrder.setPayDepositTime(new Date());
		driveOrder.setGoodsStatus("3");
		driveOrderMapper.updateByPrimaryKeySelective(driveOrder);
		// 修改users表中的信息
		user.setCurrentPoint((int) currentPoint);
		user.setAccountMoney(accountMoney);
		usersMapper.updateByPrimaryKeySelective(user);
		return new AjaxBeanUtil("修改成功", true);
	}

	/**
	 * @description 接货单
	 * @author 文帅
	 * @date 2017年9月12日 下午4:37:45
	 * @param driverOrderId
	 * @return
	 */
	public AjaxBeanUtil pickUpBill(String driverOrderId) {
		DriveOrder driveOrder = driveOrderMapper.selectByPrimaryKey(driverOrderId);
		Goods goods = goodsMapper.selectByPrimaryKey(driveOrder.getGoodsId());
		goods.setGoodsTypeStr(DictUtils.getDictLabel(goods.getGoodsType(), "goods_type", goods.getGoodsType()));
		return new AjaxBeanUtil("查询成功", true, goods);
	}

	/**
	 * @description 确认接货
	 * @author 文帅
	 * @date 2017年9月12日 下午5:39:24
	 * @param driverOrderId
	 * @return
	 */
	public AjaxBeanUtil confirmPickUp(String driverOrderId, HttpServletRequest request) {
		DriveOrder driveOrder = driveOrderMapper.selectByPrimaryKey(driverOrderId);
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			// 图片存放路径
			String tempURL = "/upload/APPGoodsImg/";
			// 获取在Web服务器上的 绝对路径
			String path = request.getSession().getServletContext().getRealPath("/") + tempURL;
			// 创建文件夹
			File folders = new File(path);
			if (!folders.exists() && !folders.isDirectory()) {
				folders.mkdirs();
			}
			int i = 1;
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 获取文件扩展名
						String tail = "";
						String[] tmps = file.getOriginalFilename().split("\\.");
						if (tmps.length > 1) {
							tail = "." + tmps[tmps.length - 1];
						}
						// 生成随机文件名
						SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
						String fileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
						// 合并文件名和扩展名
						fileName = fileName + tail;
						// 定义上传路径
						String imgPath = tempURL + fileName;
						File localFile = new File(imgPath);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (i == 1) {
							driveOrder.setReciveGoodsImg1(imgPath);
						}
						if (i == 2) {
							driveOrder.setReciveGoodsImg2(imgPath);
						}
						if (i == 3) {
							driveOrder.setReciveGoodsImg3(imgPath);
						}
						if (i == 4) {
							driveOrder.setReciveGoodsImg4(imgPath);
						}
						if (i == 5) {
							driveOrder.setReciveGoodsImg5(imgPath);
						}
						i++;
					}
				}
			}
		}
		driveOrder.setGoodsStatus("4");
		driveOrderMapper.updateByPrimaryKeySelective(driveOrder);
		return new AjaxBeanUtil("查询成功", true);
	}

	/**
	 * @description 确认送达
	 * @author 文帅
	 * @date 2017年9月13日 上午11:05:22
	 * @param driverOrderId
	 *            车主订单号
	 * @param expressNo
	 *            快递单号
	 * @param reciveGoodsName
	 *            收货人姓名
	 * @param sendDriveName
	 *            回执单车主名称
	 * @return
	 */
	public AjaxBeanUtil confirmService(String driverOrderId, String expressNo, String reciveGoodsName, String sendDriveName, HttpServletRequest request) {
		DriveOrder driveOrder = driveOrderMapper.selectByPrimaryKey(driverOrderId);
		// UserType(1经纪人、0普通货主)
		if ("0".equals(driveOrder.getUserType())) {
			GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.selectByPrimaryKey(driveOrder.getUserOrderId());
			Goodsowners goodsowners = goodsownersMapper.selectByPrimaryKey(goodsownerOrder.getGoodsownersId());
			Users user = usersMapper.selectByPrimaryKey(goodsowners.getUserId());
			if (!"C".equals(user.getLevel())) {
				if (StringUtils.isEmpty(expressNo)) {
					return new AjaxBeanUtil("请输入快递单号", false);
				}
				if (StringUtils.isEmpty(reciveGoodsName)) {
					return new AjaxBeanUtil("请输入收货人姓名", false);
				}
				if (StringUtils.isEmpty(sendDriveName)) {
					return new AjaxBeanUtil("请输入车主名称", false);
				}
			}
		} else {
			AgentOrder agentOrder = agentOrderMapper.selectByPrimaryKey(driveOrder.getUserOrderId());
			// WeituoOrFabu(1委托、0发布)
			if ("1".equals(agentOrder.getWeituoOrFabu())) {
				GoodsownerOrder goodsownerOrder = goodsownerOrderMapper.selectByPrimaryKey(agentOrder.getGoodsownerOrderid());
				Goodsowners goodsowners = goodsownersMapper.selectByPrimaryKey(goodsownerOrder.getGoodsownersId());
				Users user = usersMapper.selectByPrimaryKey(goodsowners.getUserId());
				if (!"C".equals(user.getLevel())) {
					if (StringUtils.isEmpty(expressNo)) {
						return new AjaxBeanUtil("请输入快递单号", false);
					}
					if (StringUtils.isEmpty(reciveGoodsName)) {
						return new AjaxBeanUtil("请输入收货人姓名", false);
					}
					if (StringUtils.isEmpty(sendDriveName)) {
						return new AjaxBeanUtil("请输入车主名称", false);
					}
				}
			}
		}
		driveOrder.setExpressNo(expressNo);
		driveOrder.setReciveGoodsName(reciveGoodsName);
		driveOrder.setSendDriveName(sendDriveName);
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			// 图片存放路径
			String tempURL = "/upload/APPGoodsImg/";
			// 获取在Web服务器上的 绝对路径
			String path = request.getSession().getServletContext().getRealPath("/") + tempURL;
			// 创建文件夹
			File folders = new File(path);
			if (!folders.exists() && !folders.isDirectory()) {
				folders.mkdirs();
			}
			int i = 1;
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 获取文件扩展名
						String tail = "";
						String[] tmps = file.getOriginalFilename().split("\\.");
						if (tmps.length > 1) {
							tail = "." + tmps[tmps.length - 1];
						}
						// 生成随机文件名
						SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
						String fileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
						// 合并文件名和扩展名
						fileName = fileName + tail;
						// 定义上传路径
						String imgPath = tempURL + fileName;
						File localFile = new File(imgPath);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (i == 1) {
							driveOrder.setBackImg1(imgPath);
						}
						if (i == 2) {
							driveOrder.setBackImg2(imgPath);
						}
						i++;
					}
				}
			}
		}
		driveOrder.setGoodsStatus("13");
		driveOrderMapper.updateByPrimaryKeySelective(driveOrder);
		return new AjaxBeanUtil("确认成功", true);
	}

	/**
	 * @description 根据车主订单ID查询物流信息列表
	 * @author 文帅
	 * @date 2017年9月13日 下午3:15:13
	 * @param driverOrderId
	 * @return
	 */
	public AjaxBeanUtil logisticsInfoList(String driverOrderId) {
		List<DriverLogisticsPosition> list = driverLogisticsPositionMapper.findDriverLogisticsPositionByDriverOrderId(driverOrderId);
		return new AjaxBeanUtil("查询成功", true, list);
	}
}
