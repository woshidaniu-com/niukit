
public class Test {

	
	public static void main(String[] args) {
		String temp = "70.0";
		String val = null;
		// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串  
        if (temp.indexOf(".") > -1) {  
            val = String.valueOf(new Double(temp)).trim();  
        } else {  
            val = temp.trim();  
        } 
        System.out.println(val);
        
	}
	
}
