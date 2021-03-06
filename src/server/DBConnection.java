/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.Encriptador;

/**
 * 
 * Esta clase contiene todos los metodos que acceden a la base de datos y es 
 * utilizada por la clase Server.
 * 
 * public static String checkLogin(String password, String nick)
 * -> Comprueba si el usuario dado existe y si a contraseña es correcta. 
 * Devuelve OK si los datos son correctos.
 * 
 * public static String[] registerUser(String error, String nick)
 * -> Inserta un registro en la tabla usuario. En caso de erros, devuelve
 * un array con los mensjaes de error.
 * 
 * static void setLastIP(String nick, String ip)
 * -> actualiza un registro de la tabla ussuarios con la ultima IP usada por ese
 * usuario
 * 
 * static String[] getChats(String nick)
 * -> obtiene todos los historiales de un usuario concreto
 * 
 * static String getUsers(String msg, String id)
 * -> dependiendo del valor de msg, obtiene todos los ususarios u obtiene todos 
 * los ususarios de un chat.
 * 
 * static String createNewGroup(String msg, String nick)
 * -> Inserta en chats el regitro para el nuevo chat y en participants los 
 * registros de los participantes del nuevo chat.
 * 
 * static String[] notifyUsers(String msg)
 * -> Obtiene la última IP conocida de los participantes de un chat
 * 
 * static Map getRegisteredChats(String nick)
 * -> Obtiene la informacion de todos los chats en los que participa un usuario.
 * 
 * static void saveNormalChat(String msg, String chatid)
 * -> Crea un registro en chat, participants y message para un nuevo chat entre
 * dos usuarios.
 * 
 * static ArrayList getGroupParticipants(String id)
 * -> Obtiene la última IP conocida de los participantes de un grupo especifico
 * 
 * static String changeGroup(String users, String action, String id)
 * -> Dependiendo del valor de action, se modifica un grupo añadiendo o quitando 
 * ussuarios.
 * 
 * @authors Mateo Crespi and Javier Palacios Botejara
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
    
    private static boolean devmode = false;
    
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
            Encriptador encriptador = new Encriptador(password);
            
            if (encriptador.getHashedPassword().equals("")) return "SP";
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                
                rs = st.executeQuery("SELECT password FROM users WHERE username='" + nick + "'");
                if (rs.next()){
                    String hashed = rs.getString("password");
                    boolean correctPassword = encriptador.validadorContrasenya(password, hashed);
                    if(correctPassword){
                        return "OK";
                    } else {
                        return "X";
                    }
                } else {
                    return "X";
                }
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return "SP";} 
            catch (NoSuchAlgorithmException ex) {             
                if(devmode) Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                return "SP";
            } catch (InvalidKeySpecException ex) {
                if(devmode) Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                return "SP";
            }             
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
                
                Encriptador encriptador = new Encriptador(data[4]);
                if (encriptador.getHashedPassword().equals("")) error += "\n\tParse error";
                
                if(error.equals("")){
                    ps = conn.prepareStatement("INSERT INTO `users` (`username`, `password`, `email`, `name`, `surname`, `last_ip`, `genre`, `image`) VALUES (?, ?, ?, ?, ?, '', ?, 'null')");
                    ps.setString(1, data[0]);
                    ps.setString(2, encriptador.getHashedPassword());
                    ps.setString(3, data[2]);
                    ps.setString(4, data[3]);
                    ps.setString(5, data[1]);
                    ps.setString(6, data[5]);
                    ps.executeUpdate();
                }
                
                return new String[]{error, "", data[0]};
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return serverError;}             
        } catch (ClassNotFoundException e) {return serverError;}        
        finally { try {conn.close();} catch (SQLException ex) {return serverError;} catch (Exception ex) {return serverError;}}
    }
       
    static void setLastIP(String nick, String ip) {
        try{     
            Class.forName(driver);
        try {                
                final String query = "UPDATE `users` SET `last_ip`='"+ip+"' WHERE username='"+nick+"'";
                
                conn = DriverManager.getConnection(url, user, pass);
                ps = conn.prepareStatement(query);      
                ps.executeUpdate();
                         
        } catch (SQLException ex) {
            if(devmode) ex.printStackTrace();
        }  
        } catch (ClassNotFoundException ex) {
            if(devmode) ex.printStackTrace();
        }     
        finally { try {conn.close();} catch (SQLException ex) {
            if(devmode) ex.printStackTrace();
        }} 
    }
    
    static String[] getChats(String nick) {
        String[] result = {"", "ERROR!"};
        try {
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT messages.chat_id, message FROM messages LEFT JOIN participants ON messages.chat_id = participants.chat_id WHERE participants.user_id= '" + nick + "'");
                               
                if (rs.next()) {
                    result[0] = rs.getString(1);
                    result[1] = rs.getString(2);
                    return result;
                 
                } else {result[1] = ""; return result;}  
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return result;}             
        } catch (ClassNotFoundException e) {return result;}         
        finally { try {conn.close();} catch (SQLException ex) {return result;} catch (Exception ex) {return result;}}
    }

    static String getUsers(String msg, String id) {
        String allUsers = "";
        String query = "";
        if(msg.equals("all")){ query = "SELECT username FROM users";}
        else if(msg.equals("add")){ query = "SELECT user_id FROM participants WHERE chat_id != '"+id+"'";}
        else if(msg.equals("del")){ query = "SELECT user_id FROM participants WHERE chat_id = '"+id+"'";}
        
        try {
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery(query);
                               
                while (rs.next()) { allUsers += "~"+rs.getString(1);}
                return allUsers;
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return "S";}             
        } catch (ClassNotFoundException e) {return "S";}         
        finally { try {conn.close();} catch (SQLException ex) {return "S";} catch (Exception ex) {return "S";}}
        
    }

    static String createNewGroup(String msg, String nick) {
        //(!)Catch error if no username is send
        String serverError ="";
        String id =  "~g~"+new Date().getTime()+Math.random();
        
        String chat = "INSERT INTO `chats` (`chat_id`, `chat_name`) VALUES (?, ?)";
        
        int j = msg.split("~")[0].equals("") ? 1 : 0; 
        String join = "INSERT INTO `participants` (`chat_id`, `user_id`) VALUES ('"+id+"', '"+msg.split("~")[j]+"')";        
        for(int i = 1+j; i < msg.split("~").length; i++){
            join += ", ('"+id+"', '"+msg.split("~")[i]+"')";
        }
        
        try{     
            Class.forName(driver);
            try{
                //chats: chat_id 	chat_name
                conn = DriverManager.getConnection(url, user, pass);
                ps = conn.prepareStatement(chat);
                ps.setString(1, id);
                ps.setString(2, nick);
                ps.executeUpdate(); 

//                return "OK";
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return serverError;}             
            finally{
                try{
                    //participants: user_id 	chat_id 
                    conn = DriverManager.getConnection(url, user, pass);
                    ps = conn.prepareStatement(join);
                    ps.executeUpdate(); 

                    return id;

                } catch (SQLException ex) {
                    if(devmode) ex.printStackTrace();
                    return serverError;} 
            }  
        } catch (ClassNotFoundException e) {return serverError;}          
        finally { try {conn.close();} catch (SQLException ex) {return serverError;} catch (Exception ex) {return serverError;}}
        
    }

    static String[] notifyUsers(String msg) {
        String query = "";
        String[] ips;
        
        query = "Select last_ip FROM users WHERE username IN ('"+msg.split("~")[0]+"'";
        for(int i = 1; i < msg.split("~").length; i++){
            query += ", '"+msg.split("~")[i]+"'";
        }   query +=")";            
        ips = new String[msg.split("~").length];
        
        try{     
            Class.forName(driver);
          try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery(query);
                int i = 0;               
                while (rs.next()) { ips[i] = rs.getString(1); i++;}
                return ips;
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return new String[]{"S"};}     
          catch ( Exception e){return new String[]{"S"};}
        }catch (ClassNotFoundException e) {return new String[]{"S"};}         
        finally { try {conn.close();} catch (SQLException ex) {return new String[]{"S"};} catch (Exception ex) {return new String[]{"S"};}}

    }

    static Map getRegisteredChats(String nick) {
        /**
         * Filter with user, recibe: chat_name, chat_id, message
         */
        String query = "Select chats.chat_id, chats.chat_name, messages.message FROM chats LEFT JOIN participants ON chats.chat_id = participants.chat_id LEFT JOIN messages ON messages.chat_id = chats.chat_id WHERE participants.user_id = '"+nick+"'";
        Map groups = new HashMap();
        try{     
            Class.forName(driver);
          try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery(query);
                
                while (rs.next()) { groups.put(rs.getString(1), new String[]{rs.getString(2),rs.getString(3)});}
                return groups;
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return new HashMap();}     
             catch ( Exception e){return  new HashMap();}
        }catch (ClassNotFoundException e) {return  new HashMap();}         
        finally { try {conn.close();} catch (SQLException ex) {return  new HashMap();} catch (Exception ex) {return  new HashMap();}}
    }

    static void saveNormalChat(String msg, String chatid) {
        String query = "SELECT chat_id FROM participants WHERE chat_id= '"+chatid+"'";

        try{     
            Class.forName(driver);
          try {
                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery(query);
                
                if(!rs.next() && !chatid.contains("~guest")){                    
                    String addChat = "INSERT INTO chats (`chat_id`, `chat_name`) VALUES ('"+chatid+"','NOONECARES');";
                    ps = conn.prepareStatement(addChat);
                    ps.executeUpdate(); 
                
                    addChat= "INSERT INTO messages (`chat_id`) VALUES ('"+chatid+"');";
                    ps = conn.prepareStatement(addChat);
                    ps.executeUpdate(); 
                    
                    addChat = "INSERT INTO participants (`chat_id`, `user_id`) VALUES ('"+chatid+"','"+chatid.split("~")[0]+"'),('"+chatid+"','"+chatid.split("~")[1]+"');";
                    ps = conn.prepareStatement(addChat);
                    ps.executeUpdate(); 
                } 
                //Limit????
                String newmsg = "";
                //username, chat_id, message
                String getMsg = "SELECT message FROM messages WHERE chat_id = '"+chatid+"'";
                st = conn.createStatement();
                rs = st.executeQuery(getMsg);
                if(rs.next()) {newmsg = rs.getString(1)+msg;}
                
                String addMsg = "UPDATE messages SET `message`='"+newmsg+"' WHERE chat_id='"+chatid+"'";
                ps = conn.prepareStatement(addMsg);
                ps.executeUpdate();
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace();
            }     
             catch ( Exception ex){
                 if(devmode) ex.printStackTrace();
             }
        }catch (ClassNotFoundException ex) {
            if(devmode) ex.printStackTrace();
        }         
        finally { try {conn.close();} catch (SQLException ex) {
            if(devmode) ex.printStackTrace();
        } catch (Exception ex) {
            if(devmode) ex.printStackTrace();
        }}
   
    }

    static ArrayList getGroupParticipants(String id) {
        ArrayList ips = new ArrayList();
        String query = "Select last_ip FROM users INNER JOIN participants ON users.username = participants.user_id WHERE participants.chat_id = '"+id+"'";
        try{     
            Class.forName(driver);
          try {
            conn = DriverManager.getConnection(url, user, pass);
            st = conn.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next()) {
                if(!ips.contains(rs.getString(1))){ips.add(rs.getString(1));}
            }

            return ips;
        
        } catch (SQLException ex) {
            if(devmode) ex.printStackTrace(); 
            return ips;}     
             catch ( Exception ex){
                 if(devmode) ex.printStackTrace(); 
                 return ips;}
        }catch (ClassNotFoundException ex) {
            if(devmode) ex.printStackTrace(); 
            return ips;}         
        finally { try {conn.close();} catch (SQLException ex) {
            if(devmode) ex.printStackTrace(); 
            return ips;} 
        catch (Exception ex) {
            if(devmode) ex.printStackTrace(); 
            return ips;}
        }
    }

    static String changeGroup(String users, String action, String id) {
        String allgroupmembers="";
        String query = "";
        try {
            Class.forName(driver);
            try {
                if(action.equals("add")){                    
                    int j = users.split("~")[0].equals("") ? 1 : 0; 
                    query = "INSERT INTO `participants` (`chat_id`, `user_id`) VALUES ('"+id+"', '"+users.split("~")[j]+"')";        
                    for(int i = 1+j; i < users.split("~").length; i++){
                        query += ", ('"+id+"', '"+users.split("~")[i]+"')";
                    }           
                    
                } else{
                    query="DELETE FROM participants WHERE chat_id = '"+id+"' AND user_id IN ('"+users.split("~")[0]+"'";
                    for(int i = 1; i < users.split("~").length; i++){
                        query += ", '"+users.split("~")[i]+"'";
                    }   query +=")";
                }

                if(devmode) System.out.println(query);
                
                conn = DriverManager.getConnection(url, user, pass);
                ps = conn.prepareStatement(query);
                ps.executeUpdate(); 
                
                if(action.equals("del")){return users;}
                
                query = "SELECT user_id FROM participants WHERE chat_id != '"+id+"'";        

                conn = DriverManager.getConnection(url, user, pass);
                st = conn.createStatement();
                rs = st.executeQuery(query);

                while (rs.next()) { allgroupmembers += "~"+rs.getString(1);}

                return allgroupmembers;
                
            } catch (SQLException ex) {
                if(devmode) ex.printStackTrace(); 
                return "S";}             
        } catch (ClassNotFoundException e) {return "S";}         
        finally { try {conn.close();} catch (SQLException ex) {return "S";} catch (Exception ex) {return "S";}}
        
    }
}