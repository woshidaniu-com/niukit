#http://www.xuebuyuan.com/2170428.html
#什么是XSS？

XSS(Cross Site Scripting)，即跨站脚本攻击，是一种常见于web application中的计算机安全漏洞。XSS通过在用户端注入恶意的可运行脚本，若服务器端对用户输入不进行处理，直接将用户输入输出到浏览器，则浏览器将会执行用户注入的脚本。

#XSS的分类

根据XSS造成的影响，可以将XSS分为非持久型和持久型。

1.非持久型，也叫反射型XSS。通过GET和 POST方法，向服务器端输入数据。用户输入的数据通常被放置在URL的query string中，或者是form 数据中。如果服务器端对输入的数据不进行过滤，验证或编码，就直接将用户输入的信息直接呈现给客户，则可能会造成反射型XSS。反射型XSS是比较普遍的 XSS，其危害程度通常被认为较小。但是某些反射型XSS造成的后果会很严重，如在输入框的name中输入<meta http-equiv="refresh" content="5"/>，服务器不加处理，将name的值直接送到浏览器，则浏览器会每5秒自动刷新一次。严重者会导致服务器崩溃。

2.持久型，也叫存储型XSS。通常是因为服务器端将用户输入的恶意脚本没有通过验证就直接存储在数据库，并且每次通过调用数据库的方式，将数据呈现在浏览器上。则该 XSS跨站脚本攻击将一直存在。若其他用户访问该页面，则恶意脚本就会被触发，用于盗取其他用户的私人信息。

#常用XSS方式分为以下几种：

1.输入框中直接输入恶意脚本，如：

><script>alert(document.cookie)</script>

2.输入框中输入html标签，在标签中嵌入恶意脚本，如src，href，css style等。

<IMG SRC="javascript:alert('XSS');">;
<BODY BACKGROUND="javascript:alert('XSS')">
<STYLE>li {list-style-image:url("javascript:alert('XSS')");}</STYLE><UL><LI>XSS</br>

3.将恶意脚本注入在event事件中，如onClick，onBlur，onMouseOver等事件。

<a onmouseover="alert(document.cookie)">xxslink</a>

4.在remote style sheet，javascript中，如

<LINK REL="stylesheet"HREF="javascript:alert('XSS');">
<SCRIPT/SRC="http://ha.ckers.org/xss.js"></SCRIPT>

5.META 标签，如

<meta http-equiv="refresh"content="5" />
<META HTTP-EQUIV="Set-Cookie"Content="USERID=<SCRIPT>alert('XSS')</SCRIPT>">


#AntiSamy介绍

OWASP是一个开源的、非盈利的全球性安全组织，致力于应用软件的安全研究。我们的使命是使应用软件更加安全，使企业和组织能够对应用安全风险作出更清晰的决策。目前OWASP全球拥有140个分会近四万名会员，共同推动了安全标准、安全测试工具、安全指导手册等应用安全技术的发展。

OWASP AntiSamy项目可以有好几种定义。从技术角度看，它是一个可确保用户输入的HTML/CSS符合应用规范的API。也可以这么说，它是个确保用户无法在HTML中提交恶意代码的API，而这些恶意代码通常被输入到个人资料、评论等会被服务端存储的数据中。在Web应用程序中，“恶意代码”通常是指 Javascript。同时层叠样式表（CSS）在调用Javascript引擎的时候也会被认为是恶意代码。当然在很多情况下，一些“正常”的HTML 和CSS也会被用于恶意的目的，所以我们也会对此予以处理。
 

#AnitiSamy下载

         官方网站：https://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project
         项目地址：https://code.google.com/p/owaspantisamy/downloads/list

                   我们看到Downloads，下载WhereToGet.txt就可以看到下载地址
 

#标准策略文件说明

#antisamy-slashdot.xml

Slashdot (  http://www.slashdot.org/  )  是一个提供技术新闻的网站，它允许用户用有限  的  HTML  格式的内容匿名回帖。  Slashdot 不仅仅是目前同类中最酷的网站之一，而  且同时也曾是最容易被成功攻击的网站之一。更不幸的是，导致大部分用户遭受攻  击的原由是臭名昭着的  goatse.cx  图片  (  请你不要刻意去看  )  。  Slashdot 的安全策略非  常严格：用户只能提交下列的  html  标签：  <b>, <u>, <i>,<a>,<blockquote>  ，并且  还不支持  CSS.  

因此我们创建了这样的策略文件来实现类似的功能。它允许所有文本格式的标签来  直接修饰字体、颜色或者强调作用。

#antisamy-ebay.xml

众所周知，  eBay (  http://www.ebay.com/  )  是当下最流行的在线拍卖网站之一。它是一  个面向公众的站点，因此它允许任何人发布一系列富  HTML  的内容。  我们对  eBay  成为一些复杂  XSS  攻击的目标，并对攻击者充满吸引力丝毫不感到奇怪。由于  eBay  允许  输入的内容列表包含了比  Slashdot 更多的富文本内容，所以它的受攻击面也要大得多。下  面的标签看起来是  eBay  允许的（  eBay  没有公开标签的验证规则）  : <a>,..

#antisamy-myspace.xml

MySpace ( http://www.myspace.com/  )  是最流行的一个社交网站之一。用户允许提交  几乎所有的他们想用的  HTML  和  CSS  ，只要不包含  JavaScript 。  MySpace  现在用一  个黑名单来验证用户输入的  HTML  ，这就是为什么它曾受到  Samy  蠕虫攻击  ( http://namb.la/)  的原因。  Samy  蠕虫攻击利用了一个本应该列入黑名单的单词  (eval)  来进行组合碎片攻击的，其实这也是  AntiSamy 立项的原因。  

#antisamy-anythinggoes.xml 

也很难说出一个用这个策略文件的用例。如果你想允许所有有效的  HTML  和  CSS  元素输入（但能拒绝  JavaScript 或跟  CSS  相关的网络钓鱼攻击），你可以使用  这个策略文件。其实即使  MySpace 也没有这么疯狂。然而，它确实提供了一个很  好的参考，因为它包含了对于每个元素的基本规则，所以你在裁剪其它策略文件的  时候可以把它作为一个知识库。  

策略文件定制

http://www.owasp.org/index.php/AntiSamy_Directives

AntiSamy.JAVA的使用

首先，老规矩，我们需要一个jar包

使用Maven的，在pom.xml的dependencies中添加如下代码

<dependency>
	<groupId>org.owasp.antisamy</groupId>
	<artifactId>antisamy</artifactId>
	<version>1.5.3</version>
</dependency>


配置web.xml

<!-- XSS -->
<filter>
	<filter-name>XSS</filter-name>
	<filter-class>com.xxx.XssFilter</filter-class>
</filter>

<filter-mapping>
	<filter-name>XSS</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>


#其中XssFilter为自定义的class，该类必须实现Filter类，在XssFilter类中实现doFilter函数。将策略文件放到和pom.xml平级目录下，然后我们就开始编写XssFilter类。

public class XssFilter implements Filter {
	
	@SuppressWarnings("unused")
	private FilterConfig filterConfig;

	public void destroy() {
		this.filterConfig = null;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
}

#需要重写request，新建一个类RequestWrapper，继承HttpServletRequestWrapper，我们需要重写getParameterMap()方法，以及过滤非法html的方法xssClean()

public class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,String[]> getParameterMap(){
		Map<String,String[]> request_map = super.getParameterMap();
		Iterator iterator = request_map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry me = (Map.Entry)iterator.next();
			//System.out.println(me.getKey()+":");
			String[] values = (String[])me.getValue();
			for(int i = 0 ; i < values.length ; i++){
				System.out.println(values[i]);
				values[i] = xssClean(values[i]);
			}
		}
		return request_map;
	}
	

	private String xssClean(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
        	Policy policy = Policy.getInstance("antisamy-myspace-1.4.4.xml");
        	//CleanResults cr = antiSamy.scan(dirtyInput, policyFilePath); 
            final CleanResults cr = antiSamy.scan(value, policy);
            //安全的HTML输出
            return cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
	}
}

