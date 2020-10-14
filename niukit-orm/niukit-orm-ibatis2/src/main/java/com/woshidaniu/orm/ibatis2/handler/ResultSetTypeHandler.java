package com.woshidaniu.orm.ibatis2.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibatis.sqlmap.engine.type.TypeHandler;

public class ResultSetTypeHandler implements TypeHandler {

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,String jdbcType) throws SQLException {
		ps.setString(i, (String) parameter);
	}

	@Override
	public Object getResult(ResultSet rs, String columnName)throws SQLException {
		return rs.getString(columnName);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)throws SQLException {
		return cs.getString(columnIndex);
	}

	@Override
	public boolean equals(Object arg0, String arg1) {
		return arg0.getClass().equals(arg1);
	}

	@Override
	public Object valueOf(String arg0) {
		return String.valueOf(arg0);
	}

}