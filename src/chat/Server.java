/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
            String nick, ip, move, msg;
            ArrayList<String> ips = new ArrayList<String>();
            Package p;
            
            while(true){
                try (Socket mysocket = port.accept()) {
                    
                    ObjectInputStream entrada = new ObjectInputStream(mysocket.getInputStream());
                    p = (Package) entrada.readObject();
                    
                    if(p.getStatus().equals("online")){
                        InetAddress locateip = mysocket.getInetAddress();
                        String getip = locateip.getHostAddress();
                        
                        if(!ips.contains(getip)){
                            ips.add(getip);
                        }
                        
                        p.setIps(ips);
                    
                        for(String userip:ips){
                            Socket sendmsg = new Socket(userip, 9090);
                            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
                            msgpackage.writeObject(p);

                            msgpackage.close(); sendmsg.close(); mysocket.close();
                        }
                        
                    } else if (p.getStatus().equals("messaging")){
//                        nick = p.getNick();
                        ip = p.getIp();
                        
                        Socket sendmsg = new Socket(ip, 9090);
                        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
                        msgpackage.writeObject(p);
                        
                        msgpackage.close(); sendmsg.close(); mysocket.close();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}