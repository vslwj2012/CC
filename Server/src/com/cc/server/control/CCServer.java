package com.cc.server.control;


import com.cc.client.model.CCUser;
import com.cc.client.model.ChatMessage;
import com.cc.client.model.Files;
import com.cc.client.model.MessageType;

import static com.cc.client.model.RespoundType.*;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是聊天软件的服务器类
 *
 * 本次项目中，服务端充当两个角色
 * 1.业务处理中心，如处理用户登录或注册请求
 * 2.消息中转信息
 */
public class CCServer {
    private ServerSocket server;//定义一个serversocket对象用来让客户端连接
    private Connection con;
    private ObjectInput in;
    private ObjectOutput out;
    private Change change=new Change();//用于实现修改信息操作
    private AddAndDeleteFriend aadf=new AddAndDeleteFriend();//用于实现添加删除好友

    Map<String,ObjectOutput> onlineClient=new HashMap<>();//定义一个键值对集合存储在线的用户的账号和输出流，用来转发消息时确定对象
    //当一个用户登录成功时加入集合

    //1.在动态代码块中将serversocket对象初始化
    {
        try {
            server=new ServerSocket(8888);
            System.out.println("服务器启动成功！");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("服务器启动失败！请检查端口是否被占用！");
        }
    }

    //2.在类的构造器中开启对外服务的方法（调用accpet方法）
    public CCServer(){
        try{
            while(true)//用于用户反复登录
            {
                Socket client = server.accept();
                System.out.println(client.getInetAddress().getHostAddress()+"链接进来了");
                out=new ObjectOutputStream(client.getOutputStream());
                in=new ObjectInputStream(client.getInputStream());

                new MessageReciveThread(out,in).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void login(CCUser user, ObjectOutput out)
    {
        ChatMessage replay=new ChatMessage();
        con=DBConnect.getCon();
        boolean login=Login.login(user,con);

        if(onlineClient.containsKey(user.getUsername()))//判断用户是否已经在线
        {
            replay.setRestype(RELOGIN);
        }
        else{
            if(login)
            {
                replay.setRestype(LOGINSUCCESS);
                replay.setFrom(user);

                onlineClient.put(user.getUsername(),out);//添加在线用户
            }
            else
            {
                replay.setRestype(DISLOGIN);
            }
        }


        try {
            out.writeObject(replay);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(CCUser user,ObjectOutput out)
    {
        ChatMessage replay=new ChatMessage();
        con=DBConnect.getCon();
        int register=Register.register(user,con);

        if(register==-1)
        {
            replay.setRestype(REPEAT);
        }
        else if (register==1)
        {
            replay.setRestype(REGISTERSUCCESS);
        }

        try {
            out.writeObject(replay);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFriend(ChatMessage message,ObjectOutput out)
    {
        Connection con=DBConnect.getCon();
        int flag=aadf.addFriend(message,con);

        ChatMessage replay=new ChatMessage();
        replay.setType(MessageType.ADDFRIEND);
        if (flag==-1)
        {
            replay.setContent("-1");//不存在该用户
        }
        else if(flag==0)
        {
            replay.setContent("0");//已是好友
        }
        else
        {
            if(onlineClient.containsKey(message.getFriendusername()))//如果在线
            {
                ChatMessage friendREQ=new ChatMessage();
                friendREQ.setFrom(message.getFrom());
                friendREQ.setFriendusername(message.getFriendusername());
                friendREQ.setType(MessageType.RECIVEFRIEND);
                friendREQ.setFriendtype(message.getFriendtype());
                friendREQ.setState(2);
                friendREQ.setContent(message.getFrom().getNickname()+"("+message.getFrom().getUsername()+")请求添加您为好友！");

                String sql="insert into message VALUES (?,?,?,?,?,?)";
                PreparedStatement ps= null;
                try {//如果在现时没接受，则在下次上线时提醒，所以要加入数据库一条消息记录,所以也要存入数据库
                    con=DBConnect.getCon();
                    ps = con.prepareStatement(sql);
                    ps.setString(2,"2");
                    ps.setString(1,message.getFriendusername());
                    ps.setString(3,message.getFrom().getNickname()+"("+message.getFrom().getUsername()+")请求添加您为好友！");
                    ps.setString(4,new Date().toLocaleString());
                    ps.setString(5,message.getFrom().getUsername());
                    ps.setString(6,message.getFriendtype());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    ObjectOutputStream toout=(ObjectOutputStream)onlineClient.get(message.getFriendusername());
                    toout.writeObject(friendREQ);
                    toout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else { //如果好友不在线
                String sql="insert into message VALUES (?,?,?,?,?,?)";
                Connection conn=DBConnect.getCon();
                PreparedStatement ps= null;
                try {
                    ps = conn.prepareStatement(sql);
                    ps.setString(1,message.getFriendusername());
                    ps.setString(2,"2");
                    ps.setString(3,message.getFrom().getNickname()+"("+message.getFrom().getUsername()+")请求添加您为好友！");
                    ps.setString(4,new Date().toLocaleString());
                    ps.setString(5,message.getFrom().getUsername());
                    ps.setString(6,message.getFriendtype());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        try {
            out.writeObject(replay);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chatText(ChatMessage message,ObjectOutput out)
    {
        System.out.println(message);
        message.setTime(new Date().toLocaleString());
        String to=message.getTo().getUsername();
        String from=message.getFrom().getUsername();
        message.setFromusername(from);
        con=DBConnect.getCon();
        if(onlineClient.containsKey(to))//判断是否在线
        {
            System.out.println("服务器已转发消息给在线用户");
            message.setState(0);
            ObjectOutputStream toout= (ObjectOutputStream) onlineClient.get(to);
            message.getTo().getMessageList().add(message);
            try {
                toout.writeObject(message);
                toout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            message.setState(0);
        }

        //将消息插入数据库
        String sql="Insert into message values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,to);
            ps.setString(2,message.getState()+"");
            ps.setString(3,message.getContent());
            ps.setString(4,message.getTime());
            ps.setString(5,from);
            ps.setString(6,null);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //每连进来一个用户开启一个线程，否则所有客户端共享io，造成io阻塞
    private class MessageReciveThread extends Thread{
        private ObjectOutput out;
        private ObjectInput in;
        private String username;//用于记录连接进来的用户名，方便用户退出时从在线集合中删除

        public MessageReciveThread(ObjectOutput out, ObjectInput in){
            this.out=out;
            this.in=in;
        }

        public void run()
        {
            while(true)//用于反复接收用户请求
            {

                try {

                    ChatMessage message=(ChatMessage)in.readObject();
                    switch(message.getType())
                    {
                        case LOGIN:{
                            username=message.getFrom().getUsername();
                            System.out.println(username);
                            login(message.getFrom(),out);break;
                        }
                        case REGISTER:{register(message.getFrom(),out);break;
                        }
                        case CHANGENICK:{con=DBConnect.getCon();
                            change.changeNick(message.getFrom(),con);break;
                        }
                        case CHANGEMOTTO:{con=DBConnect.getCon();
                            change.changeMotto(message.getFrom(),con);break;
                        }
                        case ADDFRIEND:{
                            addFriend(message,out);break;
                        }
                        case TEXT:{
                            chatText(message,out);break;
                        }
                        case AREADYREADMESSAGE:{
                            con=DBConnect.getCon();

                            String fromwho=message.getFromusername();
                            String username=message.getTo().getUsername();
                            String sql="update message set state=? where fromwho=? and username=? and content=?";
                            PreparedStatement ps=null;

                            ps=con.prepareStatement(sql);
                            ps.setString(1,"1");
                            ps.setString(2,fromwho);
                            ps.setString(3,username);
                            ps.setString(4,message.getContent());
                            ps.executeUpdate();

                        }
                        case AREADYREADMESSAGES:{
                            con=DBConnect.getCon();
                            CCUser u=message.getTo();
                            String fromwho=message.getFromusername();
                            String username=message.getTo().getUsername();
                            ArrayList<ChatMessage> messageList=u.getMessageList();

                            String sql="update message set state=? where fromwho=? and username=? and content=?";
                            PreparedStatement ps=null;
                            for(ChatMessage m:messageList)
                            {
                                ps=con.prepareStatement(sql);
                                ps.setString(1,"1");
                                ps.setString(2,fromwho);
                                ps.setString(3,username);
                                ps.setString(4,m.getContent());
                                ps.executeUpdate();
                            }
                        }
                        case RECIVEFRIEND:{
                            ChatMessage recive=message;
                            String to=message.getFrom().getUsername();
                            Connection con=DBConnect.getCon();
                            if(recive.getState()==3)
                            {
                                String sql="insert into friends(username, friendtype, friendname) values(?,?,?)";
                                PreparedStatement ps=con.prepareStatement(sql);
                                ps.setString(1,message.getFrom().getUsername());
                                ps.setString(2,message.getFriendtype());
                                ps.setString(3,message.getFriendusername());
                                ps.executeUpdate();

                                sql="delete from message where username=?and fromwho=? and state=?";
                                ps=con.prepareStatement(sql);
                                ps.setString(1,message.getFriendusername());
                                ps.setString(2,message.getFrom().getUsername());
                                ps.setString(3,"2");
                                ps.executeUpdate();

                                CCUser towho=new CCUser();
                                sql="select * from ccuser where username=?";
                                ps=con.prepareStatement(sql);
                                ps.setString(1,message.getFriendusername());
                                ResultSet rs=ps.executeQuery();
                                rs.next();
                                towho.setIsfriendtype(message.getFriendtype());
                                towho.setUsername(message.getFriendusername());
                                towho.setNickname(rs.getString(3));
                                towho.setMotto(rs.getString(5));
                                towho.setImage(rs.getString(4));

                                recive.setFriendusername(towho.getUsername());
                                recive.setFromusername(message.getFrom().getUsername());
                                recive.setTo(message.getFrom());
                                recive.setFrom(towho);
                            }
                            else if(recive.getState()==4)
                            {
                                String sql="delete from message where username=?and fromwho=? and state=?";
                                PreparedStatement ps=con.prepareStatement(sql);
                                ps.setString(1,message.getFriendusername());
                                ps.setString(2,message.getFrom().getUsername());
                                ps.setString(3,"2");
                                ps.executeUpdate();

                            }

                            ObjectOutputStream toout= (ObjectOutputStream) onlineClient.get(to);
                            recive.setType(MessageType.FRIENDRES);
                            try {
                                toout.writeObject(recive);
                                toout.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case FILE:{
                            ObjectOutputStream toFileOut=(ObjectOutputStream)onlineClient.get(message.getFiles().getTouser());
                            toFileOut.writeObject(message);
                            toFileOut.flush();

                            Files files=message.getFiles();
                            System.out.println(files.getFromuser()+"发送"+files.getFilename()+"给"+files.getTouser());
                            long filesize=files.getFilesize();
                            int transUnitLength=1024;
                            long transCount=filesize/transUnitLength;
                            for (long n=0;n<transCount;n++)
                            {
                                byte[] bs=new byte[transUnitLength];
                                int length=in.read(bs);
                                System.out.println(length);
                                toFileOut.write(bs,0,length);
                                toFileOut.flush();
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onlineClient.remove(username);
                    System.out.println("用户已退出"+username);
                    return;//当客户端关闭时，服务端还在跑while循环，还在一直读对象
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new CCServer();
    }
}