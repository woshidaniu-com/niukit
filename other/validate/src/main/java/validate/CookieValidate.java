//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieValidate {
    String VALIDATEURL = "http://zuinfo.zju.edu.cn:8080/AMWebService/Validate";

    String validate(HttpServletRequest var1, HttpServletResponse var2, String var3);

    void deleteValidateData(String var1);
}
