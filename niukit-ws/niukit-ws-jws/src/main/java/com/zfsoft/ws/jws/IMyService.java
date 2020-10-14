package com.woshidaniu.ws.jws;

/**
 * *******************************************************************
 * @className	： IMyService
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Feb 25, 2016 12:02:25 PM
 * @version 	V1.0 
 * *******************************************************************
 */

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.woshidaniu.basemodel.UserModel;
  
@WebService  
@SOAPBinding(style = SOAPBinding.Style.RPC)  
public interface IMyService {  
      
    @WebResult(name="addResult")  
    public int add(@WebParam(name="a")int a,@WebParam(name="b")int b);  
      
    @WebResult(name="loginUser")  
    public UserModel login(@WebParam(name="username")String username,@WebParam(name="password")String password);  
}  
