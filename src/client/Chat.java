/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package client;

/**
 *
 * @author Javier Palacios Botejara
 */
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

import java.util.Timer;

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

    String serverIP;
    JList userOnline;
    DefaultListModel model = new DefaultListModel();
    
    JTextArea userInfo = new JTextArea(7,20);
    
    JFrame infoFrame;
     
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
        
        //USER LOGIN ==========================================================            
           JOptionPane.showOptionDialog(this, new SessionStart(), "Select a piece", 1, 1, CHATLOGO, new Object[]{},null);
           processSessionStart(SessionStart.getSelection());
        //=====================================================================
        //SERVER CONNECTION ===================================================
            infoFrame = new Information();
            new RecieveMsg();
            getServerIP();
        //=====================================================================
    }  
    
    private void  processSessionStart(String slc){
        System.out.println(slc);
        if(slc.equals("Login")){new Login().setVisible(true);}
        else if(slc.equals("Register")){new Register().setVisible(true);}
    }
    
    class Information extends JFrame{
        Information(){
            setLayout(new BorderLayout());
            
            JButton btn = new JButton("Retry");            
            userInfo.setBackground(Color.black);
            userInfo.setForeground(Color.green);
            btn.addActionListener((ActionEvent e) -> {getServerIP();});
            
            add("Center",userInfo);
            add("South",btn);
            setUndecorated(true);
            setLocationRelativeTo(null);
            pack();
            setVisible(true);
        }
        
    }
    
    class SearchServer implements Runnable {
        int i;
        String ip;
        
        SearchServer(int i, String ip){
            this.ip = ip;
            this.i = i;
        }
        
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    try (Socket socket = new Socket(ip+i,9999)) {
                        Package p = new Package();
                        p.setStatus("online");

                        ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                        objp.writeObject(p);
                        socket.close();
                    } 

                } catch (IOException ex) {/*System.out.println("Server tested: "+ip+i);*/}
            }
        }
    }
    
    class KillSearchThread extends TimerTask {
        private Thread t;
        private Timer timer;

        KillSearchThread(Thread t, Timer timer){
            this.t = t;
            this.timer = timer;
        }

        public void run() {
            if (t != null && t.isAlive()) {
                t.interrupt();
                timer.cancel();
            }
        }
    }
    
    private void getServerIP() {
        userInfo.setText("\n   Starting connection...\n");
        String ip = (String) GetIP.getLocalIp().get(1);
        ip = ip.substring(0, ip.lastIndexOf(".")+1);
        
        for(int i = 0; i<=255; i++){
            Thread t = new Thread(new SearchServer(i, ip));
            
            Timer timer = new Timer();
            timer.schedule(new KillSearchThread(t, timer), 100);
            t.start();
        }
        
        userInfo.append("   Waiting response....\n");
    }

    public void sendMsg() {
            
            try {
                try (Socket socket = new Socket(serverIP,9999)) {
                    Package p = new Package();
                    p.setNick("");
                    try{p.setIp(userOnline.getSelectedValue().toString());}catch(Exception e){} //***
                    p.setMove("");
                    p.setStatus("messaging");
                    p.setMsg(field.getText());
                    
                    ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                    objp.writeObject(p);
                    socket.close();
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    
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
                        
                        if(p.getStatus().equals("imserver")){setServerIP(mysocket, p);}
                        if(p.getStatus().equals("messaging")){sendMessage(p);} 
                        
                    } catch(Exception e){}                    
                }
            } catch (Exception e){}
        }      
        
        private void setServerIP(Socket mysocket, Package p) {
            userInfo.append("   Setting connection \n");
            
            InetAddress locateip = mysocket.getInetAddress();
            serverIP = locateip.getHostAddress();            
            
            for(String user : p.getIps()){
                boolean own = GetIP.getPublicIP().contains(user) || GetIP.getLocalIp().contains(user);
                if(!model.contains(user) && !own){model.addElement(user);}
            }
            
            userInfo.append("   Use responsibly, don't be a jerk  ;)");
            closeInfoPanel();
        }
        
        private void sendMessage(Package p){
            txtArea.append(p.getMsg()+"\n");

        }     
        
        private void closeInfoPanel(){
            try { Thread.sleep(5000);} catch (InterruptedException ex) {}
            infoFrame.dispatchEvent(new WindowEvent(infoFrame, WindowEvent.WINDOW_CLOSING));
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
                sendMsg();
                
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
                sendMsg();
                
                txtArea.append(field.getText() + "\n");
                field.setText("");
            }  
        } catch (Exception e){e.printStackTrace();}
    }
    
    public void keyTyped (KeyEvent typing){}
    public void keyReleased(KeyEvent released){}
}