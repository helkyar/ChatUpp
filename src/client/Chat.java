/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package client;

/**
 *
 * @author Javier Palacios Botejara
 */
import client.helpers.GetIP;
import client.helpers.KillSearchThread;
import client.helpers.SearchServer;
import client.login.Login;
import client.login.Register;
import packager.Package;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

/**
 * 
 * @author Javier Palacios
 */
public class Chat extends JFrame implements ActionListener, KeyListener{
        
    Container content = getContentPane();
    Container sideBar = getContentPane();
    
    JPanel users = new JPanel();
    JPanel win = new JPanel();
    JPanel input = new JPanel();
    JPanel text = new JPanel();
    JFrame frame = new JFrame();
         
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    ImageIcon send = new ImageIcon("img/send.png");
    ImageIcon erase = new ImageIcon("img/erase.png");
    ImageIcon exit = new ImageIcon("img/exit.png");        
        
    JButton sendbtn = new JButton("Send", send);
    JButton exitbtn = new JButton("Exit", exit);
    JButton erasebtn = new JButton("Clean", erase);
        
    JTextField field = new JTextField(38);
    JTextArea txtArea = new JTextArea(20,50);
    
//=======================================================================
    public static final ImageIcon CHATLOGO = new ImageIcon("img/logo.png");
    public static final Image LOGO = CHATLOGO.getImage();
    
    JScrollPane userPane = new JScrollPane();
    JPanel allusers = new JPanel();

    String serverIP = "";
    JList userOnline;
    DefaultListModel model = new DefaultListModel();
    
    private static JTextArea userInfo = new JTextArea(7,20); 
    JFrame infoFrame;
    public static JFrame sessionFrame;
     
    public Chat() {
        super("Chatty");     
        setIconImage(LOGO);
    // SHITTY SWING COMPONENTS WITH NO STYLE WHATSOEVER ========================
            userInfo.setEditable(false);
            
            txtArea.setEditable(false);
            text.add(txtArea); 
            
            input.add(sendbtn);
            input.add(exitbtn);
            input.add(erasebtn);
            input.add(field);
                    
            allusers.setLayout(new BorderLayout(10,10));
            
            userOnline = new JList<String>();
            userOnline.setModel(model);
            userPane = new JScrollPane(userOnline);
            users.add(userPane);            

            content.add("Center", text);
            content.add("South", input);
            content.add("East", users);
                    
            //LISTENERS ____________________________________________________
            sendbtn.addActionListener(this);
            exitbtn.addActionListener(this);
            erasebtn.addActionListener(this);
                    
            field.addKeyListener(this);
            
            //FRAME _________________________________________________________
            pack();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            setLocationRelativeTo(null);
        //=====================================================================
        
        //=====================================================================
        //                      SERVER CONNECTION         
        //=====================================================================
            infoFrame = new Information();
            new RecieveMsg();
            getServerIP();
    }  
           
    private void getServerIP() {
        userInfo.setText("\n   Starting connection...\n");
        String ip = (String) GetIP.getLocalIp().get(1);
        ip = ip.substring(0, ip.lastIndexOf(".")+1);
        
        for(int i = 0; i<=255; i++){
            Thread t = new Thread(new SearchServer(i, ip));
            
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new KillSearchThread(t, timer), 100);
            t.start();
        }        
        userInfo.append("   Waiting response....\n");
        
        new Timer(10000, (ActionEvent e) -> {
            userInfo.append("   Maybe your Internet is down?\n");
            userInfo.append("   Or our server is fucked...\n"); 
            if(!serverIP.equals("")){((Timer)e.getSource()).stop();}
        }).start();
    }
//=====================================================================
//                     USER SESSION
//=====================================================================
    private void  processSessionStart(){     
        StartOptions options = new StartOptions();
        JOptionPane.showOptionDialog(this, options, "Select a piece", 1, 1, CHATLOGO, new Object[]{},null);
        String slc = options.getSelection();
        System.out.println(slc);
        if(slc.equals("Login")){sessionFrame = new Login();}
        else if(slc.equals("Register")){sessionFrame = new Register();}
    }

//===========================================================================================
//                     RECIEVE MESSAGE 
//===========================================================================================
    class RecieveMsg implements Runnable{
       
        RecieveMsg(){
            Thread lintening = new Thread(this);
            lintening.start();
        }
        
        @Override
        public void run() {
            try {
                ServerSocket port = new ServerSocket(9090);
                String nick, ip, move, msg;
                Package p;

                while(true){
                    try (Socket mysocket = port.accept()) {
                        ObjectInputStream entrada = new ObjectInputStream(mysocket.getInputStream());
                        p = (Package) entrada.readObject();
                        
                        userInfo.append("   Server response: "+ p.getStatus()+"\n");
                        
                //=====================================================================================
                //                 SERVER RESPONSE MANAGING
                //=====================================================================================                        
                        if(p.getStatus().equals("imserver")){setServerIP(mysocket, p);}
                        else if(p.getStatus().equals("login")){setLoginMessage(p.getMsg());}
                        else if(p.getStatus().equals("register")){setRegisterMessage(p);}
                        else if(p.getStatus().equals("messaging")){sendMessage(p);} 
                //=====================================================================================                        
                    } catch(Exception e){}                    
                }
            } catch (Exception e){}
        }                     
    }
    
    /**
     * Sets the server ip after server response. To avoid scanning every
     * time a message has to be send. At the same time gets all ips 
     * connected to the server.
     * @param socket socket in witch the chat is listenning
     * @param package Object send by server (Serializable class Package)
     */
    private void setServerIP(Socket mysocket, Package p) {
        userInfo.append("   Setting connection \n");
            
        InetAddress locateip = mysocket.getInetAddress();
        serverIP = locateip.getHostAddress();            
            
        for(String user : p.getIps()){
            boolean own = GetIP.getPublicIP().contains(user) || GetIP.getLocalIp().contains(user);
            if(!model.contains(user) && !own){model.addElement(user);}
        }            
            
        userInfo.append("   Use responsibly, don't be a jerk  ;)");
        try { Thread.sleep(3000);} catch (InterruptedException ex) {}
        //Close Server Connection Info POP-UP
        infoFrame.dispatchEvent(new WindowEvent(infoFrame, WindowEvent.WINDOW_CLOSING));
        new Send(serverIP);
        processSessionStart();
    }
    
    private void setLoginMessage(String msg) {
        infoFrame = new Information();
        if(msg.equals("OK")){
            userInfo.setText("\n\tLogin successfull!!");
        }
        else if(msg.equals("X")){userInfo.setText("\n\tWrong credentials");}        
        else {userInfo.setText("\n\tServer error, please restart");}  
        
        try { Thread.sleep(1000);} catch (InterruptedException ex) {}
        //Close Login Info POP-UP
        infoFrame.dispatchEvent(new WindowEvent(infoFrame, WindowEvent.WINDOW_CLOSING));
        //Close Login/Register window
        if(msg.equals("OK"))
        sessionFrame.dispatchEvent(new WindowEvent(sessionFrame, WindowEvent.WINDOW_CLOSING));
    }
    
    private void setRegisterMessage(Package p) {
        infoFrame = new Information();
        if(p.getMsg().equals("")){
            userInfo.setText("\n\tRegister successfull!!");
        
        } else{userInfo.setText(p.getMsg()+p.getNick());}         
        
        try { Thread.sleep(5000);} catch (InterruptedException ex) {}
        //Close Login Info POP-UP
        infoFrame.dispatchEvent(new WindowEvent(infoFrame, WindowEvent.WINDOW_CLOSING));
        //Close Login/Register window
        if(p.getMsg().equals(""))
        sessionFrame.dispatchEvent(new WindowEvent(sessionFrame, WindowEvent.WINDOW_CLOSING));
    }
    
    private void sendMessage(Package p){
        txtArea.append(p.getMsg()+"\n");
    }     
    
    
//=======================================================================
// POP-UPS
//=======================================================================
    private class Information extends JFrame{
        Information(){            
            userInfo.setBackground(Color.black);
            userInfo.setForeground(Color.green);
            
            add(userInfo);
            setUndecorated(true);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
            
            if(serverIP.equals(""))
                new Timer(20000, (ActionEvent e) -> {
                    if(!serverIP.equals("")){((Timer)e.getSource()).stop();}
                    else{getServerIP();}                    
                }).start();
        }        
    }
    
    private class StartOptions extends JPanel implements ActionListener{   
    private String selection = "";
    
    public StartOptions (){
        JButton log = new JButton("Login");        
        JButton reg = new JButton("Register");        
        JButton guest = new JButton("Guesst");
        log.addActionListener(this);
        reg.addActionListener(this);
        guest.addActionListener(this);
        add(log);
        add(reg);
        add(guest);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        selection = e.getActionCommand();
        Window[] windows = Window.getWindows();
                for (Window window : windows) {
                    if (window instanceof JDialog) {
                        JDialog dialog = (JDialog) window;
                        if (dialog.getContentPane().getComponentCount() == 1
                            && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                            dialog.dispose();
                        }
                    }
                }
    }
    
    public String getSelection(){
        return selection;
    }
    
}
    
// ======================================================================
// MAIN
// ======================================================================
  /**
    * @param args the command line arguments
    */
    public static void main(String[] args){
	new Chat();
    }
        
// ======================================================================
// EVENTS
// ======================================================================
    public void actionPerformed(ActionEvent event) {
        try {                
            if(event.getSource() == sendbtn){
                Send.message(field.getText(), userOnline.getSelectedValue().toString(), "", "");
                
                txtArea.append(field.getText() + "\n");
                field.setText("");
            } else if (event.getSource() == erasebtn){
                txtArea.setText("");
            }else if (event.getSource() == exitbtn){
                System.exit(0);
            }
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void keyPressed(KeyEvent pressed) {
         try {
            if(pressed.getKeyCode() == KeyEvent.VK_ENTER){
                Send.message(field.getText(), userOnline.getSelectedValue().toString(), "", "");
                
                txtArea.append(field.getText() + "\n");
                field.setText("");
            }  
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void keyTyped (KeyEvent typing){}
    public void keyReleased(KeyEvent released){}
}