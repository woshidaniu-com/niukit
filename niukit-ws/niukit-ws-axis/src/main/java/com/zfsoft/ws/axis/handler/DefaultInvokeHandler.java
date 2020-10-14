/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.ws.axis.handler;

import java.rmi.RemoteException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


public class DefaultInvokeHandler implements InvokeHandler<Object> {

	@Override
	public void handleServ(Service service) {
		
	}

	@Override
	public Object handleCall(Call call, Object[] args) throws RemoteException {
		return call.invoke(args);
	}

}
