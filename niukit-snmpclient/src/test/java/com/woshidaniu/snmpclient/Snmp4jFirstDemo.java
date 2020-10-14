package com.woshidaniu.snmpclient;
/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */



import java.io.IOException;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Snmp4jFirstDemo {

    private Snmp snmp = null;
    private int version ;

    public Snmp4jFirstDemo(int version) {
        try {
            this.version = version;
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            if (version == SnmpConstants.version3) {
                // 设置安全模式
                USM usm = new USM(SecurityProtocols.getInstance(),new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
            }
            // 开始监听消息
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Boolean syn, final Boolean bro, PDU pdu, String addr)
            throws IOException {
        // 生成目标地址对象
        Address targetAddress = GenericAddress.parse(addr);
        Target target = null;
        if (version == SnmpConstants.version3) {
            // 添加用户
            snmp.getUSM().addUser(new OctetString("MD5DES"),new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,new OctetString("MD5DESUserAuthPassword"),PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
            target = new UserTarget();
            // 设置安全级别
            ((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
            ((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
            target.setVersion(SnmpConstants.version3);
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                ((CommunityTarget) target).setCommunity(new OctetString("public"));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString("public"));
            }

        }
        // 目标对象相关设置
        target.setAddress(targetAddress);
        target.setRetries(5);
        target.setTimeout(1000);

        if (!syn) {
            // 发送报文 并且接受响应
            ResponseEvent response = snmp.send(pdu, target);
            // 处理响应
            System.out.println("Synchronize(同步) message(消息) from(来自) "
                    + response.getPeerAddress() + "\r\n"+"request(发送的请求):"
                    + response.getRequest() + "\r\n"+"response(返回的响应):"
                    + response.getResponse());
            /**
             * 输出结果：
             * Synchronize(同步) message(消息) from(来自) 192.168.1.233/161
                request(发送的请求):GET[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = Null]]
                response(返回的响应):RESPONSE[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = WIN-667H6TS3U37]]

             */
        } else {
            // 设置监听对象
            ResponseListener listener = new ResponseListener() {

                public void onResponse(ResponseEvent event) {
                    if (bro.equals(false)) {
                        ((Snmp) event.getSource()).cancel(event.getRequest(),this);
                    }
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println("Asynchronise(异步) message(消息) from(来自) "
                            + event.getPeerAddress() + "\r\n"+"request(发送的请求):" + request
                            + "\r\n"+"response(返回的响应):" + response);
                }

            };
            // 发送报文
            snmp.send(pdu, target, null, listener);
        }
    }

    public static void main(String[] args) {
        //Snmp的三个版本号
        //int ver3 = SnmpConstants.version3;
        int ver2c = SnmpConstants.version2c;
        //int ver1 = SnmpConstants.version1;
        Snmp4jFirstDemo manager = new Snmp4jFirstDemo(ver2c);
        // 构造报文
        PDU pdu = new PDU();
         //PDU pdu = new ScopedPDU();
        // 设置要获取的对象ID，这个OID代表远程计算机的名称
        OID oids = new OID("1.3.6.1.2.1.1.5.0");
        pdu.add(new VariableBinding(oids));
        // 设置报文类型
        pdu.setType(PDU.GET);
        //((ScopedPDU) pdu).setContextName(new OctetString("priv"));
        try {
            // 发送消息 其中最后一个是想要发送的目标地址
            //manager.sendMessage(false, true, pdu, "udp:192.168.1.229/161");//192.168.1.229 Linux服务器
            manager.sendMessage(false, true, pdu, "udp:192.168.1.233/161");//192.168.1.233 WinServer2008服务器
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}