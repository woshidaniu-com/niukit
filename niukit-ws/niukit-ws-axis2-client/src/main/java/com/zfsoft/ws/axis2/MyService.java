package com.woshidaniu.ws.axis2;

public class MyService {  
    public String sayHello(String name,boolean isMan) {  
        if(isMan) {  
            return "Hello,Mr "+name+"! Welcome to Webservice";  
        } else {  
            return "Hello,Miss "+name+"! Welcome to Webservice";  
        }  
    }  
}  