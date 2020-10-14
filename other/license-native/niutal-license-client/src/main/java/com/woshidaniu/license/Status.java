/**
 * 
 */
package com.woshidaniu.license;

/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����	��Ȩ�ļ�״̬����<br><br>
 *   		0,1,2�Ϸ����������������; <br>
 *   		3,4���Ϸ�����������������; <br>
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��6��17������9:45:14
 */
public interface Status {
	
	/**
	 * license �ļ�������
	 */
	static final int NO_LICENSE_FILE = -1;
	
	/**
	 * ��ʼ��״̬-δʹ��
	 */
	static final int INITIAL = 0;
	
	/**
	 * ����״̬-δ����
	 */
	static final int NORMAL = 1;
	
	/**
	 * ����״̬-�ӽ�����״̬
	 */
	static final int NORMAL_CHECK= 2;
	
	/**
	 * ����״̬-��������
	 */
	static final int EXPIRED = 3;
	
	/**
	 * �Ƿ�״̬-��Ȩ�ļ����ݲ�ͬ��
	 */
	static final int INVALID = 4;
	
	/**
	 * δ֪����
	 */
	static final int UNKNOWN_ERROR = 9;
}
