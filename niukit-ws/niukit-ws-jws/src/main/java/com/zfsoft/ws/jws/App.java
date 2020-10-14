package com.woshidaniu.ws.jws;

import java.io.IOException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
/**
 * 
 *@类名称	: App.java
 *@类描述	：1.客户端调用（wximport自动生成代码 【推荐】）
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 5:06:49 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class App {
	/**
     * 通过wsimport 解析wsdl生成客户端代码调用WebService服务
     * 
     * @param args
	 * @throws IOException 
     * 
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        
      /*  *//**
         * <service name="MyService">
         * 获得服务名称
         *//*
        MyService mywebService = new MyService();
        
        *//**
         * <port name="HelloServicePort" binding="tns:HelloServicePortBinding">
         *//*
        HelloService hs = mywebService.getHelloServicePort();*/
        
        /**
         * 调用方法
         
        System.out.println(hs.sayGoodbye("sjk"));*/
        //System.out.println(hs.aliassayHello("sjk"));
        

        /**
         * 通过客户端编程的方式调用Webservice服务
         *
         */
        URL wsdlUrl = new URL("http://192.168.1.100:6789/hello?wsdl");
        Service s = Service.create(wsdlUrl, new QName("http://ws.itcast.cn/","HelloServiceService"));
        HelloService2 hs2 = s.getPort(new QName("http://ws.itcast.cn/","HelloServicePort"), HelloService2.class);
        String ret = hs2.sayHello("zhangsan");
        System.out.println(ret);
    }
}
