package com.woshidaniu.configuration.base;

public abstract class JavaConstants {

	// 应用根目录
	public static final String WEB_ROOT = "web_root";
	
	//------------Java 系统常量Key-------------------------------------------------------
	
	// 用户名称
	public static final String USER_NAME = "user.name";
	
	// 用户家目录(home directory),如Windows中Administrator的家目 录为c:\Documents and
	// Settings\Administrator
	public static final String USER_HOME = "user.home";
	// 用户目前的工作目录
	public static final String USER_DIR = "user.dir";
	// 用户的临时工作目录 
	public static final String TEMP_DIR = "temp.dir";
	// 文件分隔符，例如在window环境的文件分陋符是"\",而Unix环境的文件分隔符刚好相反，是"/".
	public static final String FILE_SEPARATOR = "file.separator";
	// 路径分隔符，如Unix是以“:”表示
	public static final String PATH_SEPARATOR = "path.separator";
	// 换行符号，如Unix是以"\n"表示
	public static final String LINE_SEPARATOR = "line.separator";
	// 显示java版本
	public static final String JAVA_VERSION = "java.version";
	// 显示java制造商
	public static final String JAVA_ENDOR = "java.endor";
	// 显示java制造商URL
	public static final String JAVA_ENDOR_URL = "java.endor.url";
	// 显示java的安装路径
	public static final String JAVA_HOME = "java.home";
	// 显示java类版本
	public static final String JAVA_CLASS_VERSION = "java.class.version";
	// 显示java classpath
	public static final String JAVA_CLASS_PATH = "java.class.path";
	// 显示操作系统名称
	public static final String OS_NAME = "os.name";
	// 显示操作系统结构，如x86
	public static final String OS_ARCH = "os.arch";
	// 显示操作系统版本
	public static final String OS_VERSION = "os.version";
	/**
	 * session里存放用户信息的属性名
	 */
	public static final String USER_ENTITY = "user.entity";
	

	public static void main(String[] args) {
		System.out.println(System.getProperty(USER_DIR));
		System.out.println(System.getProperty(USER_HOME));
		System.out.println(System.getProperty(USER_NAME));
		System.out.println(System.getProperty(JAVA_CLASS_PATH));
		System.out.println(System.getProperty(JAVA_CLASS_VERSION));
		System.out.println(System.getProperty(JAVA_ENDOR));
		System.out.println(System.getProperty(JAVA_ENDOR_URL));
		System.out.println(System.getProperty(JAVA_HOME));
		System.out.println(System.getProperty(JAVA_VERSION));
		System.out.println(System.getProperty(FILE_SEPARATOR));
		System.out.println(System.getProperty(PATH_SEPARATOR));
		System.out.println(System.getProperty(LINE_SEPARATOR));
		System.out.println(System.getProperty(TEMP_DIR));
		
	}
}



