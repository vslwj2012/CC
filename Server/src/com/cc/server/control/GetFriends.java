package com.cc.server.control;

import com.cc.client.model.CCUser;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class GetFriends {
    public static Set<CCUser> getAllFriends(CCUser user,Connection con)
    {
        PreparedStatement ps=null;
        String sql = "select * from ccuser where username=?";
        try {
            ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            rs.next();//判断是否用户已存在，若存在返回

            Set<CCUser> friends = new HashSet<>();
            return friends;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;

        }
}
