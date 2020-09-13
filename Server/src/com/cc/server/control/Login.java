package com.cc.server.control;

import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 在数据库中查询是否存在该用户
 */
public class Login {
    public static boolean login(CCUser user, Connection con)
    {
        boolean login=false;
        try {
            String username = user.getUsername();
            String password = user.getPassword();

            String sql = "select * from CCUser where username=? and password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            login = rs.next();

            if (login==false) return false;

            user.setNickname(rs.getString(3));
            user.setMotto(rs.getString(5));

            //获取好友列表
            Set<CCUser> friendsList=new HashSet<>();
            //获取好友列表时怎么获取  把好友列表获取后可以试试把接受好友请求的注释去掉
            sql="select * from friends where username=?or friendname=?";
            ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,username);
            ResultSet rs2=ps.executeQuery();
            while (rs2.next())
            {
                CCUser u=new CCUser();
                String myname1=rs2.getString(1);
                String myname2=rs2.getString(3);
                if(username.equals(myname1))
                {
                    u.setUsername(rs2.getString(3));
                }
                else if(username.equals(myname2))
                {
                    u.setUsername(rs2.getString(1));
                }

                u.setIsfriendtype(rs2.getString(2));
                friendsList.add(u);
            }

            ResultSet rs3=null;
            sql="select * from ccuser where username=?";
            for(CCUser f:friendsList)
            {
                ps=con.prepareStatement(sql);
                ps.setString(1,f.getUsername());
                rs3=ps.executeQuery();
                rs3.next();
                f.setNickname(rs3.getString(3));
                f.setMotto(rs3.getString(5));
                f.setImage(rs3.getString(4));
            }
            user.setFriends(friendsList);

            //登录时查看未读消息
            ArrayList<ChatMessage> messageList=new ArrayList<>();
            sql="select * from message where username=? AND (state=?or state=?)";
            ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,"0");
            ps.setString(3,"2");
            ResultSet rs4=ps.executeQuery();
            PreparedStatement ps2=null;

            //ArrayList<String> fromusernames=new ArrayList();
            ResultSet rs5=null;
            while(rs4.next())
            {
                ChatMessage m=new ChatMessage();
                m.setContent(rs4.getString(3));
                m.setTime(rs4.getString(4));
                m.setState(Integer.valueOf(rs4.getString(2)));

                String fromusername=rs4.getString(5);
                m.setFromusername(fromusername);

                CCUser from=new CCUser();
                from.setUsername(fromusername);
                sql="select * from ccuser where username=?";
                ps2=con.prepareStatement(sql);
                ps2.setString(1,fromusername);
                rs5=ps2.executeQuery();
                rs5.next();
                from.setUsername(rs5.getString(1));
                from.setNickname(rs5.getString(3));
                from.setMotto(rs5.getString(5));
                from.setImage(rs5.getString(4));
                //当state=2时消息该怎么设置
                if(m.getState()==2)
                {
                    String friendtype=rs4.getString(6);
                    m.setFriendtype(friendtype);
                    from.setIsfriendtype(friendtype);
                }

                m.setFrom(from);
                messageList.add(m);

            }

            user.setMessageList(messageList);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnect.closeCon(con);
        return login;
    }
}
