package com.cc.server.control;

import com.cc.client.model.CCUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Change {
    public int changeNick(CCUser user, Connection con)
    {
        int flag=0;
        try {
            String sql="update CCUser set nickname=? where username=? ";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,user.getNickname());
            ps.setString(2,user.getUsername());
            flag=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnect.closeCon(con);
        return flag;
    }

    public int changeMotto(CCUser user,Connection con)
    {
        int flag=0;
        try {
            String sql="update CCUser set motto=? where username=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,user.getMotto());
            ps.setString(2,user.getUsername());
            flag=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnect.closeCon(con);
        System.out.println(flag);
        return flag;
    }
}
