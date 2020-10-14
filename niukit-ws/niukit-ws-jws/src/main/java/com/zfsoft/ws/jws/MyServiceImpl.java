package com.woshidaniu.ws.jws;


import javax.jws.WebParam;
import javax.jws.WebService;

import com.woshidaniu.basemodel.UserModel;
  
@WebService(endpointInterface="com.woshidaniu.service.IMyService")  
public class MyServiceImpl implements IMyService {  
  
    @Override  
    public int add(int a, int b) {  
        System.out.println(a + "+" + b + "=" + (a + b));  
        return a+b;  
    }  
  
    @Override  
    public UserModel login(@WebParam(name = "username") String username,  
            @WebParam(name = "password") String password) {  
        System.out.println(username + " is logging");  
        UserModel user = new UserModel();
        user.setUserID("1");
        user.setUserName(username);  
        user.setUserPassword(password);
        return user;  
          
    }  
}  