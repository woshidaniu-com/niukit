package com.woshidaniu.socket.client;

import org.junit.Test;

public class ClientTest {

	@Test
	public void testClient(){
		Client client = new Client("127.0.0.1",11200,"GBK");
		client.sentMsg("hello~~");
		
//		StringBuilder msg = new StringBuilder();
//		//S(6)交易编号 XYDK001(代扣报盘)
//		msg.append("XYDK001");
//		msg.append(" ");
//		//S(32)批次号  8位日期（YYYYMMDD）+交易编号前4位（同上）+学校编号（002）+5位序号（同一天从00001累计）
//		msg.append("20160621XYDK00200001");
//		msg.append("");
//		//S(128)批次名称
//		msg.append("");
//		msg.append("");
		client.close();
	}
}
