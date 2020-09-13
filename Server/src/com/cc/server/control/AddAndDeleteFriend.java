package com.cc.server.control;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddAndDeleteFriend {
    public int addFriend(ChatMessage message, Connection con)
    {
        String username=message.getFrom().getUsername();
        String friendusername=message.getFriendusername();
        String friendtype=message.getFriendtype();

        try {
            String sql="select * from ccuser where username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,friendusername);
            ResultSet rs1=ps.executeQuery();
            boolean isin=rs1.next();//判断该用户是否存在
            if(!isin)
            {
                DBConnect.closeCon(con);
                return -1;
            }
            String nick=rs1.getString(3);
            String image=rs1.getString(4);
            String motto=rs1.getString(5);
            CCUser fr=new CCUser();
            fr.setNickname(nick);
            fr.setImage(image);
            fr.setMotto(motto);
            message.setTo(fr);

            sql="select * from friends where (username=? AND friendname=?)or (username=?and friendname=?)";
            ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,friendusername);
            ps.setString(3,friendusername);
            ps.setString(4,username);
            ResultSet rs2=ps.executeQuery();
            boolean isfriend=rs2.next();
            if (isfriend)
            {
                DBConnect.closeCon(con);
                return 0;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBConnect.closeCon(con);
        return 1;
    }
}
