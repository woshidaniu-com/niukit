/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.ws.axis.handler;

import java.rmi.RemoteException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public interface InvokeHandler<T> {

	/**
	 * 对Service进行预处理
	 */
	public void handleServ(Service service);
	
	/**
	 * 对Call进行预处理
	 */
	public T handleCall(Call call,Object[] args) throws RemoteException;
	
}
