/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.EOFException;
import packager.Package;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author javip
 */
public class Server extends JFrame implements Runnable{
    JTextArea txt = new JTextArea();
    Map<String, String> ips = new HashMap<>(); 
    
    Server(){
        Thread lintening = new Thread(this);
        lintening.start();
        
        add(txt);
        setSize(100,200);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Server();
    }    

    @Override
    public void run() {
        try {
            ServerSocket port = new ServerSocket(9999);
            String nick, move, msg;
            Package p;
            
            while(true){
                try (Socket request = port.accept()) {
                    
                    ObjectInputStream entrada = new ObjectInputStream(request.getInputStream());
                    p = (Package) entrada.readObject();
                    
                    if(p.getStatus().equals("online")){sayHelloToChat(request, p);}
                    else if (p.getStatus().equals("login")){checkLogin(p);}
                    else if (p.getStatus().equals("register")){registerUser(p);}
                    else if (p.getStatus().equals("getusers")){sendUsersOnline(request, p);}
                    else if (p.getStatus().equals("messaging")){sendMessage(p);}
                    else if (p.getStatus().equals("creategroup")){sendAllUsers(p);}
                    else if (p.getStatus().equals("groupusers")){informGroupUsers(p);}
//                    else if (p.getStatus().equals("groupmessage")){sendGroupMessage(p);}
                    
                    request.close();
                    
                } catch (ClassNotFoundException ex) {System.out.println("Class not found");}
                 catch(EOFException ex){System.out.println("Wrong chat connection protocol");}
                catch(ConnectException ex){ex.printStackTrace();}
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }
    
    private void sayHelloToChat(Socket request, Package p) throws IOException{
        InetAddress locateip = request.getInetAddress();
        String getip = locateip.getHostAddress();
                        
        txt.append("New connection: "+getip);
        p.setStatus("imserver");   
        Socket sendmsg = new Socket(getip, 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close();
    }
    
    private void checkLogin(Package p) throws IOException{
        String resp = DBConnection.checkLogin(p.getMsg(), p.getNick());
        p.setMsg(resp);
        p.setIps(DBConnection.getGroups(p.getNick()));
        
        //if OK resgister ip as last for this user        
         if(resp.equals("OK")){DBConnection.setLastIP(p.getNick(), p.getIp());}
         
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); 
    }

    private void registerUser(Package p) throws IOException{
        String[] data = DBConnection.registerUser(p.getMsg(), p.getNick());
        p.setMsg(data[0]);
        p.setInfo(data[1]);
        p.setNick(data[2]);
        p.setIps(DBConnection.getGroups(data[2]));
        
         if(data[0].equals("")){DBConnection.setLastIP(data[2], p.getIp());}
        //if OK resgister ip as last for this user
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close();      
    }
    
    private void sendUsersOnline(Socket request, Package p) throws IOException{
        //Get host ip
        InetAddress locateip = request.getInetAddress();
        String getip = locateip.getHostAddress();
        String nick = p.getNick();
        //fetch chats from db
        String[] chats = DBConnection.getChats(nick);
        if(!chats[0].equals("")){p.setInfo(chats[0]); p.setMsg(chats[1]);}
        
        if(!ips.containsValue(getip)){ips.put(getip, nick);}        
        p.setIps(ips);
                            
        for(String userip:ips.keySet()){            
            System.out.println(userip);
            Socket sendmsg = new Socket(userip, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close();
        }
    }
    
    private void sendMessage(Package p) throws IOException{
        DBConnection.saveNormalChat(p.getMsg(),p.getInfo());
        
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close();
    }

    private void sendAllUsers(Package p)  throws IOException{
        p.setMsg(DBConnection.getAllUsers());
        
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); 
    }

    private void informGroupUsers(Package p)  throws IOException{
        p.setMsg(DBConnection.createNewGroup(p.getMsg(), p.getNick()));
        String[] ips = DBConnection.notifyUsers(p.getMsg());
        
        for(String userip:ips){      
            Socket sendmsg = new Socket(userip, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close();
        }
    }
    //GUARDAR MENSAJES INDIVIDUALES
    //REGISTRAR LOS CHATS INDIVIDUALES
    //GUARDAR MENSAJES GRUPALES
    //ENVIAR MENSAJES GRUPALES
}