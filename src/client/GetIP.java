package client;

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
 * @author Javier Palacios Botejara, Houssam Amrouch, Mateo Crespi, Cristian Echauri, Mildred Rámirez
 */
public class GetIP {
    //obtencion de ip publica
    public static String getPublicIP() {
        String publicIP;
        //Recoje la ip publica y mira si esta conectado y la devuelve en un string publicIP
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
        //Arrary de String para guardar la interfaces que estan en línea
        ArrayList<String> localIP = new ArrayList<>(); 
        try {
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                // Debido a la cantidad de interfaces, solo imprimiremos las que están en línea
                if (iface.isUp()) {localIP.add(iface.getInetAddresses().nextElement().getHostAddress());}                
            }           
        } catch (SocketException ex) {
            Logger.getLogger(GetIP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return localIP;
    }
}
 
