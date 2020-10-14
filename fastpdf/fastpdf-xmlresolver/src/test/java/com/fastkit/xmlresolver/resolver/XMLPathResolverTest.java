
 /**
 * @title: XMLPathResolverTest.java
 * @package com.fastkit.xmlresolver.resolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-9
 * @time : 上午11:47:30 
 */

package com.fastkit.xmlresolver.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

 /**
 * @package com.fastkit.xmlresolver.resolver
 * @className: XMLPathResolverTest
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-9
 * @time : 上午11:47:30 
 */
public class XMLPathResolverTest extends TestCase{

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
		String rootPath = getAbsolutePath(File.separator);
		//根据处理后的路径组装正则表达式   */*/xml/*-config.xml$;
		String regex = getResolvePath(path).replace("\\", "\\\\").replace("/", "\\\\").replace("*", "(\\w)*")+"$";
		System.out.println("regex:"+regex);
		//遍历文件
		List<File> list = listFile(new File(rootPath));
		for (File file : list) {
			String absPath = file.getAbsolutePath();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(absPath);
			if(matcher.find()){
				path_list.add(absPath);
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
	
	public String getAbsolutePath(String path) {
		//得到根目录路径
		String rootPath = "E:\\testworkplace\\workplace1\\fastkit\\fastkit-jchart\\src\\main\\resources\\";
		//处理根目录路径
		rootPath = getResolvePath(rootPath);
		//处理目标路径
		String resolvePath = getResolvePath(path);
		if(resolvePath.startsWith(File.separator)){
			resolvePath = resolvePath.substring(File.separator.length());
		}
		//返回最终路径
		return rootPath + File.separator + resolvePath;
	}
	
	public void test() throws FileNotFoundException{
		System.out.println("222");	
		//System.out.println(resolver("\\resources\\*-config.xml"));
	}
	
}



