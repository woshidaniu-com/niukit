package com.woshidaniu.sms.client.def;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.sms.client.SmsClientAdapter;
import com.woshidaniu.sms.client.SmsEnum;
import com.woshidaniu.sms.client.http.HttpConnectionUtils;

/**
 * 
 *@类名称		： GreenTownSmsClient.java
 *@类描述		：绿信通短信发送接口实现
 *@创建人		：kangzhidong
 *@创建时间	：2017年8月21日 下午5:58:45
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class GreenTownSmsClient extends SmsClientAdapter {

	
	protected static Logger LOG = LoggerFactory.getLogger(GreenTownSmsClient.class);
	
	/**
	 * 短信提交地址:
	 *	短信可以一次性提交丌超过 5000 个手机号码，每个号码用英文逗号间隔。（大批量号码建议使 用 POST 方式）
	 *	URL 地址为：http://IP:PORT/sms/send
	 *	注：其中 IP:PORT 为服务部署的地址和端口。
	 *	IP 为 api.china95059.net，PORT 默讣为 8080。
	 */ 
	protected String smsurl = "http://api.china95059.net:8080/sms/send";
	/**
	 * SMS服务商提供的appid
	 */ 
	protected String appid;
	/**
	 * SMS服务商提供的appid对应的password
	 */ 
	protected String password;
	
	protected static Map<String, String> statusMap = new HashMap<String, String>();
	
	static {
		statusMap.put("0", "提交成功/查询成功");
		statusMap.put("101", "无此用户");
		statusMap.put("102", "密码错误");
		statusMap.put("103", "提交过快（提交速度超过流速限制）");
		statusMap.put("104", "系统忙（因平台侧原因，暂时无法处理提交的短信）");
		statusMap.put("105", "敏感短信（短信内容包含敏感词）");
		statusMap.put("106", "消息长度错（短信内容长度>2000 或 <=0）");
		statusMap.put("107", "包含错误的手机号码");
		statusMap.put("108", "手机号码个数错（数量>5000 或<=0）");
		statusMap.put("109", "无发送额度（该用户可用短信数已使用完）");
		statusMap.put("110", "不在发送时间内");
		statusMap.put("111", "超出该账户当月发送额度限制");
		statusMap.put("112", "attime 参数错误（小于当前时间或者格式问题）");
		statusMap.put("113", "extno 格式错误（非数字或者长度不对）");
		statusMap.put("115", "自动审核驳回（内容白名单不一致）");
		statusMap.put("116", "签名不合法（用户必须带签名的前提下）");
		statusMap.put("117", "IP 地址认证错");
		statusMap.put("118", "用户没有相应的发送权限");
		statusMap.put("119", "用户已过期");
		
	}
		
		
	
	@Override
	public String name() {
		return SmsEnum.GreenTown.getName();
	}
	
	@Override
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		super.onPreHandle(conn);
		conn.setRequestMethod(SmsEnum.GreenTown.getMethod());
	}

	/**
	 * @param content 短信内容，短信内容长度丌能超过 1000 个字。使用 URL方式编码为 UTF-8 格式。短信内容（含签名）超过 70 个字符时以长短信的格式发送（计费为 67 字/条）。
	 * @param mobile  短信接收号码，多个号码（用英文逗号隔开）视为一次群发操作; 短信可以一次性提交不超过 5000 个手机号码。（大批量号码建议使 用 POST 方式）
	 */
	@Override
	public boolean send(String content, String mobile) {

		try {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			
			paramsMap.put("name", getAppid());
			paramsMap.put("pswd", getPassword());
			paramsMap.put("mobile", mobile);
			// 必填参数。短信内容，短信内容长度丌能超过 1000 个字。使用 URL方式编码为 UTF-8 格式。短信内容（含签名）超过 70 个字符时以长短信的格式发送（计费为 67 字/条）。
			//paramsMap.put("msg", URLEncoder.encode(content, "UTF-8"));
			paramsMap.put("msg", content);
			// 可选参数。是否需要状态报告，默讣 true，true，表明需要状态报告；false 不需要状态报告
			paramsMap.put("needstatus", String.valueOf(true));
			// 可选参数。扩展码，用户定义扩展码，2 位，默讣空
			paramsMap.put("sender", "");
			// 可选参数。返回类型，默认json,暂不支持其他
			paramsMap.put("type", "json");
			// 可选参数。定时发送时间。yyyyMMddHHmm 格式
			//map.put("attime", "json");
			
			/*	esptime 必返参数。响应时间，yyyyMMddHHmmss 格式
				respstatus 必返参数。响应状态，见 1.3.3 响应状态值
				msgid 可返参数。respstatus 为 0 时才返回，13 位字符串，提供后续状态报 告匹配时使用。一次发送请求只返回一个 msgid，如 respstatus 不为 0，此参数无
			*/
			JSONObject jsonObject = HttpConnectionUtils.httpRequestWithPost(getSmsurl(), paramsMap, jsonHandler);
			String respstatus = jsonObject.getString("respstatus");
			if("0".equals(respstatus)){
				return true;
			} else {
				LOG.warn(statusMap.get(respstatus));
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return false;
	}

	public String getSmsurl() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_URL_KEY) : smsurl;
	}

	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}

	public String getAppid() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_APPID_KEY) : appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getPassword() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_PASSWORD_KEY) : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
