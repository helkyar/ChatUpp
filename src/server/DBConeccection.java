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
import javax.swing.JOptionPane;

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
    
    private void checkLogin(String password, String user){
        try {
            Class.forName(driver);
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT * FROM usrers WHERE nombre_usuario='" + user + "'"
                        + "and contrasenya='" + password + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Acceso autorizado");
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor comprueba las credenciales");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha habido un problema con el servidor.\n Prueba otra vez");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No va");
        }
    }
}