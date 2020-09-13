package com.cc.server.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 连接关闭数据库
 */
public class DBConnect {
    private static String url="jdbc:mysql://localhost/CC?useUnicode=true&characterEncoding=utf-8";
    private static String user="root";
    private static String password="lwj3524584";
    private static Connection con;

    /**
     * 连接数据库
     * @return
     */
    public static Connection getCon(){
        try {
            con=DriverManager.getConnection(url,user,password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 关闭数据库
     */
    public static void closeCon(Connection con)
    {
        if(con!=null)
        {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                con=null;
            }
        }
    }
}
