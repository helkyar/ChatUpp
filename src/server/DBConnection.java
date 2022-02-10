/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateo
 */
public class DBConnection {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/chat";
    private static final String user = "root";
    private static final String pass = "";
    
    private static PreparedStatement ps = null;
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
    /**
     * Checks if user and password exists in the database
     * @param password
     * @param nick
     * @return 
     */
    public static String checkLogin(String password, String nick){
        //Update last ip
        try {
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT user_id FROM users WHERE username='" + nick + "'"
                        + "and password='" + password + "'");
                               
                if (rs.next()) {return "OK";}
                else {return "X";}                
                         
            } catch (SQLException ex) {return "SP";}             
        } catch (ClassNotFoundException e) {return "SP";}         
        finally { try {conn.close();} catch (SQLException ex) {return "SPS";}}
    }
    
    public static String[] registerUser(String error, String nick){
        //parse response
        String[] data = nick.split("~");
        String[] serverError = {error, "\n\tServer Error"};
        try{     
            Class.forName(driver);
            try{
                    conn = DriverManager.getConnection(url, user, pass);
                    Connection connection = new DBConnection().conn;
                    Statement stmt = connection.createStatement();
                st = conn.createStatement();
                rs = st.executeQuery("SELECT user_id FROM users WHERE username='" + data[0] + "'");
                if (rs.next()) {error += "\n\tNick taken"; }
                
                if(error.equals("")){
                    String query = "INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `name`, `surname`, `last_ip`, `genre`, `image`)"
                                    + " VALUES (NULL,"+"'"+data[0]+"'"+", "+"'"+data[4]+"'"+", "+"'"+data[2]+"'"+", "+"'"+data[3]+"'"+", "+"'"+data[1]+"'"+", '', "+"'"+data[5]+"'"+", 'null')";
                    
                    stmt.executeUpdate(query);
                    System.out.println("ddd");
                }
                String[] msg = {error, ""};
                return msg;
            } catch (SQLException ex) {ex.printStackTrace();return serverError;}             
        } catch (ClassNotFoundException e) {return serverError;}        
        finally { try {conn.close();} catch (SQLException ex) {return serverError;}}
    }
}