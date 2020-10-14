package com.woshidaniu.ws.jws;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
 
@WebService 
public class Jdkwebservice { 

    public String doSomething(@WebParam(name="value", targetNamespace = "http://demo/", mode = WebParam.Mode.IN)String value) { 

        return "Just do it," + value + "!"; 

    } 

     

    public static void main(String[] args) { 

        Endpoint.publish("http://localhost:8080/jdkwsdemo/demo.JdkWebService", new Jdkwebservice()); 

    } 

} 