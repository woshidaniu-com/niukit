package com.woshidaniu.rjpj.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.woshidaniu.rjpj.api.RequestUrlParameter;
import com.woshidaniu.rjpj.api.RequestUrlParameterTool;
import com.woshidaniu.rjpj.api.codec.Base64;
import com.woshidaniu.rjpj.api.codec.RSACoder;

@Deprecated
public class RequestUrlParameterToolImpl implements RequestUrlParameterTool{

	@Override
	public String generateUrlParameterString(String privateKey,Map<String, String> parameters) {
		
		assertNecessaryParameter(parameters,RequestUrlParameter.XXDM);
		assertNecessaryParameter(parameters,RequestUrlParameter.XM);
		assertNecessaryParameter(parameters,RequestUrlParameter.SJHM);
		assertNecessaryParameter(parameters,RequestUrlParameter.FORM_CODE);
		
		//根据参数名排序
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		treeMap.put(RequestUrlParameter.XXDM, parameters.get(RequestUrlParameter.XXDM));
		treeMap.put(RequestUrlParameter.XM, parameters.get(RequestUrlParameter.XM));
		treeMap.put(RequestUrlParameter.SJHM, parameters.get(RequestUrlParameter.SJHM));
		treeMap.put(RequestUrlParameter.FORM_CODE, parameters.get(RequestUrlParameter.FORM_CODE));
		
		//xm进行base64编码
		String originXm = treeMap.get(RequestUrlParameter.XM);
		String base64Xm = Base64.encodeBase64URLSafeString(originXm.getBytes());
		treeMap.put(RequestUrlParameter.XM, base64Xm);
		
		return doBuild(privateKey,treeMap);
	}

	@Override
	public boolean verifyUrlParameter(String publicKey,Map<String, String> parameters) {
		if(!parameters.containsKey(RequestUrlParameter.XXDM) ||
		   !parameters.containsKey(RequestUrlParameter.XM) ||
		   !parameters.containsKey(RequestUrlParameter.SJHM) ||
		   !parameters.containsKey(RequestUrlParameter.FORM_CODE) ||
		   !parameters.containsKey(RequestUrlParameter.SIGN)){
				return false;
			}
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		
		treeMap.put(RequestUrlParameter.XXDM, parameters.get(RequestUrlParameter.XXDM));
		treeMap.put(RequestUrlParameter.XM, parameters.get(RequestUrlParameter.XM));
		treeMap.put(RequestUrlParameter.SJHM, parameters.get(RequestUrlParameter.SJHM));
		treeMap.put(RequestUrlParameter.FORM_CODE, parameters.get(RequestUrlParameter.FORM_CODE));
		
		String sign = parameters.get(RequestUrlParameter.SIGN);
		
		String param = makeParamStr(treeMap);
		byte[] signData = Base64.decodeBase64(sign);
		byte[] keyData = Base64.decodeBase64(publicKey);
		
		try {
			return RSACoder.verify(param.getBytes(), keyData, signData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Map<String, String> getOriginParameter(Map<String, String> parameters) {
		Map<String,String> copyParameters = new HashMap<String,String>(parameters);
		
		String base64Xm = copyParameters.get(RequestUrlParameter.XM);
		
		//xm进行base64解码
		byte[] originXm = Base64.decodeBase64(base64Xm.getBytes());
		String originXmStr = new String(originXm);
		copyParameters.put(RequestUrlParameter.XM, originXmStr);
		
		return copyParameters;
	}
	
	private void assertNecessaryParameter(Map<String, String> parameters, String name) {
		if(!parameters.containsKey(name)){
			throw new IllegalArgumentException("miss parameter:["+ name +"] in parameters!");
		}
		String value = parameters.get(name);
		if(value == null || value.equals("")){
			throw new IllegalArgumentException("parameter:["+ name +"]'s value in is null!");
		}
	}
	
	private String doBuild(String key,TreeMap<String,String> treeMap) {
		
		String param = makeParamStr(treeMap);
		
		//对所有参数构建前面
		byte[] privateKey = Base64.decodeBase64(key);
		byte[] sign = null;
		try {
			sign = RSACoder.sign(param.getBytes(), privateKey);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String signStr = Base64.encodeBase64URLSafeString(sign);
		String urlParameter = param + "&" + RequestUrlParameter.SIGN + "=" + signStr +"&" +RequestUrlParameter.RT +"=" + System.currentTimeMillis()+"";
		return urlParameter;
	}
	
	private String makeParamStr(TreeMap<String,String> treeMap){
		
		StringBuilder stringBuilder = new StringBuilder();
		
		boolean first = true;
		
		for(Entry<String, String> e : treeMap.entrySet()){
			if(!first){
				stringBuilder.append("&");
			}
			stringBuilder.append(e.getKey()).append("=").append(e.getValue());
			first = false;
		}
		String param = stringBuilder.toString();
		return param;
	}
}
