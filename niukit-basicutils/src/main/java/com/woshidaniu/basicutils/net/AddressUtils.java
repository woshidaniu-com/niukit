package com.woshidaniu.basicutils.net;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Convenience utilities for simplifying common address parsing.
 */
public class AddressUtils {

	/**
	 * Split a string in the form of
	 * "host1:port1,host2:port2 host3:port3,host4:port4" into a Map of
	 * InetSocketAddress instances suitable for instantiating a
	 * MemcachedClient,map's key is the main memcached node,and value is the
	 * standby node for main node. Note that colon-delimited IPv6 is also supported. For
	 * example: ::1:11211
	 * 
	 * @param s
	 * @return
	 */
	public static Map<InetSocketAddress, InetSocketAddress> getAddressMap(
			String s) {
		if (s == null) {
			throw new NullPointerException("Null host list");
		}
		if (s.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in list:  ``" + s
					+ "''");
		}
		s=s.trim();
		Map<InetSocketAddress, InetSocketAddress> result = new LinkedHashMap<InetSocketAddress, InetSocketAddress>();
		for (String hosts : s.split(" ")) {
			String[] nodes = hosts.split(",");

			if (nodes.length < 1) {
				throw new IllegalArgumentException("Invalid server ``" + hosts
						+ "'' in list:  " + s);
			}
			String mainHost = nodes[0].trim();
			InetSocketAddress mainAddress = getInetSocketAddress(s, mainHost);
			if (nodes.length >= 2) {
				InetSocketAddress standByAddress = getInetSocketAddress(s,
						nodes[1].trim());
				result.put(mainAddress, standByAddress);
			} else {
				result.put(mainAddress, null);
			}

		}
		assert !result.isEmpty() : "No addrs found";
		return result;
	}

	private static InetSocketAddress getInetSocketAddress(String s,
			String mainHost) {
		int finalColon = mainHost.lastIndexOf(':');
		if (finalColon < 1) {
			throw new IllegalArgumentException("Invalid server ``" + mainHost
					+ "'' in list:  " + s);

		}
		String hostPart = mainHost.substring(0, finalColon).trim();
		String portNum = mainHost.substring(finalColon + 1).trim();

		InetSocketAddress mainAddress = new InetSocketAddress(hostPart, Integer
				.parseInt(portNum));
		return mainAddress;
	}

	/**
	 * Split a string in the form of "host:port host2:port" into a List of
	 * InetSocketAddress instances suitable for instantiating a MemcachedClient.
	 * 
	 * Note that colon-delimited IPv6 is also supported. For example: ::1:11211
	 */
	public static List<InetSocketAddress> getAddresses(String s) {
		if (s == null) {
			throw new NullPointerException("Null host list");
		}
		if (s.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in list:  ``" + s
					+ "''");
		}
		s = s.trim();
		ArrayList<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();

		for (String hoststuff : s.split(" ")) {
			int finalColon = hoststuff.lastIndexOf(':');
			if (finalColon < 1) {
				throw new IllegalArgumentException("Invalid server ``"
						+ hoststuff + "'' in list:  " + s);

			}
			String hostPart = hoststuff.substring(0, finalColon).trim();
			String portNum = hoststuff.substring(finalColon + 1).trim();

			addrs
					.add(new InetSocketAddress(hostPart, Integer
							.parseInt(portNum)));
		}
		assert !addrs.isEmpty() : "No addrs found";
		return addrs;
	}

	public static InetSocketAddress getOneAddress(String server) {
		if (server == null) {
			throw new NullPointerException("Null host");
		}
		if (server.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in:  ``" + server
					+ "''");
		}
		server = server.trim();
		int finalColon = server.lastIndexOf(':');
		if (finalColon < 1) {
			throw new IllegalArgumentException("Invalid server ``" + server
					+ "''");

		}
		String hostPart = server.substring(0, finalColon).trim();
		String portNum = server.substring(finalColon + 1).trim();
		return new InetSocketAddress(hostPart, Integer.parseInt(portNum));
	}
	
	public static Map<InetSocketAddress, InetSocketAddress> getAddressMap(List<InetSocketAddress> addressList) {
		Map<InetSocketAddress, InetSocketAddress> addressMap = new LinkedHashMap<InetSocketAddress, InetSocketAddress>();
		if (addressList != null) {
			for (InetSocketAddress addr : addressList) {
				addressMap.put(addr, null);
			}
		}
		return addressMap;
	}
	
}
