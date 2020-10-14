package com.woshidaniu.javamail;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.woshidaniu.javamail.conf.EmailBody;
import com.woshidaniu.javamail.impl.JavaMailClientImpl;
import com.woshidaniu.javamail.provider.EmailPropertiesProvider;
import com.woshidaniu.javamail.provider.def.DefaultEmailPropertiesProvider;

public class JavaMailClientTest {

	protected Properties props = System.getProperties();
	protected EmailPropertiesProvider propsProvider;
	protected JavaMailClientAdapter mailClient;
	
	@Before
	public void setup(){
		
		
		/*
		 *  我们是没办法使用javamail通过一般的代理服务器发送邮件的，比如下面的代码是没有效果的：
		 */
		/*Properties props = System.getProperties();
		props.setProperty("proxySet","true");
        props.setProperty("ProxyHost","192.168.155.1");
        props.setProperty("ProxyPort","1080");*/
        //或者这样，也是没用
        /*Properties props = System.getProperties();
        props.setProperty("proxySet","true");
        props.setProperty("http.proxyHost","192.168.155.1");
        props.setProperty("http.proxyPort","808");*/

		/*
		 * 不过可以通过socks网关来访问外网的email服务器，当然，前提是你安装了一个socks服务器
		 * 此处使用ccproxy代理测试
		 */
		
		// 设置代理服务器
		props.setProperty("proxySet", "true");
		props.setProperty("socksProxyHost", "10.71.19.215");
		props.setProperty("socksProxyPort", "1080");
		
		/*------------------------ JVM 网络代理设置 ------------------------*/
		
		Properties prop = System.getProperties();
		
		prop.setProperty(JVMNetProxy.JVM_NET_PROXY_ENABLE, "true");
		
		// Http网络代理
		
		// 设置http访问要使用的代理服务器的地址
		prop.setProperty(JVMNetProxy.JVM_HTTP_PROXY_HOST, "10.71.19.215");
		// 设置http访问要使用的代理服务器的端口
		prop.setProperty(JVMNetProxy.JVM_HTTP_PROXY_PORT, "808");
		 // 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		prop.setProperty(JVMNetProxy.JVM_HTTP_PROXY_NON_HOSTS, "localhost|10.71.31.*|10.71.19.*");
		prop.setProperty(JVMNetProxy.JVM_HTTP_PROXY_USER, "");
		prop.setProperty(JVMNetProxy.JVM_HTTP_PROXY_PASSWORD, "");
		
        // Socks网络代理服务器的地址与端口
 		prop.setProperty(JVMNetProxy.JVM_SOCKS_PROXY_HOST, "10.71.19.215");
 		prop.setProperty(JVMNetProxy.JVM_SOCKS_PROXY_PORT, "1080");
     		
        // 设置登陆到代理服务器的用户名和密码
        //Authenticator.setDefault(new ProxyAuthenticator("userName", "Password")); 
        
		
		//props.setProperty("mail.smtp.host", "smtp.163.com");
		//props.setProperty("mail.smtp.port", "465");
		
		
		props.setProperty(JavaMailKey.MAIL_DEBUG, "true");
		props.setProperty(JavaMailKey.MAIL_HOST, "smtp.163.com");
		props.setProperty(JavaMailKey.MAIL_PORT, "25");
		props.setProperty(JavaMailKey.MAIL_USER, "xxx@163.com");
		props.setProperty(JavaMailKey.MAIL_PASSWORD, "xxx");
		
		props.setProperty(JavaMailKey.MAIL_SMTP_AUTH, "true");
		// props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		// props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// props.setProperty("mail.smtp.socketFactory.port", "465");
		
		// props.put("mail.store.protocol", "pop3");
		// props.put("mail.transport.protocol", "smtp");
		
		mailClient = new JavaMailClientImpl();
		
		
		propsProvider = new DefaultEmailPropertiesProvider();
		
		propsProvider.setProps(props);;
		
		mailClient.setPropsProvider(propsProvider);
		
		
	}
	
	@Test
	public void testDelete() throws Exception{
		
		//构建邮件对象
		EmailBody email = new EmailBody();
		
		// 设置发件人信息
		email.setFrom("xxx大学-消息中心", props.getProperty(JavaMailKey.MAIL_USER), false);

		// 设置收件人的邮箱
		String sendTo = "xxx@qq.com";
		email.setMailto(sendTo, sendTo);
		
		// 设置邮件标题
		email.setSubject("邮件客户端测试");
        // 设置邮件的内容体
        String content = "如果您收到此邮件说明测试成功。";
        email.setContent(content);
		
        mailClient.sendMime(email);
		
		
	}
	
	@After
	public void shutdown(){
		
	}
	 
}
