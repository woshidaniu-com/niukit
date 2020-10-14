package com.woshidaniu.snmpclient;
/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */


import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * <p>ClassName: GetOID<p>
 * <p>Description:获得本机的信息 <p>
 * @author xudp
 * @version 1.0 V
 * @createTime 2014-9-15 下午04:45:12
 */
public class GetOID {

    public static void main(String[] args) throws Exception{  
        
        try{  
            //设定CommunityTarget   
            CommunityTarget myTarget = new CommunityTarget();
            //定义远程主机的地址  
            //Address deviceAdd = GenericAddress.parse("udp:192.168.1.233/161"); 
            //定义本机的地址   
            Address localAdd = GenericAddress.parse("udp:localhost/161");
            //设定远程主机的地址 
            //myTarget.setAddress(deviceAdd); 
            //设定本地主机的地址
            myTarget.setAddress(localAdd);
            //设置snmp共同体   
            myTarget.setCommunity(new OctetString("public")); 
            //设置超时重试次数   
            myTarget.setRetries(2); 
            //设置超时的时间  
            myTarget.setTimeout(5*60);
            //设置使用的snmp版本 
            myTarget.setVersion(SnmpConstants.version2c);  
             
            //设定采取的协议   
            TransportMapping transport = new DefaultUdpTransportMapping();//设定传输协议为UDP
            //调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
            transport.listen();  
            //创建SNMP对象，用于发送请求PDU
            Snmp protocol = new Snmp(transport);  
            //创建请求pdu,获取mib   
            PDU request = new PDU(); 
            //调用的add方法绑定要查询的OID
            //request.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1")));  
            //request.add(new VariableBinding(new OID(new int[] {1,3,6,1,2,1,1,2})));
            request.add(new VariableBinding(SNMP_OID.OID_SYSDESC));
           
           
            //调用setType()方法来确定该pdu的类型
            request.setType(PDU.GETNEXT);
            //调用 send(PDU pdu,Target target)发送pdu，返回一个ResponseEvent对象
            ResponseEvent responseEvent = protocol.send(request, myTarget);  
            //通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
            PDU response=responseEvent.getResponse();  
            //输出   
            if(response != null){  
                System.out.println("request.size()="+request.size());  
                System.out.println("response.size()="+response.size());
                //通过应答pdu获得mib信息（之前绑定的OID的值），方法：VaribleBinding get(int index)
                VariableBinding vb1 = response.get(0);  
                //VariableBinding vb2 = response.get(1);  
                System.out.println("vb1:" + vb1);  
                //System.out.println("vb2:" + vb2);
                //调用close()方法释放该进程
                transport.close();  
                
                /**
                 * 输出结果：
                 * request.size()=2
                   response.size()=2
                    1.3.6.1.2.1.1.1.0 = Hardware: x86 Family 6 Model 58 Stepping 9 AT/AT COMPATIBLE - Software: Windows 2000 Version 5.1 (Build 2600 Multiprocessor Free)
                    1.3.6.1.2.1.1.2.0 = 1.3.6.1.4.1.311.1.1.3.1.1

                 */
            }  
            
          }catch(IOException e){  
              e.printStackTrace();  
          }  
        }  
}