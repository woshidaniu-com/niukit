package com.woshidaniu.ws.jws;


import java.net.MalformedURLException;  
import java.net.URL;  
  
import javax.xml.namespace.QName;  
import javax.xml.ws.Service;  
  
public class TestClient {  
  
    public static void main(String[] args){  
         try {  
             //创建访问wsdl服务地址的url  
            URL url = new URL("http://localhost:8888/ns?wsdl");  
            //通过QName指明服务的和具体信息  
             QName sname= new QName("http://service.zttc.org/","MyServiceImplService");  
             //创建服务  
             Service service = Service.create(url,sname);  
             //实现接口  
             IMyService ms =service.getPort(IMyService.class);  
             System.out.println(ms.add(12, 33));  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
    }  
}  