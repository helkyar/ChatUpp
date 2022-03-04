/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class P2P{
    
    private static String sender;
    private static String receiver;
    private static int port;
    
    public P2P(String sender, String receiver, int port) {
        this.sender = sender;
        this.receiver = receiver;
        this.port = port;
    }
    
    /**
     * @param frame
     */
    public static void message(ImageIcon frame) {
            
        try {
            try (Socket socket = new Socket(receiver, port)) {
                packager.VideoPackage videopack = new packager.VideoPackage(sender, "videotransfer", scaleImage(frame, 100, 100));
                ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                objp.writeObject(videopack);
                //objp.flush();
                socket.close();
            }
               
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public static String getSender() {
        return sender;
    }

    public static void setSender(String sender) {
        P2P.sender = sender;
    }

    public static String getReceiver() {
        return receiver;
    }

    public static void setReceiver(String receiver) {
        P2P.receiver = receiver;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        P2P.port = port;
    }
    
    
    //This will keep the right aspect ratio.
    private static ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
    

}
