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
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Timer;

/**
 * 
 * @author Javier Palacios
 */
public class Chat extends JFrame implements ActionListener{
    
    public static final ImageIcon CHATLOGO = new ImageIcon("img/logo.png");
    public static final Image LOGO = CHATLOGO.getImage();
    
    private JPanel chat = new JPanel();
    private JPanel options = new JPanel();
    private JPanel input = new JPanel(); //facilitate adding componnents
    private JPanel connect = new JPanel();
    private JPanel users = new JPanel();
    private JPanel groups = new JPanel();
    private JScrollPane scrollUsers;
    private JScrollPane scrollGroups; 
        
    private JButton sendbtn = new JButton(new ImageIcon("img/send.png"));
    private JButton exitbtn = new JButton("X");
    private JButton erasebtn = new JButton("#");
    private JButton login = new JButton("Login");
    private JButton register = new JButton("Register");
    
    private JTextField userinput = new JTextField(38);
    private JTextArea chatxt = new JTextArea(20,50);
        
    private JTextArea userInfo;
    private JFrame infoPopup;    
    //setting it as static to prevent lossing reference on session change doesn't work
    public static JFrame sessionFrame;
    
    private String serverIP = "";
    private boolean onlyonce = true;
    private String adress = "";
    private String nick = "~guest~";
    private JLabel nickLabel = new JLabel("username: "+nick);
     
    public Chat() {
        
    //POP-UP textarea___________________________________________________ 
        userInfo = new JTextArea(7,20); 
        userInfo.setEditable(false);
        
    //Set Panels_________________________________________________________
        setLayout(new BorderLayout(5,5));
        connect.setLayout(new GridLayout(2,1));
        scrollUsers = new JScrollPane(users, 22,31); //vertical always [20 for needed], horizontal never
        scrollGroups = new JScrollPane(groups, 22,31); //vertical always [20 for needed], horizontal never
        
    //Dimension and Layout_______________________________________________    
        users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
        groups.setLayout(new BoxLayout(groups, BoxLayout.Y_AXIS));
        scrollUsers.setPreferredSize(new Dimension(115,210));
        scrollGroups.setPreferredSize(new Dimension(115,210));
        
        chat.setLayout(new BorderLayout(10,10));
        input.setLayout(new FlowLayout());
        chatxt.setEditable(false);
    
    //Add them___________________________________________________________
        connect.add(scrollUsers);
        connect.add(scrollGroups);
               
        input.add(sendbtn);
        input.add(userinput);
        input.add(nickLabel);
        chat.add("Center",chatxt);
        chat.add("South",input);
        options.add(login);
        options.add(register);
        options.add(erasebtn);
        options.add(exitbtn);
        
        add("North", options);
        add("Center", chat);
        add("West", connect);
        add("East", new JPanel()); //Just for style
                    
    //LISTENERS ____________________________________________________
        sendbtn.addActionListener(this);  
        erasebtn.addActionListener(this);    
        exitbtn.addActionListener(this);    
        login.addActionListener(this);    
        register.addActionListener(this);    
        userinput.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent pressed){sendToChat(pressed);}            
        });
            
    //FRAME _________________________________________________________            
        setTitle("Chatty");     
        setIconImage(LOGO);
        setSize(800,600);
           
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
    //=====================================================================
    //                      SERVER CONNECTION         
    //=====================================================================
        infoPopup = new Information();
        new RecieveMsg();
        getServerIP();
    }  
           
    private void getServerIP() {
        //Get user local ip
        if(serverIP.equals("")){
            userInfo.setText("\n   Starting connection...\n");
            String ip = (String) GetIP.getLocalIp().get(1);
            ip = ip.substring(0, ip.lastIndexOf(".")+1);
            //Test last 255 local ips searching for server
            for(int i = 0; i<=255; i++){
                Thread t = new Thread(new SearchServer(i, ip));

                java.util.Timer timer = new java.util.Timer();
                timer.schedule(new KillSearchThread(t, timer), 100);
                t.start();
            }           
            userInfo.append("   Waiting response....\n");
        }
        
        //Set message in case it takes too much
        new Timer(15000, (ActionEvent e) -> {
            userInfo.append("   Maybe your Internet is down?\n");
            userInfo.append("   Or our server is fucked...\n"); 
            if(!serverIP.equals("")){((Timer)e.getSource()).stop();}
        }).start();
    }
//=====================================================================
//                     USER SESSION
//=====================================================================
    private void  processSessionStart(){ 
        if(onlyonce){
            StartOptions options = new StartOptions();
            JOptionPane.showOptionDialog(this, options, "Select a piece", 1, 1, CHATLOGO, new Object[]{},null);
            String slc = options.getSelection();

            if(slc.equals("Login")){sessionFrame = new Login();}
            else if(slc.equals("Register")){sessionFrame = new Register();}
            onlyonce = false;
        }
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
                        else if(p.getStatus().equals("login")){setLoginMessage(p);}
                        else if(p.getStatus().equals("register")){setRegisterMessage(p);}                        
                        else if(p.getStatus().equals("getusers")){setUsersOnline(p);}
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

        userInfo.append("   Use responsibly, don't be a jerk  ;)");
        try { Thread.sleep(3000);} catch (InterruptedException ex) {}
        //Close Server Connection Info POP-UP
        infoPopup.dispatchEvent(new WindowEvent(infoPopup, WindowEvent.WINDOW_CLOSING));
        new Send(serverIP);
        processSessionStart();
    }
    
    private void setLoginMessage(Package p) {
        infoPopup = new Information();
        if(p.getMsg().equals("OK")){
            userInfo.setText("\n\tLogin successfull!!");
            //Ask server to get users and send current user
            askForUsersOnline(p.getNick());
        
        }
        else if(p.getMsg().equals("X")){userInfo.setText("\n\tWrong credentials");}        
        else {userInfo.setText("\n\tServer error, please restart");}  
        
        try { Thread.sleep(2000);} catch (InterruptedException ex) {}
        //Close Login Info POP-UP
        infoPopup.dispatchEvent(new WindowEvent(infoPopup, WindowEvent.WINDOW_CLOSING));
        //Close Login/Register window
        if(p.getMsg().equals("OK"))
        sessionFrame.dispatchEvent(new WindowEvent(sessionFrame, WindowEvent.WINDOW_CLOSING));        
    }
    
    private void setRegisterMessage(Package p) {
        infoPopup = new Information();
        if(p.getInfo().equals("")){
            userInfo.setText("\n\tRegister successfull!!");
            //Ask server to get users and send current user
            askForUsersOnline(p.getNick());
            
        } else{userInfo.setText(p.getMsg()+p.getNick());}         
        
        try { Thread.sleep(5000);} catch (InterruptedException ex) {}
        //Close Login Info POP-UP
        infoPopup.dispatchEvent(new WindowEvent(infoPopup, WindowEvent.WINDOW_CLOSING));
        //Close Login/Register window
        if(p.getMsg().equals(""))
        sessionFrame.dispatchEvent(new WindowEvent(sessionFrame, WindowEvent.WINDOW_CLOSING));
    }
    
    private void askForUsersOnline(String nick){
        nickLabel.setText("username: "+nick);
        Send.message((String) GetIP.getLocalIp().get(1), "", nick, "getusers");
    }
    
    private void setUsersOnline(Package p){
        for(String ip : p.getIps().keySet()){
            boolean own = (
//                GetIP.getPublicIP().contains(p.getIps().get(user)) || 
                GetIP.getLocalIp().contains(ip)
            );
            
          if(!own){                       
                JToggleButton btn = new JToggleButton(p.getIps().get(ip), CHATLOGO);
                btn.addActionListener((ActionEvent e) -> {
                    adress = ip;                
                });

                users.add(btn);         
                users.setVisible(false);
                users.setVisible(true);  
            }
        }            
            
    }
       
    private void sendMessage(Package p){
        chatxt.append(p.getMsg()+"\n");
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
        if(adress.length()>1 && event.getSource() == sendbtn){
            try { 
                    Send.message( adress,userinput.getText(), "", "messaging");

                    chatxt.append(userinput.getText() + "\n");
                    userinput.setText("");
            } catch (Exception e){e.printStackTrace();}
        }else if(event.getSource() == erasebtn){chatxt.setText("");}
        else if(event.getSource() == exitbtn){System.exit(0);}
        else if(event.getSource() == login){sessionFrame = new Login();}
        else if(event.getSource() == register){sessionFrame = new Register();}
        else { JOptionPane.showMessageDialog(this, "Select a chat");}
    }
    
    public void sendToChat(KeyEvent pressed) {
        if(adress.length()>1){
            try {
                if(pressed.getKeyCode() == KeyEvent.VK_ENTER){
                    Send.message( adress, userinput.getText(), "", "messaging");

                    chatxt.append(userinput.getText() + "\n");
                    userinput.setText("");
                }  
            } catch (Exception e){e.printStackTrace();}
        }else { JOptionPane.showMessageDialog(this, "Select a chat");}
    }
}