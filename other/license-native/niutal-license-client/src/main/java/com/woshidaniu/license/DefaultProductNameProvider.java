/**
 * 
 */
package com.woshidaniu.license;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����ϵͳ�������Ĭ�ϵ�����
 *   
 *   class : com.woshidaniu.license.DefaultProductNameProvider
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��8��11������2:53:40
 */
public class DefaultProductNameProvider implements WhoAmI {

	/**
	 * ϵͳ����
	 */
	private String productName;
	
	public static final String DEFAULT_PRODUCT_NAME = "���Ǵ�ţ�������";
	
	/* (non-Javadoc)
	 * @see com.woshidaniu.license.WhoAmI#myNameIs()
	 */
	@Override
	public String myNameIs() {
		String myName = DEFAULT_PRODUCT_NAME;
		if(productName != null){
			myName = productName;
		}
		try {
			return new Base64().encodeAsString(myName.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
