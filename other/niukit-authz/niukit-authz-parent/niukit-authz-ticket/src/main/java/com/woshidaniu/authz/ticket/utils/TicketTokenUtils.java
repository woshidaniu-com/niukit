package com.woshidaniu.authz.ticket.utils;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.time.TimeProvider;
import com.woshidaniu.authz.ticket.time.impl.DefaultTimeProvider;
import com.woshidaniu.basicutils.DateUtils;
import com.woshidaniu.basicutils.TimeUtils;
import com.woshidaniu.basicutils.URLUtils;
import com.woshidaniu.security.algorithm.DesBase64Codec;
import com.woshidaniu.security.algorithm.DesBase64Utils;

/**
 * 
 * @className	： TicketTokenUtils
 * @description	： ：系统对接接口appid和token生成和校验
 * @author 		：康康（1571）
 * @date		： 2018年5月17日 下午1:43:12
 * @version 	V1.0
 */
public class TicketTokenUtils{
	
	protected static final Logger log = LoggerFactory.getLogger(TicketTokenUtils.class);
	
	protected static final DesBase64Codec codec = new DesBase64Codec();

	private TimeProvider timeProvider = new DefaultTimeProvider();
	
	//学校代码
	private String xxdm;
	
	//时间戳过期时间
	private String timeStampPeriod;
	
	//token有效时间
	private String tokenPeriod;
	
	//时间提供者实现类
	private String timeProviderClass; 
	
	public void checkAndDisplaySelfParam() {
		
		if(this.xxdm == null || this.xxdm.equals("")) {
			throw new IllegalStateException("Ticket认证的xxdm is null");
		}else {
			log.info("Ticket认证的xxdm:"+xxdm);
		}
		
		if(this.timeStampPeriod == null || this.timeStampPeriod.equals("")) {
			throw new IllegalStateException("Ticket认证的timeStampPeriod is null");
		}else {
			log.info("Ticket认证的timeStampPeriod:"+this.timeStampPeriod);
		}
		
		if(this.tokenPeriod == null || this.tokenPeriod.equals("")) {
			throw new IllegalStateException("Ticket认证的tokenPeriod is null");
		}else {
			log.info("Ticket认证的tokenPeriod:"+this.tokenPeriod);
		}
		
		if(this.timeProviderClass == null  || this.timeProviderClass.equals("")) {
			log.info("Ticket认证未设置时间提供者,采用默认时间提供者:"+this.timeProvider);
		}else {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(this.timeProviderClass);
			} catch (ClassNotFoundException e) {
				log.error("未发现设置的时间提供者类，采用默认时间提供者:"+this.timeProvider);
				return;
			}
			if(TimeProvider.class.isAssignableFrom(clazz)) {
				try {
					TimeProvider timeProvider = (TimeProvider)clazz.newInstance();
					this.timeProvider = timeProvider;
					log.info("设置的时间提供者类:"+this.timeProvider);
				} catch (Exception e) {
					log.error("设置的时间提供者类实例化异常，采用默认时间提供者:"+this.timeProvider);
					log.error("",e);
					return;
				}				
			}else {
				log.error("设置的时间提供者类不是com.woshidaniu.authz.ticket.time.TimeProvider的子类。采用默认时间提供者:"+this.timeProvider);
			}
		}
	}
	
	/**
	 * 
	 *@描述		：根据给出的学校代码生成appid,该appid应当不管何时计算都是唯一的值
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 20164:19:07 PM
	 *@param xxdm
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String genAppid(String xxdm) {
		
		return Hex.encodeHexString(Base64.encodeBase64(codec.encrypt(xxdm).getBytes()));
	}
	
	public String genSecret(String xxdm) {
		return new String(Base64.encodeBase64(xxdm.getBytes()));
	}

	/**
	 * 
	 *@描述		：根据给出的学校代码生成token,该token应当不管何时计算都是唯一的值
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 20164:20:32 PM
	 *@param xxdm
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String genToken(String appid,String secret,String xxdm) {
		//数据库时间
		Date now = this.timeProvider.get();
		String nowStr = DateUtils.format(now, DateUtils.DATE_FORMAT_LONG);
		return DesBase64Utils.encrypt(xxdm + "-" + nowStr + "-" + UUID.randomUUID().toString());
	}
	
	/**
	 * 
	 *@描述		：
	 *@创建人		: wandalong
	 *@创建时间	: Sep 8, 201612:05:35 PM
	 *@param userid	: 用户ID
	 *@param roleid	: 角色ID，xs,js：方便区别用户角色
	 *@param timestamp ： 时间戳;格式: yyyyMMddHHmmssSSS
	 *@param token : 系统双方约定的秘钥:基于Des + Base64加密的值
	 *@return : MD5加密信息（大写）:格式为：(卡号-用户类型-时间戳-token)值组合的MD5值
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String genVerify(String userid,String roleid,String timestamp,String token) {
		token = token.contains("%") ? URLUtils.unescape(token) : token;
		return DigestUtils.md5Hex(userid + "-" + roleid + "-" + timestamp + "-" + token);
	}
	
	/**
	 * 
	 *@描述		：
	 *@创建人		: wandalong
	 *@创建时间	: Sep 8, 201612:22:26 PM
	 *@param domian : 应用访问域名地址
	 *@param token  : 系统双方约定的秘钥:基于Des + Base64加密的值
	 *@param verify : MD5加密信息（大写）:格式为：(卡号-用户类型-时间戳-token)值组合的MD5值
	 *@param userid	: 用户ID
	 *@param roleid	: 角色ID，xs,js：方便区别用户角色
	 *@param timestamp ： 时间戳;格式: yyyyMMddHHmmssSSS
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String genTicketLoginURL(String domian,String token,String verify,String userid,String roleid,String timestamp) {
		StringBuilder builder = new StringBuilder(domian);
		//应用访问路径
		builder.append("/api/login_tickitLogin.html");
		builder.append("?token=").append(token);
		builder.append("&verify=").append(verify);
		builder.append("&userid=").append(userid);
		builder.append("&roleid=").append(roleid);
		builder.append("&time=").append(timestamp);
		return builder.toString();
	}
	
	/**
	 * 
	 *@描述		：验证appid有效性
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 20169:19:26 PM
	 *@param xxdm
	 *@param appid
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean validAppid(String xxdm,String appid) {
		if(appid == null) {
			return false;
		}
		return genAppid(xxdm).equals(appid);
	}
	
	/**
	 * 
	 *@描述		：验证token有效性
	 *@创建人		: wandalong
	 *@创建时间	: Sep 7, 20164:41:48 PM
	 *@param xxdm
	 *@param token
	 *@param dbtime : 数据库时间；格式 yyyy-MM-dd HH24:mi:ss
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean validToken(String xxdm,String token) {
		if(xxdm == null || token == null) {
			return false;			
		}
		String tokenText = null;
		try {
			tokenText = codec.decrypt(token);
		} catch (Exception e) {
			try {
				tokenText = DesBase64Utils.decrypt(token);				
			}catch(Exception e1) {
				return false;
			}
		}
		String[] tokenArr = tokenText.split("-");
		if(!xxdm.equals(tokenArr[0])){
			return false;
		}
		//以下为校验token的有效期逻辑
		Date time = DateUtils.parseDate(tokenArr[1], DateUtils.DATE_FORMAT_LONG);
		//数据库时间
		Date now = this.timeProvider.get();
		//票据登录（通过握手秘钥等参数认证登录）中的token失效时间；可使用单位 (s:秒钟,m:分钟,h:小时,d:天)；如10m表示10分钟
		long period = TimeUtils.getTimeMillis(this.tokenPeriod);
		//提交过来的时间戳大于当前数据时间或超过有效期
		if( (time.getTime() > now.getTime()) || (now.getTime() - time.getTime()) > period ){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 *@描述		：验证参数有效性
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 20168:54:09 PM
	 *@param verify
	 *@param toValid : 卡号-用户类型-时间戳-token值组合
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean validVerify(String verify,String toValid) {
		if(verify == null || toValid == null) {
			return false;
		}
		return verify.toUpperCase().equals(DigestUtils.md5Hex(toValid).toUpperCase());
	}
	
	/**
	 * 
	 *@描述		：验证时间戳的有效性 
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 20169:21:32 PM
	 *@param timestamp ： 时间戳;格式: yyyyMMddHHmmssSSS
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public boolean validTimestamp(String timestamp) {
		if(timestamp == null) {
			return false;
		}
		try {
			//获取转换后的date对象
			Date time = DateUtils.parseDate(timestamp, DateUtils.DATE_FORMAT_LONG);
			long timeNumber = time.getTime();
			//数据库时间
			Date now = this.timeProvider.get();
			long nowNumber = now.getTime();
			//票据登录（通过握手秘钥等参数认证登录）中的时间戳允许时差范围；可使用单位 (s:秒钟,m:分钟,h:小时,d:天)；如1m表示1分钟
			long period = TimeUtils.getTimeMillis(this.timeStampPeriod);
			boolean last = period >= (nowNumber - timeNumber);
			//提交过来的时间戳小于当前数据时间且没有超过有效期
			if( (timeNumber < nowNumber) && last){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setXxdm(String xxdm) {
		this.xxdm = xxdm;
	}

	public void setTimeStampPeriod(String timeStampPeriod) {
		this.timeStampPeriod = timeStampPeriod;
	}

	public void setTokenPeriod(String tokenPeriod) {
		this.tokenPeriod = tokenPeriod;
	}

	public String getXxdm() {
		return xxdm;
	}

	public String getTimeStampPeriod() {
		return timeStampPeriod;
	}

	public String getTokenPeriod() {
		return tokenPeriod;
	}

	public void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public String getTimeProviderClass() {
		return timeProviderClass;
	}

	public void setTimeProviderClass(String timeProviderClass) {
		this.timeProviderClass = timeProviderClass;
	}
	
}

