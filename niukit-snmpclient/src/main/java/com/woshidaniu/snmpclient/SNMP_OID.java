/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.snmpclient;

import org.snmp4j.smi.OID;

/**
 *@类名称	: SNMP.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Oct 8, 2016 5:15:58 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public final class SNMP_OID {

	/*------------系统参数（1.3.6.1.2.1.1）-----------------*/
	
	/**
	 * 获取系统基本信息;sysDescr 1.3.6.1.2.1.1.1
	 */
	public static final OID OID_SYSDESC = new OID("1.3.6.1.2.1.1.1");
	/**
	 * 获取系统基本信息;sysObjectID 1.3.6.1.2.1.1.2
	 */
	public static final OID OID_SYSOBJECTID = new OID("1.3.6.1.2.1.1.2");
	/**
	 * 获取系统基本信息;sysUpTime 1.3.6.1.2.1.1.3
	 */
	public static final OID OID_SYSUPTIME = new OID("1.3.6.1.2.1.1.3");
	/**
	 * 获取系统基本信息;sysContact 1.3.6.1.2.1.1.4
	 */
	public static final OID OID_SYSCONTACT = new OID("1.3.6.1.2.1.1.4");
	/**
	 * 获取系统基本信息;sysName 1.3.6.1.2.1.1.5
	 */
	public static final OID OID_SYSNAME = new OID("1.3.6.1.2.1.1.5");
	/**
	 * 获取系统基本信息;sysLocation 1.3.6.1.2.1.1.6
	 */
	public static final OID OID_SYSLOCATION = new OID("1.3.6.1.2.1.1.6");
	/**
	 * 获取系统基本信息;sysServices 1.3.6.1.2.1.1.7
	 */
	public static final OID OID_SYSSERVICES = new OID("1.3.6.1.2.1.1.7");
	
	//Interfaces Group
	
	/**
	 * ;ifNumber 1.3.6.1.2.1.2.1
	 */
	public static final OID OID_IFNUMBER = new OID("1.3.6.1.2.1.2.1");
	
	
	/*
	ifTable 1.3.6.1.2.1.2.2
	ifEntry 1.3.6.1.2.1.2.2.1
	ifIndex 1.3.6.1.2.1.2.2.1.1
	ifDescr 1.3.6.1.2.1.2.2.1.2
	ifType 1.3.6.1.2.1.2.2.1.3
	ifMtu 1.3.6.1.2.1.2.2.1.4
	ifSpeed 1.3.6.1.2.1.2.2.1.5
	ifPhysAddress 1.3.6.1.2.1.2.2.1.6
	ifAdminStatus 1.3.6.1.2.1.2.2.1.7
	ifOperStatus 1.3.6.1.2.1.2.2.1.8
	ifLastChange 1.3.6.1.2.1.2.2.1.9
	ifInOctets 1.3.6.1.2.1.2.2.1.10
	ifInUcastPkts 1.3.6.1.2.1.2.2.1.11
	ifInNUcastPkts 1.3.6.1.2.1.2.2.1.12
	ifInDiscards 1.3.6.1.2.1.2.2.1.13
	ifInErrors 1.3.6.1.2.1.2.2.1.14
	ifInUnknownProtos 1.3.6.1.2.1.2.2.1.15
	ifOutOctets 1.3.6.1.2.1.2.2.1.16
	ifOutUcastPkts 1.3.6.1.2.1.2.2.1.17
	ifOutNUcastPkts 1.3.6.1.2.1.2.2.1.18
	ifOutDiscards 1.3.6.1.2.1.2.2.1.19
	ifOutErrors 1.3.6.1.2.1.2.2.1.20
	ifOutQLen 1.3.6.1.2.1.2.2.1.21
	ifSpecific 1.3.6.1.2.1.2.2.1.22
	//IP Group
	ipForwarding 1.3.6.1.2.1.4.1
	ipDefaultTTL 1.3.6.1.2.1.4.2
	ipInReceives 1.3.6.1.2.1.4.3
	ipInHdrErrors 1.3.6.1.2.1.4.4
	ipInAddrErrors 1.3.6.1.2.1.4.5
	ipForwDatagrams 1.3.6.1.2.1.4.6
	ipInUnknownProtos 1.3.6.1.2.1.4.7
	ipInDiscards 1.3.6.1.2.1.4.8
	ipInDelivers 1.3.6.1.2.1.4.9
	ipOutRequests 1.3.6.1.2.1.4.10
	ipOutDiscards 1.3.6.1.2.1.4.11
	ipOutNoRoutes 1.3.6.1.2.1.4.12
	ipReasmTimeout 1.3.6.1.2.1.4.13
	ipReasmReqds 1.3.6.1.2.1.4.14
	ipReasmOKs 1.3.6.1.2.1.4.15
	ipReasmFails 1.3.6.1.2.1.4.16
	ipFragsOKs 1.3.6.1.2.1.4.17
	ipFragsFails 1.3.6.1.2.1.4.18
	ipFragCreates 1.3.6.1.2.1.4.19
	ipAddrTable 1.3.6.1.2.1.4.20
	ipAddrEntry 1.3.6.1.2.1.4.20.1
	ipAdEntAddr 1.3.6.1.2.1.4.20.1.1
	ipAdEntIfIndex 1.3.6.1.2.1.4.20.1.2
	ipAdEntNetMask 1.3.6.1.2.1.4.20.1.3
	ipAdEntBcastAddr 1.3.6.1.2.1.4.20.1.4
	ipAdEntReasmMaxSize 1.3.6.1.2.1.4.20.1.5
	//ICMP Group
	icmpInMsgs 1.3.6.1.2.1.5.1
	icmpInErrors 1.3.6.1.2.1.5.2
	icmpInDestUnreachs 1.3.6.1.2.1.5.3
	icmpInTimeExcds 1.3.6.1.2.1.5.4
	icmpInParmProbs 1.3.6.1.2.1.5.5
	icmpInSrcQuenchs 1.3.6.1.2.1.5.6
	icmpInRedirects 1.3.6.1.2.1.5.7
	icmpInEchos 1.3.6.1.2.1.5.8
	icmpInEchoReps 1.3.6.1.2.1.5.9
	icmpInTimestamps 1.3.6.1.2.1.5.10
	icmpInTimestampReps 1.3.6.1.2.1.5.11
	icmpInAddrMasks 1.3.6.1.2.1.5.12
	icmpInAddrMaskReps 1.3.6.1.2.1.5.13
	icmpOutMsgs 1.3.6.1.2.1.5.14
	icmpOutErrors 1.3.6.1.2.1.5.15
	icmpOutDestUnreachs 1.3.6.1.2.1.5.16
	icmpOutTimeExcds 1.3.6.1.2.1.5.17
	icmpOutParmProbs 1.3.6.1.2.1.5.18
	icmpOutSrcQuenchs 1.3.6.1.2.1.5.19
	icmpOutRedirects 1.3.6.1.2.1.5.20
	icmpOutEchos 1.3.6.1.2.1.5.21
	icmpOutEchoReps 1.3.6.1.2.1.5.22
	icmpOutTimestamps 1.3.6.1.2.1.5.23
	icmpOutTimestampReps 1.3.6.1.2.1.5.24
	icmpOutAddrMasks 1.3.6.1.2.1.5.25
	icmpOutAddrMaskReps 1.3.6.1.2.1.5.26
	//TCP Group
	tcpRtoAlgorithm 1.3.6.1.2.1.6.1
	tcpRtoMin 1.3.6.1.2.1.6.2
	tcpRtoMax 1.3.6.1.2.1.6.3
	tcpMaxConn 1.3.6.1.2.1.6.4
	tcpActiveOpens 1.3.6.1.2.1.6.5
	tcpPassiveOpens 1.3.6.1.2.1.6.6
	tcpAttemptFails 1.3.6.1.2.1.6.7
	tcpEstabResets 1.3.6.1.2.1.6.8
	tcpCurrEstab 1.3.6.1.2.1.6.9
	tcpInSegs 1.3.6.1.2.1.6.10
	tcpOutSegs 1.3.6.1.2.1.6.11
	tcpRetransSegs 1.3.6.1.2.1.6.12
	tcpConnTable 1.3.6.1.2.1.6.13
	tcpConnEntry 1.3.6.1.2.1.6.13.1
	tcpConnState 1.3.6.1.2.1.6.13.1.1
	tcpConnLocalAddress 1.3.6.1.2.1.6.13.1.2
	tcpConnLocalPort 1.3.6.1.2.1.6.13.1.3
	tcpConnRemAddress 1.3.6.1.2.1.6.13.1.4
	tcpConnRemPort 1.3.6.1.2.1.6.13.1.5
	tcpInErrs 1.3.6.1.2.1.6.14
	tcpOutRsts 1.3.6.1.2.1.6.15
	//UDP Group
	udpInDatagrams 1.3.6.1.2.1.7.1
	udpNoPorts 1.3.6.1.2.1.7.2
	udpInErrors 1.3.6.1.2.1.7.3
	udpOutDatagrams 1.3.6.1.2.1.7.4
	udpTable 1.3.6.1.2.1.7.5
	udpEntry 1.3.6.1.2.1.7.5.1
	udpLocalAddress 1.3.6.1.2.1.7.5.1.1
	udpLocalPort 1.3.6.1.2.1.7.5.1.2
	//SNMP Group
	snmpInPkts 1.3.6.1.2.1.11.1
	snmpOutPkts 1.3.6.1.2.1.11.2
	snmpInBadVersions 1.3.6.1.2.1.11.3
	snmpInBadCommunityNames 1.3.6.1.2.1.11.4
	snmpInBadCommunityUses 1.3.6.1.2.1.11.5
	snmpInASNParseErrs 1.3.6.1.2.1.11.6
	//NOT USED 1.3.6.1.2.1.11.7
	snmpInTooBigs 1.3.6.1.2.1.11.8
	snmpInNoSuchNames 1.3.6.1.2.1.11.9
	snmpInBadValues 1.3.6.1.2.1.11.10
	snmpInReadOnlys 1.3.6.1.2.1.11.11
	snmpInGenErrs 1.3.6.1.2.1.11.12
	snmpInTotalReqVars 1.3.6.1.2.1.11.13
	snmpInTotalSetVars 1.3.6.1.2.1.11.14
	snmpInGetRequests 1.3.6.1.2.1.11.15
	snmpInGetNexts 1.3.6.1.2.1.11.16
	snmpInSetRequests 1.3.6.1.2.1.11.17
	snmpInGetResponses 1.3.6.1.2.1.11.18
	snmpInTraps 1.3.6.1.2.1.11.19
	snmpOutTooBigs 1.3.6.1.2.1.11.20
	snmpOutNoSuchNames 1.3.6.1.2.1.11.21
	snmpOutBadValues 1.3.6.1.2.1.11.22
	//NOT USED 1.3.6.1.2.1.11.23
	snmpOutGenErrs 1.3.6.1.2.1.11.24
	snmpOutGetRequests 1.3.6.1.2.1.11.25
	snmpOutGetNexts 1.3.6.1.2.1.11.26
	snmpOutSetRequests 1.3.6.1.2.1.11.27
	snmpOutGetResponses 1.3.6.1.2.1.11.28
	snmpOutTraps 1.3.6.1.2.1.11.29
	snmpEnableAuthenTraps 1.3.6.1.2.1.11.30*/
	
}
