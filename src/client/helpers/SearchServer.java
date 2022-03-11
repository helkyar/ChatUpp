/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.helpers;

import client.Chat;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * Esta clase se encarga de enviar Packages a IPs para comprobar si una de 
 * ellas es el servidor.
 * 
 * @author Javier Palacios Botejara
 */
public class SearchServer implements Runnable {
    int i;
    String ip;
    private boolean devmode = false;
        
    public SearchServer(int i, String ip){
        this.ip = ip;
        this.i = i;
    }
        
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                try (Socket socket = new Socket(ip+i,9999)) {
                    packager.Package p = new packager.Package();
                    p.setStatus("online");
                    ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                    objp.writeObject(p);
                    socket.close();
                } 
            } catch (IOException ex) {if(devmode) System.out.println("Server tested: "+ip+i);}
        }
    }
}
