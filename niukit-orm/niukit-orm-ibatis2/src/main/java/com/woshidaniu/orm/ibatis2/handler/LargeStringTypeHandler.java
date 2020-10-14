package com.woshidaniu.orm.ibatis2.handler;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibatis.sqlmap.engine.type.StringTypeHandler;
/**
 * 
 * @description:For using LargeStringTypeHandler, you must add a line in SqlMapConfig.xml as following:<br/>
 * 				<typeHandler javaType="java.lang.String" callback="com.fastkit.orm.ibatis2.typehandler.LargeStringTypeHandler"/>
 * @author kangzhidong
 * @date 2012-6-14
 */
public class LargeStringTypeHandler extends StringTypeHandler {
   
    public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType) throws SQLException {
        String s = (String)parameter;
        if (s.length() < 667) {
            //assume that all characters are chinese characters.
            super.setParameter(ps, i, parameter, jdbcType);
        }else{
            //use setCharacterStream can insert more characters.
            ps.setCharacterStream(i, new StringReader(s), s.length());
        }
    }
   
}

