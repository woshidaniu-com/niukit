package com.woshidaniu.fastpdf.icepdf;

import java.util.List;

public class HtmlHelper 
{
	public static final String dviImgP_head = "<div style=\"float:left;width:100%;border:1px #aaa solid;\"><img id='img' src='x'  width='100%' /></div>";
	
	public static final String dviBtns = "<div style=\"clear:left;width:100%;text-align:center;\">" +
			"<button type=\"button\" id=\"previous\" onClick=\"prev()\">prev</button>" +
			"<button onClick=\"next()\" type=\"button\" id=\"next\">next</button>" +
					"</div>";
	
	public static final String js = "<script type=\"text/javascript\">" +
			"var i=1;" +
			"function prev()" +
			"{if(i>1)" +
			"{i-=1;var cur_img=document.getElementById('img'+ i );" +
			"cur_img.src='none';new_img.style.display='block';}}" +
			"function next()" +
			"{if(i<3)" +
			"{i+=1;" +
			"var cur_img=document.getElementById('img'+(i-1));" +
			"cur_img.style.display='none';new_img.style.display='block';}}" +
			"</script>";
	
	public static final String whole_head = "<html><head></head><body>"+dviImgP_head +dviBtns ;
	
	public static final String whole_end = "</body></html>";
	
	public static String getHtml(List<String> imgs) 
	{
		if( null == imgs || imgs.size() == 0 )
		{
			return null;
		}
		
		int len = imgs.size();
		
		StringBuffer jsAry = new StringBuffer("var pngAry = new Array(");
		
		for( int i = 0 ; i < len; i ++ )
		{
			String s = imgs.get(i);
			
			if( i == imgs.size()-1 )
			{
				jsAry.append("\"" + s + "\"");
			}
			else
			{
				jsAry.append("\"" + s + "\",");
			}
		}
		
		jsAry.append( ");");
		
		String js = "<script type=\"text/javascript\"> " +
				"var i=1; " + jsAry.toString() +
				"function prev()" +
				"{if(i>=1){i-=1;var img=document.getElementById('img'); img.src=pngAry[i]; }}" +
				"function next()" +
				"{if(i<"+ (len-1) + "){i+=1;var img=document.getElementById('img'); img.src=pngAry[i];}} " +
				"var cur_img=document.getElementById('img');" +
				"cur_img.src=pngAry[0]; </script>" ;
		
		String html = whole_head + js + whole_end;
		
		return html;
	}
}
