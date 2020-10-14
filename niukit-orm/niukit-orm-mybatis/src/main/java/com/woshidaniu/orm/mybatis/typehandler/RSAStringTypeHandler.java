package com.woshidaniu.orm.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class RSAStringTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		try {
			//parameter = RSAHexCodec.getInstance().encodeByPublicKey(parameter, RSAHexCodec.public_key);
		} catch (Exception e) {
		}
		ps.setString(i, parameter);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String returnValue = null;
		try {
			returnValue = rs.getString(columnName);
			if (null != returnValue) {
				//returnValue = RSAHexCodec.getInstance().decodeByPrivateKey(returnValue, RSAHexCodec.private_key);
			}
		} catch (Exception e) {
		}
		return returnValue;
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String returnValue = null;
		try {
			returnValue = rs.getString(columnIndex);
			if (null != returnValue) {
				//returnValue = RSAHexCodec.getInstance().decodeByPrivateKey(returnValue, RSAHexCodec.private_key);
			}
		} catch (Exception e) {
		}
		return returnValue;
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String returnValue = null;
		try {
			returnValue = cs.getString(columnIndex);
			if (null != returnValue) {
				//returnValue = RSAHexCodec.getInstance().decodeByPrivateKey(returnValue, RSAHexCodec.private_key);
			}
		} catch (Exception e) {
		}
		return returnValue;
	}

}
