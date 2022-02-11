/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author admin
 */
public class Send {
    private static String server;
    
    public Send(String server){
        this.server = server;
    }
    
    /**
     * 
     * @param adress IP adress where the message is going to be send
     * @param msg message to be send
     * @param nick username of the remitent
     * @param status status of the request (server filters with this param)
     */
    public static void message(String adress, String msg, String nick, String status) {
            
        try {
            try (Socket socket = new Socket(server,9999)) {
                packager.Package p = new packager.Package();
                p.setNick(nick);
                p.setIp(adress);
                p.setStatus(status);
                p.setMsg(msg);
                    
                ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                objp.writeObject(p);
                socket.close();
            }
               
        } catch (IOException ex) {ex.printStackTrace();}
    }
}
