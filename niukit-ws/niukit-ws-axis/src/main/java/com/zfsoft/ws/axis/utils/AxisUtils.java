package com.woshidaniu.ws.axis.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.woshidaniu.ws.axis.ParamObject;
import com.woshidaniu.ws.axis.handler.InvokeHandler;

public abstract class AxisUtils {

	//实例化Service对象
	protected static Service service = new Service();
	
	public static Object invoke(String targetURL, QName optName,boolean usesoap,QName returnType,ParamObject ... params) throws ServiceException, MalformedURLException, RemoteException{
		//创建调用对象
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(targetURL));
		call.setOperationName(optName);
		//设置参数
		Object[] args = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			ParamObject paramModel = params[i];
			//组织参数
			args[i] = paramModel.getValue();
			//设置远程调用接口类型
			call.addParameter(paramModel.getName(), paramModel.getXmlType(), paramModel.getMode());
		}
		call.setReturnType(returnType);
		call.setUseSOAPAction(usesoap);
		//执行调用，并返回结果
		return call.invoke(args);
	}
	
	public static <T> T invoke(String targetURL,InvokeHandler<T> handler,ParamObject ... params) throws ServiceException, MalformedURLException, RemoteException{
		//创建调用对象
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(targetURL));
		//设置参数
		Object[] args = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			ParamObject paramModel = params[i];
			//组织参数
			args[i] = paramModel.getValue();
			//设置远程调用接口类型
			call.addParameter(paramModel.getName(), paramModel.getXmlType(), paramModel.getMode());
		}
		//执行调用，并返回结果
		return handler.handleCall(call,args);
	}
	
}
