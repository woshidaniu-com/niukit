package com.woshidaniu.rjpj.api.impl;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.woshidaniu.rjpj.api.RequestUrlParameter;
import com.woshidaniu.rjpj.api.RequestUrlParameterTool;
import com.woshidaniu.rjpj.api.codec.Base64;
import com.woshidaniu.rjpj.api.codec.MD5Codec;

public class SimpleRequestUrlParameterToolImpl implements RequestUrlParameterTool{
	
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	@Override
	public String generateUrlParameterString(String privateKey, Map<String, String> parameters) {
		
		assertNecessaryParameter(parameters,RequestUrlParameter.XXDM);
		assertNecessaryParameter(parameters,RequestUrlParameter.XM);
		//assertNecessaryParameter(parameters,RequestUrlParameter.SJHM);
		assertNecessaryParameter(parameters,RequestUrlParameter.FORM_CODE);
		
		String sjhm = parameters.get(RequestUrlParameter.SJHM);
		if(sjhm == null){
			parameters.put(RequestUrlParameter.SJHM, "");
		}
		
		//根据参数名排序
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		treeMap.put(RequestUrlParameter.XXDM, parameters.get(RequestUrlParameter.XXDM));
		treeMap.put(RequestUrlParameter.XM, parameters.get(RequestUrlParameter.XM));
		treeMap.put(RequestUrlParameter.SJHM, parameters.get(RequestUrlParameter.SJHM));
		treeMap.put(RequestUrlParameter.FORM_CODE, parameters.get(RequestUrlParameter.FORM_CODE));
		
		//xm进行base64编码
		String originXm = treeMap.get(RequestUrlParameter.XM);
		//utf8处理
		byte[] array = originXm.getBytes(DEFAULT_CHARSET);
		String base64Xm = Base64.encodeBase64URLSafeString(array);
		treeMap.put(RequestUrlParameter.XM, base64Xm);
		
		return doBuild(privateKey,treeMap);
	}
	
	private String doBuild(String key,TreeMap<String,String> treeMap) {
		
		String param = makeParamStr(treeMap);
		String sign = MD5Codec.encrypt(key, param);
		
		String urlParameter = param + "&" + RequestUrlParameter.SIGN + "=" + sign;
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
	
	private void assertNecessaryParameter(Map<String, String> parameters, String name) {
		if(!parameters.containsKey(name)){
			throw new IllegalArgumentException("miss parameter:["+ name +"] in parameters!");
		}
		String value = parameters.get(name);
		if(value == null || value.equals("")){
			throw new IllegalArgumentException("parameter:["+ name +"]'s value in is null!");
		}
	}

	@Override
	public boolean verifyUrlParameter(String publicKey, Map<String, String> parameters) {
		if(!parameters.containsKey(RequestUrlParameter.XXDM) ||
		   !parameters.containsKey(RequestUrlParameter.XM) ||
		   !parameters.containsKey(RequestUrlParameter.FORM_CODE) ||
		   !parameters.containsKey(RequestUrlParameter.SIGN)){
				return false;
			}
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		
		treeMap.put(RequestUrlParameter.XXDM, parameters.get(RequestUrlParameter.XXDM));
		treeMap.put(RequestUrlParameter.XM, parameters.get(RequestUrlParameter.XM));
		treeMap.put(RequestUrlParameter.FORM_CODE, parameters.get(RequestUrlParameter.FORM_CODE));
		
		String sjhm = parameters.get(RequestUrlParameter.SJHM);
		if(sjhm == null){
			treeMap.put(RequestUrlParameter.SJHM, "");
		}else{
			treeMap.put(RequestUrlParameter.SJHM, parameters.get(RequestUrlParameter.SJHM));
		}
		
		String sign = parameters.get(RequestUrlParameter.SIGN);
		
		String param = makeParamStr(treeMap);
		String signToCompulate = MD5Codec.encrypt(publicKey, param);
		return signToCompulate.equals(sign);
	}

	@Override
	public Map<String, String> getOriginParameter(Map<String, String> parameters) {
		Map<String,String> copyParameters = new HashMap<String,String>(parameters);
		
		String base64Xm = copyParameters.get(RequestUrlParameter.XM);
		//xm进行base64解码
		byte[] originXm = Base64.decodeBase64(base64Xm.getBytes());
		String originXmStr = new String(originXm,DEFAULT_CHARSET);
		copyParameters.put(RequestUrlParameter.XM, originXmStr);
		
		return copyParameters;
	}
}
