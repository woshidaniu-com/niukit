package com.woshidaniu.basicutils.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("static-access")
public final class InetAddressUtils {
	
	static InetAddress inet = null;
	static{
		try {
			inet = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static String getHostAddressIp() throws UnknownHostException{
		System.out.println("当前IP地址:" + inet.getHostAddress());
		return inet.getHostAddress();
	}
	
	public static String getCanonicalHostName() throws UnknownHostException{
		System.out.println("当前CanonicalHostName:" + inet.getCanonicalHostName());
		return inet.getCanonicalHostName();
	}
	
	public static String getHostName() throws UnknownHostException{
		System.out.println("当前HostName:" + inet.getHostName());
		return inet.getHostName();
	}
	
	public static InetAddress getLocalHost() throws UnknownHostException{
		return inet.getLocalHost();
	}
	
	public static byte[] getAddress() throws UnknownHostException{
		System.out.println("当前Address:" + inet.getAddress());
		return inet.getAddress();
	}
	
	public static InetAddress getByName(String host) throws UnknownHostException{
		return inet.getByName(host);
	}
	
	public static InetAddress[] getAllByName(String host) throws UnknownHostException{
		return inet.getAllByName(host);
	}
	
	public static InetAddress getByAddress(byte[] addr) throws UnknownHostException{
		return inet.getByAddress(addr);
	}
	
	public static InetAddress getByAddress(String host,byte[] addr) throws UnknownHostException{
		return inet.getByAddress(host, addr);
	}
	
	/**
	 * 
	 *@描述：获取网卡信息 Map<网卡名称,网卡信息>
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-23下午05:17:17
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@return
	 */
	public static Map<String,InetAddress> getNetworkAdapter() {
		Map<String,InetAddress> map = new HashMap<String, InetAddress>();
		try {
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
	        while (enumeration.hasMoreElements()) {
	            NetworkInterface networkInterface = enumeration.nextElement();
	         
	            if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && networkInterface.isUp()) {
	                System.out.println(networkInterface.getDisplayName());
	                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
	                while (addressEnumeration.hasMoreElements()) {
	                    System.out.println("\t" + addressEnumeration.nextElement());
	                    map.put(networkInterface.getDisplayName(), addressEnumeration.nextElement());
	                }
	            }
	        }
		} catch (SocketException e) {
        	System.err.println("Error when getting host ip address: <{}>." + e.getMessage());
        }
		return map;
	}
	
	/**
	 * 
	 *@描述：获取网卡信息List<网卡信息>
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-23下午05:16:56
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@return
	 */
	public static List<InetAddress> getAddressList() {
    	List<InetAddress> addressList = null;
        try {
        	addressList = new ArrayList<InetAddress>();
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && networkInterface.isUp()) {
                    System.out.println(networkInterface.getDisplayName());
                    Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                    while (addressEnumeration.hasMoreElements()) {
                        System.out.println("\t" + addressEnumeration.nextElement());
                        addressList.add(addressEnumeration.nextElement());
                    }
                }
            }
            
        } catch (SocketException e) {
        	System.err.println("Error when getting host ip address: <{}>." + e.getMessage());
        }
        return null;
    }
	
	/*
     * 对于InetAddress的测试
     * 关键词：主机名、主机别名、ip地址、ipv4、ipv6
     * InetAddress是一个不可变对象
     * InetAddress是一个对象，包含3个属性：主机名称、主机别名、主机ip
     * 一个ip对应唯一一个主机名，一个主机名可以对应多个ip(一台计算机上有多个网络地址)，一个域名可以对应多个ip（一个web服务有多台机器做负载均衡），一个ip也可以对应多个域名（多个web服务器在同一台机器上）
     * 一般的主机别名就是：网站的域名，为了便于记忆 ,DNS通过主机别名或者主机名都能找到相应的IP
     * 
     * @param args
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("对于InetAddress.getByName(host)  InetAddress.getAllByName(host) 的测试");
        //InetAddress.getByName(host)就相当于InetAddress.getAllByName(host)返回的数组取第一个,所以先只说InetAddress.getAllByName(host)
        //InetAddress.getAllByName(host)的参数可以是：域名（别名）、主机名、ip地址（ipv4：192.168.1.235  或者 ipv6）
        
        /*使用域名称来构造InetAddress ，例如：www.google.com
         * 1、构造时就需要连接网络，查询DNS,网络不通则报异常，无法创建成功
         * 2、此时域名显示的是就是域名，主机名显示的是真正的主机名称 。
         * 3、因为一个域名对应多个ip，所以会有多个InetAddress对象
         */
        InetAddress[] googleAddressList=InetAddress.getAllByName("www.google.com");
        for(InetAddress add:googleAddressList)
            System.out.println("ip地址是："+add.getHostAddress()+" 域名(主机别名)是："+add.getHostName()+" 主机名是："+add.getCanonicalHostName());
        
        /*
         * 使用主机名来构造InetAddress ，例如hx-in-f147.1e100.net（google一台服务器的名称）
         * 1、同上面的测试一样：构造时就需要连接网络，查询DNS，网络不通则报异常，无法创建成功
         * 2、此时主机别名和主机名都显示的是hx-in-f147.1e100.net
         * 3、对应着唯一的一个ip地址
         */
        InetAddress[] googleReallyHostAddressList=InetAddress.getAllByName("hx-in-f147.1e100.net");
        for(InetAddress add:googleReallyHostAddressList)
            System.out.println("ip地址是："+add.getHostAddress()+" 域名(主机别名)是："+add.getHostName()+" 主机名是："+add.getCanonicalHostName());
        
      /*
       * 使用ip地址来来构造InetAddress
       * 1、此时构造时不需要联网查询DNS
       * 2、此时的主机名 和 主机别名 均为空：InetAddress的canonicalHostName 和 hostName 均采用延迟加载的方式，当外界调用相应的get方法时，才取查询DNS获得
       * 3、若网络不通，即无法查询DNS，则使用该ip地址作为主机名和主机别名
       */
        InetAddress[] ipAddressList=InetAddress.getAllByName("74.125.71.99");
        for(InetAddress add:ipAddressList)
            System.out.println("ip地址是："+add.getHostAddress()+" 域名(主机别名)是："+add.getHostName()+" 主机名是："+add.getCanonicalHostName());
        
        System.out.println("InetAddress.getByAddress(addr) 的测试;");
        /*
         * 这个同InetAddress.getAllByName() ，使用ip来构造是一样的 。
         */
        InetAddress byteAddress=InetAddress.getByAddress(ipAddressList[0].getAddress());
        System.out.println("ip地址是："+byteAddress.getHostAddress()+" 域名(主机别名)是："+byteAddress.getHostName()+" 主机名是："+byteAddress.getCanonicalHostName());
        /*
         * 使用主机别名和ip来构造一个InetAddress，但是不检查相应的域名是否能解析，ip是否存在能连接。
         * 调用getCanonicalHostName()时候需要联网  。
         */
        InetAddress allInetAddress=InetAddress.getByAddress("hx-in-f147.1e100.net", ipAddressList[0].getAddress());
        System.out.println("ip地址是："+allInetAddress.getHostAddress()+" 域名(主机别名)是："+allInetAddress.getHostName()+" 主机名是："+allInetAddress.getCanonicalHostName());
        
     
		try {
			
			System.out.println(InetAddressUtils.getCanonicalHostName());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
