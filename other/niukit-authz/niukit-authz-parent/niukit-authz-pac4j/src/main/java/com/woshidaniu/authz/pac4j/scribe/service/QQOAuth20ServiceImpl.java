package com.woshidaniu.authz.pac4j.scribe.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.woshidaniu.authz.pac4j.oauth.OAuth2Constants;

/**
 * 
 *  <h3><span class="mw-headline">Step2：通过Authorization Code获取Access Token</span></h3>
 *  <p><b>请求地址</b>：</p>
 *  <p>PC网站：https://graph.qq.com/oauth2.0/token</p>
 *  <p>WAP网站：https://graph.z.qq.com/moc2/token</p>
 *  <p><b>请求方法</b>：</p>
 *  <p>GET</p>
 *  <p><b>请求参数</b>：</p>
 *  <p>请求参数请包含如下内容：</p>
 *  <table class="t">
 *  	<tbody>
 *  		<tr>
 *  			<th width="100">参数</th>
 *  			<th width="100">是否必须</th>
 *  			<th>含义</th>
 *  		</tr>
 *  		<tr>
 *  			<td>grant_type</td>
 *  			<td><span style="color: red;">必须</span></td>
 *  			<td>授权类型，在本步骤中，此值为“<span style="color: #ff0000;">authorization_code</span>”。</td>
 *  		</tr>
 *  		<tr class="h">
 *  			<td>client_id</td>
 *  			<td><span style="color: red;">必须</span></td>
 *  			<td>申请QQ登录成功后，分配给网站的appid。</td>
 *  		</tr>
 *  		<tr>
 *  			<td>client_secret</td>
 *  			<td><span style="color: red;">必须</span></td>
 *  			<td>申请QQ登录成功后，分配给网站的appkey。</td>
 *  		</tr>
 *  		<tr class="h">
 *  			<td>code</td>
 *  			<td><span style="color: red;">必须</span></td>
 *  			<td>上一步返回的authorization code。<p></p>
 *  				<p>如果用户成功登录并授权，则会跳转到指定的回调地址，并在URL中带上Authorization Code。</p>
 *  				<p>例如，回调地址为www.qq.com/my.php，则跳转到：</p>
 *  				<p>http://www.qq.com/my.php?code=520DD95263C1CFEA087******</p>
 *  				<p><span style="color: red;">注意此code会在10分钟内过期。</span></p>
 *  			</td>
 *  		</tr>
 *  		<tr>
 *  			<td>redirect_uri</td>
 *  			<td><span style="color: red;">必须</span></td>
 *  			<td>与上面一步中传入的redirect_uri保持一致。</td>
 *  		</tr>
 *  	</tbody>
 *  </table>
 *  <p>&nbsp;</p>
 *  <p><b>返回说明</b>：</p>
 *  <p align="left">如果成功返回，即可在返回包中获取到Access Token。 如：</p>
 *  <p align="left">access_token=FE04************************CCE2&amp;expires_in=7776000&amp;refresh_token=88E4************************BE14</p>
 *  <table class="t">
 *  	<tbody>
 *  		<tr>
 *  			<th width="80"><b>参数说明</b></th>
 *  			<th width="150"><b>描述</b></th>
 *  		</tr>
 *  		<tr>
 *  			<td>access_token</td>
 *  			<td>授权令牌，Access_Token。</td>
 *  		</tr>
 *  		<tr class="h">
 *  			<td>expires_in</td>
 *  			<td>该access token的有效期，单位为秒。</td>
 *  		</tr>
 *  		<tr>
 *  			<td>refresh_token</td>
 *  			<td>在授权自动续期步骤中，获取新的Access_Token时需要提供的参数。</td>
 *  		</tr>
 *  	</tbody>
 *  </table>
 *  <p>&nbsp;</p>
 *  <p><b>错误码说明</b>：</p>
 *  <p>接口调用有错误时，会返回code和msg字段，以url参数对的形式返回，value部分会进行url编码（UTF-8）。</p>
 *  <p>PC网站接入时，错误码详细信息请参见：<a title="公共返回码说明" href="http://wiki.connect.qq.com/%E5%85%AC%E5%85%B1%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E#100000-100031.EF.BC.9APC.E7.BD.91.E7.AB.99.E6.8E.A5.E5.85.A5.E6.97.B6.E7.9A.84.E5.85.AC.E5.85.B1.E8.BF.94.E5.9B.9E.E7.A0.81">100000-100031：PC网站接入时的公共返回码</a>。</p>
 *  <p>WAP网站接入时，错误码详细信息请参见：<a title="公共返回码说明" href="http://wiki.connect.qq.com/%E5%85%AC%E5%85%B1%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E#7000-7999.EF.BC.9AWAP.E7.BD.91.E7.AB.99.E6.8E.A5.E5.85.A5.EF.BC.8C.E9.80.9A.E8.BF.87AuthorizationCode.E8.8E.B7.E5.8F.96AccessToken.E6.97.B6.EF.BC.8C.E5.8F.91.E7.94.9F.E9.94.99.E8.AF.AF">7000-7999：通过Authorization Code获取Access Token时，发生错误</a>。</p>
 */
public class QQOAuth20ServiceImpl extends OAuth20Service {
	
	private static Pattern openIdPattern = Pattern.compile("\"openid\":\\s*\"(\\S*?)\"");
	
    public QQOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
		super(api, config);
	}

	@Override
	public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
		String response = accessToken.getRawResponse();
		Matcher matcher = openIdPattern.matcher(response);
		if (matcher.find()) {
			request.addQuerystringParameter(OAuth2Constants.OPENID, matcher.group(1));
		} else {
			throw new OAuthException("接口返回数据 miss openid: " + response);
		}
		request.addQuerystringParameter("dataType", "json");
		super.signRequest(accessToken, request);
	}
      
}
