package com.cc.server.control;

import com.cc.client.model.CCUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register {
    public static int register(CCUser user, Connection con)
    {
        String name=user.getNickname();
        String  username=user.getUsername();
        String password=user.getPassword();
        int flag=0;//判断是否添加成功，若为0则表示在数据库中添加了0条记录

        String sql=null;
        PreparedStatement ps=null;
        try {
            sql="select * from ccuser where username=?";
            ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            boolean repeat=rs.next();//判断是否用户已存在，若存在返回
            if(repeat)
            {
                DBConnect.closeCon(con);
                return -1;
            }

            sql="insert into ccuser VALUES (?,?,?,null,? )";
            ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ps.setString(3,"佚名");
            ps.setString(4,"无");

            flag=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnect.closeCon(con);
        return flag;
    }
}
