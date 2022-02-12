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
    private static final String driver = "com.mysql.cj.jdbc.Driver";
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
                         
            } catch (SQLException ex) {ex.printStackTrace(); return "SP";}             
        } catch (ClassNotFoundException e) {return "SP";}         
        finally { try {conn.close();} catch (SQLException ex) {return "SPS";} catch (Exception ex) {return "SPS";}}
    }
    
    public static String[] registerUser(String error, String nick){
        //parse response
        String[] data = nick.split("~");
        String[] serverError = {error, "\n\tServer Error", ""};
        try{     
            Class.forName(driver);
            try{
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT user_id FROM users WHERE username='" + data[0] + "'");
                if (rs.next()) {error += "\n\tNick taken"; }
                
                if(error.equals("")){
                    ps = conn.prepareStatement("INSERT INTO `users` (`username`, `password`, `email`, `name`, `surname`, `last_ip`, `genre`, `image`) VALUES (?, ?, ?, ?, ?, '', ?, 'null')");
                    ps.setString(1, data[0]);
                    ps.setString(2, data[4]);
                    ps.setString(3, data[2]);
                    ps.setString(4, data[3]);
                    ps.setString(5, data[1]);
                    ps.setString(6, data[5]);
                    ps.executeUpdate();
                }
                String[] msg = {error, "", data[0]};
                return msg;
            } catch (SQLException ex) {ex.printStackTrace(); return serverError;}             
        } catch (ClassNotFoundException e) {return serverError;}        
        finally { try {conn.close();} catch (SQLException ex) {return serverError;} catch (Exception ex) {return serverError;}}
    }

    static String[] getChats(String nick) {
        String[] result = {"", "ERROR!"};
        try {
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT chat_id, message FROM messages WHERE username='" + nick + "'");
                               
                if (rs.next()) {
                    result[0] = rs.getString(0);
                    result[1] = rs.getString(1);
                    return result;
                
                } else {result[1] = ""; return result;}  
                
            } catch (SQLException ex) {ex.printStackTrace(); return result;}             
        } catch (ClassNotFoundException e) {return result;}         
        finally { try {conn.close();} catch (SQLException ex) {return result;} catch (Exception ex) {return result;}}
    }
}