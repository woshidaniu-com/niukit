/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.basicutils.DateUtils;

public class TicketTokenUtilsTest {

	private TicketTokenUtils ticketTokenUtils = new TicketTokenUtils();
	
	private String xxdm = "10427";// 10427,00000
	private String userid = "000020063586";
	private String roleid = "js";
	private String domain = "http://10.71.33.192:8080/jwglxt";
	
	@Before
	public void init() throws Exception{
        
        this.ticketTokenUtils.setXxdm("");
        this.ticketTokenUtils.setTokenPeriod("2m");
        this.ticketTokenUtils.setTimeStampPeriod("2m");
	}
	
	@Test
	public void test() {

		String appid = ticketTokenUtils.genAppid(xxdm);
		String secret = ticketTokenUtils.genSecret(xxdm);

		// 获得token
		System.out.println("appid:" + appid);
		System.out.println("secret:" + secret);

		// 验证token
		String token = ticketTokenUtils.genToken(appid, secret, xxdm);
		System.out.println("token:" + token);
		
		String timestamp = DateUtils.format(new Date(), DateUtils.DATE_FORMAT_LONG);

		String verify = ticketTokenUtils.genVerify(userid, roleid, timestamp, token);
		System.out.println("verify:" + verify);

		System.out.println("validAppid:" + ticketTokenUtils.validAppid(xxdm, appid));

		System.out.println("validToken:" + ticketTokenUtils.validToken(xxdm, token));
		System.out.println("validTimestamp:" + ticketTokenUtils.validTimestamp(timestamp));

		System.out.println("ticketLoginURL: "
				+ ticketTokenUtils.genTicketLoginURL(domain, token, verify, userid, roleid, timestamp));
	}
}
