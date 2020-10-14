package com.woshidaniu.component.bpm.common;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.common.BLOBTypeHandler.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class BLOBTypeHandler extends BaseTypeHandler<String>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		byte[] bytes = parameter.getBytes();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	    ps.setBinaryStream(i, bis, bytes.length);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Blob blob = rs.getBlob(columnName);
	    byte[] returnValue = null;
	    if (null != blob) {
	      returnValue = blob.getBytes(1, (int) blob.length());
	      return new String(returnValue);
	    }
	   return "";
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Blob blob = rs.getBlob(columnIndex);
	    byte[] returnValue = null;
	    if (null != blob) {
	      returnValue = blob.getBytes(1, (int) blob.length());
	      return new String(returnValue);
	    }
	    return "";
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Blob blob = cs.getBlob(columnIndex);
	    byte[] returnValue = null;
	    if (null != blob) {
	      returnValue = blob.getBytes(1, (int) blob.length()); return new String(returnValue);
	    }
	    return "";
	}

}
