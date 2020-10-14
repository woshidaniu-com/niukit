
# web应用 配置 

> 依赖包

<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
</dependency>
<!-- JCIFS 是一个纯Java语言编写，基于CIFS/SMB网络协议的开源客户端  -->
<dependency>
	<groupId>org.samba.jcifs</groupId>
	<artifactId>jcifs</artifactId>
</dependency>
<dependency>
	<groupId>${project.groupId}</groupId>
	<artifactId>fastkit-basicutils</artifactId>
	<version>${project.version}</version>
</dependency>

> web.xml 配置

<!--共享文件请求过滤器 -->
<filter>    
	<filter-name>smbResource</filter-name>    
	<filter-class>com.woshidaniu.smbclient.web.SMBResourceRequestFilter</filter-class>  
	<!-- 文件本地存储路径 -->
    <init-param>
    	<param-name>smb.tmpdir</param-name>
    	<param-value>tmpdir</param-value>
    </init-param>
    <!-- 是否缓存共享文件到本地存储路径 -->
    <init-param>
    	<param-name>smb.cacheLocal</param-name>
    	<param-value>false</param-value>
    </init-param>
    <!-- 共享文件在本地缓存的时间;默认10分钟-->
    <init-param>
    	<param-name>smb.cacheExpiry</param-name>
    	<param-value>600000</param-value>
    </init-param>
    <!-- 请求过滤前缀;默认 /smb/ -->
    <init-param>
    	<param-name>smb.requestPrefix</param-name>
    	<param-value>/smb/</param-value>
    </init-param>
    <!-- 异常信息重定向路径 -->
    <init-param>
    	<param-name>smb.redirectURL</param-name>
    	<param-value></param-value>
    </init-param>
</filter>    
<!-- 请求路径的正则匹配表达式，被匹配的路径将会被拦截器拦截处理  -->
<filter-mapping>    
	<filter-name>smbResource</filter-name>    
	<url-pattern>/smb/*</url-pattern>    
</filter-mapping>


  
# 相关资料

> CIFS (Common Internet File System)

通用Internet文件系统

在windows主机之间进行网络文件共享是通过使用微软公司自己的CIFS服务实现的。　

CIFS 是一个新提出的协议，它使程序可以访问远程Internet计算机上的文件并要求此计算机的服务。CIFS 使用客户/服务器模式。客户程序请求远在服务器上的服务器程序为它提供服务。服务器获得请求并返回响应。CIFS是公共的或开放的SMB协议版本，并由Microsoft使用。SMB协议(见最后的名词解释)现在是局域网上用于服务器文件访问和打印的协议。象SMB协议一样，CIFS在高层运行，而不象TCP/IP协议那样运行在底层。CIFS可以看做是应用程序协议如文件传输协议和超文本传输协议的一个实现。

> CIFS 可以使您达到以下功能：

1.访问服务器本地文件并读写这些文件

2.与其它用户一起共享一些文件块

3.在断线时自动恢复与网络的连接

4.使用西欧字符文件名

一般来说，CIFS使用户得到比FTP更好的对文件的控制。它提供潜在的更直接地服务器程序接口，这比使用HTTP协议的浏览器更好。CIFS最典型的应用是windows用户能够从“网上邻居”中找到网络中的其他主机并访问其中的共享文件夹.

> CIFS 是开放的标准而且已经被作为Internet应用程序标准被提交到IETF。

JCIFS是CIFS 在JAVA中的一个实现，是samba组织负责维护开发的一个开源项目,专注于使用java语言对cifs协议的设计和实现。他们将jcifs设计成为一个完整的，丰富的，具有可扩展能力且线程安全的客户端库。这一库可以应用于各种java虚拟机访问遵循CIFS/SMB网络传输协议的网络资源。类似于java.io.File的接口形式，在多线程的工作方式下被证明是有效而容易使用的。

JCIFS的开发方法类似java的文件操作功能，它的资源url定位：smb://{user}:{password}@{host}/{path}，smb为协议名，user和password分别为共享文件机子的登陆名和密码，@后面是要访问的资源的主机名或IP地址。最后是资源的共享文件夹名称和共享资源名。例如 smb://administrator:122122@192.168.0.22/test/response.txt。

在JAVA程序中，使用如下方式获得一个远程共享文件的句柄：SmbFile file = new SmbFile("smb://guest:1234@192.168.3.56/share/a.txt");这里的句柄不仅限于远程的共享文件，还可能是共享文件夹。isFile()方法和isDirectory()用来判断这个句柄对应的资源的真实属性。如果是共享文件夹，通过调用它的list()方法将获得其中资源的列表。List方法支持过滤器机制，有两种过滤器可供使用，一种是SmbFileFilter,另一种是SmbFilenameFilter，这两个在jcifs中作为接口出现，你可以根据自己的需要派生出个性化的过滤器，实现接口中的accept方法，以满足不同业务的需求。 
