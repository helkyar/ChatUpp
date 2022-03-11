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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * La clase server se encarga de enviar la información entre clientes y realizar
 * las conecxiones con la base de datos usando la clase DBConnection.
 * 
 * Dependiendo del status de los Package recibidos, se ejecutará un metodo u otro.
 * 
 * private void sayHelloToChat(Socket request, Package p)
 * -> Cuando un cliente que está buscando al servidor encuentra la IP correcta,
 * este metodo le envía la confirmación de identidad.
 * 
 * private void checkLogin(Package p)
 * -> comprueba que los datos enviados en la ventana Login sean correctos.
 * 
 * private void registerUser(Package p)
 * -> Utilizando los datos enviados en la ventana Register, se intenta insertar
 * un registro en la tabla users
 * 
 * private void sendUsersOnline(Socket request, Package p)
 * -> informa a los clientes de los usuarios actualmente conectados a la aplicación.
 * 
 * private void sendMessage(Package p)
 * -> Envia el mensaje contenido en p a todos los participantes de un chat o un
 * grupo
 * 
 * private void informGroupUsers(Package p)
 * -> al crea un nuevo grupo, informar a todos los participantes añadidos.
 * 
 * private void changeGroupUsers(Package p)
 * -> al modificar un grupo, informa a todos los participantes involucrados en 
 * el grupo
 * 
 * @author Javier Palacios Botejara
 */
public class Server extends JFrame implements Runnable{
    private static int guest = 0;
    private JTextArea txt = new JTextArea();
    private Map<String, String[]> ips = new HashMap<>(); 
    private String ip = "";
    private boolean devmode = false;
    private final JScrollPane txtContainer = new JScrollPane(txt, 
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    Server(){
        Thread lintening = new Thread(this);
        lintening.start();
        
        add(txtContainer);
        setSize(100,200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                    //(!!!)careful ~guest~ may be send in the response
                    else if (p.getStatus().equals("messaging")){sendMessage(p);}
                    else if (p.getStatus().equals("managegroup")){sendUsers(p);}
                    else if (p.getStatus().equals("groupusers")){informGroupUsers(p);}
                    else if (p.getStatus().equals("changeusers")){changeGroupUsers(p);}                    
                    
//                    else if (p.getStatus().equals("groupmessage")){sendGroupMessage(p);}
                    
                    request.close();
                    
                } catch (ClassNotFoundException ex) {if(devmode)System.out.println("Class not found");}
                 catch(EOFException ex){if(devmode)System.out.println("Wrong chat connection protocol");}
                catch(ConnectException ex){if(devmode)ex.printStackTrace();}
            }
        } catch (IOException ex) {if(devmode)ex.printStackTrace();}
    }
    
    private void sayHelloToChat(Socket request, Package p) throws IOException{
        //Trying to save time cutting response
//        if(p.getIp().equals(ip)){return;}
//        ip = p.getIp();
        
        InetAddress locateip = request.getInetAddress();
        String getip = locateip.getHostAddress();
        txt.append("New connection: "+getip+"\n");
        p.setStatus("imserver");   
        Socket sendmsg = new Socket(getip, 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close();
    }
    
    private void checkLogin(Package p) throws IOException{
        String resp = DBConnection.checkLogin(p.getMsg(), p.getNick());
        p.setMsg(resp);
        p.setObj(DBConnection.getRegisteredChats(p.getNick()));
        
        //if OK resgister ip as last for this user        
         if(resp.equals("OK")){DBConnection.setLastIP(p.getNick(), p.getIp());}
         
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); 
    }

    private void registerUser(Package p) throws IOException{
        String[] data = DBConnection.registerUser(p.getMsg(), p.getNick());
        p.setObj(DBConnection.getRegisteredChats(data[2]));
        p.setMsg(data[0]);
        p.setInfo(data[1]);
        p.setNick(data[2]);
        
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
        //Set unique guest user
        if(nick.equals("~guest")){nick += guest+++"~";}
        p.setNick(nick);
        //fetch chats from db
        String[] chats = DBConnection.getChats(nick);
        if(!chats[0].equals("")){p.setInfo(chats[0]); p.setMsg(chats[1]);}
        //Avoid ip duplication
        if(!ips.containsValue(getip)){ips.put(getip, new String[]{nick,""});}        
        p.setObj(ips);
        p.setIp(p.getIp());
                            
        for(String userip:ips.keySet()){  
            Socket sendmsg = new Socket(userip, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close();
        }
    }
    
    private void sendMessage(Package p) throws IOException{
        DBConnection.saveNormalChat(p.getMsg(),p.getInfo());
        if(!p.getInfo().contains("~g~")){
            Socket sendmsg = new Socket(p.getIp(), 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);
            msgpackage.close(); sendmsg.close();
        }else{
           ArrayList ips = DBConnection.getGroupParticipants(p.getInfo());
           for (Object ip : ips.toArray()){
                Socket sendmsg = new Socket((String)ip, 9090);
                ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
                msgpackage.writeObject(p);
                msgpackage.close(); sendmsg.close();
           }
        }            
    }

    private void sendUsers(Package p)  throws IOException{
        p.setMsg(DBConnection.getUsers(p.getMsg(), p.getInfo()));
        
        Socket sendmsg = new Socket(p.getIp(), 9090);
        ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
        msgpackage.writeObject(p);
                        
        msgpackage.close(); sendmsg.close(); 
    }

    private void informGroupUsers(Package p)  throws IOException{
        p.setInfo(DBConnection.createNewGroup(p.getMsg(), p.getNick()));
        String[] ips = DBConnection.notifyUsers(p.getMsg());
        ArrayList<String> checker = new ArrayList<>();
        for(String userip:ips){            
            if(!checker.contains(userip)){checker.add(userip);}
        }
        for(int i = 0; i < checker.size(); i++){
            String userid = checker.get(i);
            Socket sendmsg = new Socket(userid, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close();
        }
    }   

    private void changeGroupUsers(Package p) throws IOException{
        //info = id; mesage = users; nick = action
        p.setMsg(DBConnection.changeGroup(p.getMsg(), p.getNick(), p.getInfo()));
        String[] ips = DBConnection.notifyUsers(p.getMsg()); //gets ip's of target user
        
        for(String userip:ips){      
            Socket sendmsg = new Socket(userip, 9090);
            ObjectOutputStream msgpackage = new ObjectOutputStream(sendmsg.getOutputStream());
            msgpackage.writeObject(p);

            msgpackage.close(); sendmsg.close();
        }
     }
}
 //~guest~ may be send in the response