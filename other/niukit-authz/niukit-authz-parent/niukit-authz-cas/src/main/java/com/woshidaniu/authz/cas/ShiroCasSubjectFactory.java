/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

import com.woshidaniu.authz.cas.token.ZFCasToken;

/**
 * 
 * @className	： ShiroCasSubjectFactory
 * @description	：
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:30
 * @version 	V1.0
 */
public class ShiroCasSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {

        //the authenticated flag is only set by the SecurityManager after a successful authentication attempt.
        boolean authenticated = context.isAuthenticated();

        //although the SecurityManager 'sees' the submission as a successful authentication, in reality, the
        //login might have been just a CAS rememberMe login.  If so, set the authenticated flag appropriately:
        if (authenticated) {

            AuthenticationToken token = context.getAuthenticationToken();

            if (token != null && token instanceof ZFCasToken) {
                ZFCasToken casToken = (ZFCasToken) token;
                // set the authenticated flag of the context to true only if the CAS subject is not in a remember me mode
                if (casToken.isRememberMe()) {
                    context.setAuthenticated(false);
                }
            }
        }

        return super.createSubject(context);
    }

}
