package client.chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author Javier Palacios
 */
public class GetIP {
    
    public static String getPublicIP() {
        String publicIP;
    	try {
                URL tempURL = new URL("http://checkip.amazonaws.com/");
                HttpURLConnection tempConn = (HttpURLConnection)tempURL.openConnection();
                InputStream tempInStream = tempConn.getInputStream();
                InputStreamReader tempIsr = new InputStreamReader(tempInStream);
                BufferedReader tempBr = new BufferedReader(tempIsr);        
 
                publicIP = tempBr.readLine();
 
                tempBr.close();
                tempInStream.close();
 
        } catch (Exception ex) {
                publicIP = "Null";   
          }
 
         return publicIP;
    }
    
    public static ArrayList getLocalIp(){
        
        ArrayList<String> localIP = new ArrayList<>(); 
        try {
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                // Due to the amount of the interfaces, we will only print the ones online
                if (iface.isUp()) {localIP.add(iface.getInetAddresses().nextElement().getHostAddress());}                
            }           
        } catch (SocketException ex) {
            Logger.getLogger(GetIP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return localIP;
    }
}
 
