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

/**
 *
 * @author admin
 */
public class DBConeccection {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/chat?autoReconnect=true&useSSL=false";
    String user = "root";
    String pass = "";
    
    PreparedStatement ps = null;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    
    private String checkLogin(String password, String nick){
        try {
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT * FROM usrers WHERE nombre_usuario='" + nick + "'"
                        + "and contrasenya='" + password + "'");
                
                if (rs.next()) {return "Acceso autorizado";}
                else {return "Por favor comprueba las credenciales";}                
                
            } catch (SQLException ex) {return "Server problem try again";} 
            
        } catch (ClassNotFoundException e) {return "Ups threr's seem to be a problem with the server";} 
        
        finally { try {conn.close();} catch (SQLException ex) {return "Shutting down server";}}
    }
}