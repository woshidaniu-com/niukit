package com.woshidaniu.niuca.tp.cas.client.filter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChkUser {
    public ChkUser() {
    }

    public static void main(String[] args) {
        ChkUser chk = new ChkUser();
        System.out.println(chk.getString()[0]);
    }

    public String[] getString() {
        String[] tmp = (String[])null;
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            try {
                String sql = "select wzdm,wzmc from wzxxb where wzdm='01000001'";
                con = DriverManager.getConnection("jdbc:oracle:thin:@10.71.19.133:1521:orcl", "woshidaniu_zfsmp_zt", "woshidaniu_zfsmp_zt");
                pt = con.prepareStatement(sql);
                rs = pt.executeQuery();
                if (rs.next()) {
                    tmp = new String[]{rs.getString("wzdm"), rs.getString("wzmc")};
                }

                rs.close();
                pt.close();
                con.close();
            } catch (SQLException var18) {
                var18.printStackTrace();
            }
        } catch (ClassNotFoundException var19) {
            var19.printStackTrace();

            try {
                if (rs != null) {
                    rs.close();
                }

                if (pt != null) {
                    pt.close();
                }

                if (con != null) {
                    con.close();
                }

                rs = null;
                pt = null;
                con = null;
            } catch (SQLException var17) {
                var17.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pt != null) {
                    pt.close();
                }

                if (con != null) {
                    con.close();
                }

                rs = null;
                pt = null;
                con = null;
            } catch (SQLException var16) {
                var16.printStackTrace();
            }

        }

        return tmp;
    }
}
