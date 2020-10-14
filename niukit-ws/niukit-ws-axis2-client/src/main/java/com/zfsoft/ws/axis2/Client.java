package com.woshidaniu.ws.axis2;

import javax.xml.namespace.QName;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
  
public class Client {  
  
    public static void main(String[] args) throws Exception {  
        // 使用RPC方式调用WebService  
        RPCServiceClient serviceClient = new RPCServiceClient();  
        Options options = serviceClient.getOptions();  
        // 指定调用WebService的URL  
        EndpointReference er = new EndpointReference(  
                "http://localhost:8080/axis2/services/MyService");  
        options.setTo(er);  
  
        // 指定sayHello方法的参数值  
        Object[] opAddArgs = new Object[] { "张三", false };  
  
        // 指定sayHello方法返回值的数据类型的class对象  
        Class[] classs = new Class[] { String.class };  
  
        // 指定要调用的sayHello方法及wsdl文件的命名空间，第一个参数表示WSDL文件的命名空间  
        // 通过访问http://localhost:8080/axis2/services/MyService?wsdl 就可以看见  
        // 元素的targetNamespace属性值  
        QName qname = new QName("http://ws.apache.org/axis2", "sayHello");  
  
        // 调用sayHello方法并输出该方法的返回值  
        // 这里有三个参数的意思：1，是QName对象，表示要调用的方法名；2，webservice的参数值，参数类型是Object[]；3，返回值class对象，参数类型是Class[],  
        System.out.println(serviceClient.invokeBlocking(qname, opAddArgs,  
                classs)[0]);  
  
    }  
}  