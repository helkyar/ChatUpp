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
                try (Socket socket = port.accept()) {
                    
                    ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                    p = (Package) entrada.readObject();
                    
                    if(p.getStatus().equals("online")){sayHelloToChat(socket, p);}
                    else if (p.getStatus().equals("login")){checkLogin(socket, p);}
                    else if (p.getStatus().equals("register")){registerUser(socket, p);}
                    else if (p.getStatus().equals("getusers")){sendUsersOnline(socket, p);}
                    else if (p.getStatus().equals("messaging")){sendMessage(socket, p);}
                    
                } catch (ClassNotFoundException ex) {System.out.println("Class not found");}
                 catch(EOFException ex){System.out.println("Wrong chat connection protocol");}
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }
    
    private void sayHelloToChat(Socket socket, Package p) throws IOException{
        InetAddress locateip = socket.getInetAddress();
        String getip = locateip.getHostAddress();
                        
        txt.append("New connection: "+getip);
        p.setStatus("imserver");   
        Socket sendmsg = new Socket(getip, 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); socket.close();
    }
    
    private void checkLogin(Socket socket, Package p) throws IOException{
        
        p.setMsg(DBConnection.checkLogin(p.getMsg(), p.getNick()));
        
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); socket.close();
    }

    private void registerUser(Socket socket, Package p) throws IOException{
        String[] data = DBConnection.registerUser(p.getMsg(), p.getNick());
        p.setMsg(data[0]);
        p.setInfo(data[1]);
        p.setNick(data[2]);
         
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); socket.close();        
    }
    
    private void sendUsersOnline(Socket socket, Package p) throws IOException{
        Map<String, String> ips = new HashMap<>();        
        InetAddress locateip = socket.getInetAddress();
        String getip = locateip.getHostAddress();
        
        if(!ips.containsValue(getip)){ips.put(p.getNick(), getip);}        
        p.setIps(ips);
                            
        for(String userip:ips.values()){
            Socket sendmsg = new Socket(userip, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close(); socket.close();
        }
    }
    
    private void sendMessage(Socket socket, Package p) throws IOException{

        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); socket.close();
    }
}