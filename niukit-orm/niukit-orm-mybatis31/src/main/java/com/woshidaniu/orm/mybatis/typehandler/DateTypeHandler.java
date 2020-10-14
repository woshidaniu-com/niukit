package com.woshidaniu.orm.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @className: DateTypeHandler
 * @description: 将字符串格式的日期:<code>yyyy-mm-dd</code>转换为JDBC能够识别的类型。
 * @author : kangzhidong
 * @date : 上午11:19:14 2015-4-15
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class DateTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)  throws SQLException {
        Date date = Date.valueOf(parameter.toString());
        ps.setDate(i, date);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return date.toString();
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        return date.toString();
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return date.toString();
    }

}
