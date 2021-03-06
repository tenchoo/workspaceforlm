package com.thinkgem.jeesite.mobile.utils.wxpay;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;

/**
 * @description	微信支付
 * @author 文帅
 * @date 2017年8月31日 下午5:32:43
 */
public class WxPayConfig {

	/**
	 * 微信提供给商户的服务接入网关URL(新)
	 */
	public static String url = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 统一下单															// 接口链接

	public static String appid ="wxb0de33354456148e";// 微信分配的公众账号ID

	public static String mch_id = "1483167482";// 微信支付分配的商户号

	public static String prepayid = "";// 微信返回的支付交易会话ID

	public static String packages = "Sign=WXPay";// 微信扩展字段
	
	public static String timestamp = String.valueOf(genTimeStamp());// 请求时的时间戳

	public static String sign = "";// 生成的签名

	public static String key = "cyjysoft2017062318039330813cyjys";// 密钥
							
	public static String body = "九州物流交易支付";// 商品或支付单简要描述
	public static String out_trade_no = "";// 商户系统内部的订单号,32个字符内、可包含字母

	public static int total_fee = 1; // 订单总金额，单位为分
	
	public static String spbill_create_ip = "122.114.100.238";// APP和网页支付提交用户端ip

	public static String notify_url = "http://stranger.uicp.net/eschl/reportName/weixinPayReturn";// 接收微信支付异步通知回调地址
	public static String trade_type = "APP";
	
	/**
	 * 生成签名
	 */
	public static String getSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WxPayConfig.key);
		return DigestUtils.md5Hex(sb.toString().getBytes()).toUpperCase();
//		return MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
	}
	
	/**
	 * 生成签名
	 */
	public static String getSignTwo(List<NameValuePair> params){
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		String newSb=sb.toString().substring(0, sb.length()-1);
		System.out.println("需要生成的字符串"+newSb);
		return DigestUtils.md5Hex(newSb.getBytes()).toUpperCase();
	}
	
	/**
	 * 时间戳
	 * 
	 * @return
	 */
	public static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	/**
	 * 获取随机字符串，不长于32位
	 * 
	 * @return
	 */
	public static String genNonceStr() {
		Random random = new Random();
		String nonceStr = DigestUtils.md5Hex(String.valueOf(
				random.nextInt(10000)).getBytes()).toUpperCase();
		return nonceStr;
	}
	
	/**
	 * 获取订单号随机字符串，不长于32位
	 * 
	 * @return
	 */
	public static String genOutTradNo() {
		Random random = new Random();
		return DigestUtils.md5Hex(String.valueOf(random.nextInt(10000))
				.getBytes());
	}
	
	/**
	 * 生成xml用于去请求微信 统一下单
	 * 
	 * @param params
	 * @return
	 */
	public static String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	/**
	 * 解析xml
	 * 
	 * @param content
	 * @return map
	 * @throws DocumentException
	 */
	public static Map<String, String> decodeXml(String content)
			throws Exception {
		// 解析结果存储在HashMap
		Map<String, String> map = new HashMap<String, String>();
		Document document = DocumentHelper.parseText(content);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	/**
	 * 获取统一下单接口返回的微信支付回话id
	 * @param orderSn
	 * @param ip
	 * @param total_fee
	 * @return str
	 */
	public String getpPepay_id(String orderSn,String ip,String total_fee,String description){
		List<NameValuePair> nameValuePairsSign = new ArrayList<NameValuePair>();
		nameValuePairsSign.add(new NameValuePair("appid", appid));
		nameValuePairsSign.add(new NameValuePair("body", description));
		nameValuePairsSign.add(new NameValuePair("mch_id", mch_id));
		nameValuePairsSign.add(new NameValuePair("nonce_str", genNonceStr()));
		nameValuePairsSign.add(new NameValuePair("notify_url", notify_url));
		nameValuePairsSign.add(new NameValuePair("out_trade_no", genOutTradNo()));
		nameValuePairsSign.add(new NameValuePair("spbill_create_ip", ip));
		BigDecimal bTotal_fee = new BigDecimal(0.01).setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
		String total_feeStr= bTotal_fee.multiply(new BigDecimal(100)).toString();//转元为分
		int dLength= total_feeStr.indexOf(".");
		nameValuePairsSign.add(new NameValuePair("total_fee",total_feeStr.substring(0,dLength)));
		nameValuePairsSign.add(new NameValuePair("trade_type", trade_type));
		nameValuePairsSign.add(new NameValuePair("sign", getSign(nameValuePairsSign)));
		// 开始发送请求
		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// 创建post请求方法
		PostMethod myPost = new PostMethod(url);
		// 设置请求超时时间
		client.setConnectionTimeout(300 * 1000);
		String responseString = null;
		try {
			// 设置请求头部类型
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			// 设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
			myPost.setRequestBody(toXml(nameValuePairsSign));
			myPost.setRequestEntity(new StringRequestEntity(
					toXml(nameValuePairsSign), "text/xml", "utf-8"));
			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK){
				BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				responseString = new String(strByte, 0, strByte.length, "utf-8");
				Map<String, String> map= decodeXml(responseString);
				bos.close();
				bis.close();
				return map.get("prepay_id")==null? null: getJsonStr(map.get("prepay_id"));//生成json调起支付
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成需要的支付json str
	 * @param prepay_id
	 * @return json str
	 */
	public static String getJsonStr(String prepay_id){
		List<NameValuePair> nameValuePairsSign = new ArrayList<NameValuePair>();
		nameValuePairsSign.add(new NameValuePair("appid", appid));
		String noncestr=genNonceStr();
		nameValuePairsSign.add(new NameValuePair("noncestr", noncestr));
		nameValuePairsSign.add(new NameValuePair("package", "Sign=WXPay"));
		nameValuePairsSign.add(new NameValuePair("partnerid", mch_id));
		nameValuePairsSign.add(new NameValuePair("prepayid", prepay_id));
		long timestamp= genTimeStamp();
		nameValuePairsSign.add(new NameValuePair("timestamp",String.valueOf(timestamp)));
		Map<String, String> jsonMap = new HashMap<String, String>();//开始生成json
		jsonMap.put("appid", appid);
		jsonMap.put("noncestr", noncestr);
		jsonMap.put("package", packages);
		jsonMap.put("partnerid", mch_id);
		jsonMap.put("prepayid", prepay_id);
		jsonMap.put("timestamp", String.valueOf(timestamp));
		jsonMap.put("sign", getSign(nameValuePairsSign));
		String jsonObject = JSONObject.toJSONString(jsonMap);
		return jsonObject;
	}
	
}
