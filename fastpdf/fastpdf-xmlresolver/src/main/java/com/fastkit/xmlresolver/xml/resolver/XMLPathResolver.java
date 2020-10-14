package com.fastkit.xmlresolver.xml.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /**
 * @package com.fastkit.xmlresolver.resolver
 * @className: XMLPathResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-9
 * @time : 上午10:04:04 
 */
public class XMLPathResolver {
	
	protected static Logger LOG = LoggerFactory.getLogger(XMLPathResolver.class);
	private static XMLPathResolver instance = null;
	private static Object initLock = new Object();
	private XMLPathResolver(){}
	
	public static XMLPathResolver getInstance(){
		if (instance == null) {
			synchronized(initLock) {
				instance= new XMLPathResolver();
			}
		}
		return instance;
	}
	
	public List<String> resolverAll(String paths) throws FileNotFoundException{
		List<String> path_list = new ArrayList<String>();
		//第一步判断是否是多个路径
		if(paths.indexOf(",")>-1){
			String[] xmls = paths.split(",");
			for (int i = 0; i < xmls.length; i++) {
				path_list.addAll(resolver(xmls[i]));
			}
		}else{
			path_list.addAll(resolver(paths));
		}
		return path_list;
	}
	
	public String getResolvePath(String path){
		path = path.replace("\\", File.separator).replace("/", File.separator);
		if(path.endsWith(File.separator)){
			path = path.substring(0,(path.length()-File.separator.length()));
		}
		return path; 
	}
	
	public List<String> resolver(String path) throws FileNotFoundException{
		List<String> path_list = new ArrayList<String>();
		//得到文件在磁盘上的根目录路径 
		String rootPath = "E:\\testworkplace\\workplace1\\multiui_style_v1\\src\\main\\webapp\\skins";
		//String rootPath = "E:\\workplaces\\testworkplace\\multiui_style_v1\\src\\main\\webapp\\skins";
		//String rootPath = XMLElementContext.getRealPath(File.separator);
		//遍历文件
		List<File> list = listFile(new File(rootPath));
		//根据处理后的路径组装正则表达式   */*/xml/*-config.xml$;
		String regex = getResolvePath(path).replace("\\", "\\\\").replace("/", "\\\\").replace("*", "([\\w\\d\\s-])*")+"$";
		LOG.info("【regex】"+regex);
		for (File file : list) {
			String absPath = file.getAbsolutePath();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(absPath);
			if(matcher.find()){
				path_list.add(absPath);
				LOG.info("【matcher】"+absPath);
			}
		}
		return path_list;
	}
	
	private List<File> listFile(File file) throws FileNotFoundException {
		//System.out.println(file.getAbsolutePath());
		if (file.exists()) {
			List<File> list = new ArrayList<File>();
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					if (f.isDirectory()) {
						list.addAll(listFile(f));
					} else{
						//System.out.println(f.getAbsolutePath());
						list.add(f);
					}
				}
			} else if (file.isFile()) {
				//System.out.println(file.getAbsolutePath());
				list.add(file);
			}
			return list;
		} else {
			throw new FileNotFoundException( "the Path refers to the file does not exist !");
		}
	}
	
}



