/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package client.chat;

/**
 *
 * @author Javier Palacios Botejara
 */
import packager.Package;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class Chat extends JFrame implements ActionListener, KeyListener{
        
    Container content = getContentPane();
    Container sideBar = getContentPane();
        
    JPanel users = new JPanel();
    JPanel win = new JPanel();
    JPanel input = new JPanel();
    JPanel text = new JPanel();
    JFrame frame = new JFrame();
         
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    ImageIcon send = new ImageIcon("send.png");
    ImageIcon erase = new ImageIcon("erase.png");
    ImageIcon exit = new ImageIcon("exit.png");        
        
    JButton sendbtn = new JButton("Send", send);
    JButton exitbtn = new JButton("Exit", exit);
    JButton erasebtn = new JButton("Clean", erase);
        
    JTextField field = new JTextField(38);
    JTextArea txtArea = new JTextArea(40,115);
    
    JScrollPane userPane = new JScrollPane();
    JPanel allusers = new JPanel();

    String serverIP;
    JList userOnline;
    DefaultListModel model = new DefaultListModel();;
     
    public Chat() {
        super("Chatty");
        serverIP = (String) JOptionPane.showInputDialog(this, "Introduce ip del servidor");
        serverConnect();
        new RecieveMsg();
        
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
                    
            sendbtn.addActionListener(this);
            exitbtn.addActionListener(this);
            erasebtn.addActionListener(this);
                    
            field.addKeyListener(this);
            
            setSize(screenSize.width, screenSize.height-60);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
            setLocationRelativeTo(null);	
    }  
    
    public void serverConnect(){
        try {
            Socket mySocket = new Socket(serverIP,9999);
            Package p = new Package();
            p.setStatus("online");
            
            ObjectOutputStream objp = new ObjectOutputStream(mySocket.getOutputStream());
            objp.writeObject(p);
            mySocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
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

                        txtArea.append(p.getMsg()+"\n");
                        
                        for(String user : p.getIps()){
//                            boolean inside = false;
//                            String userIP = GetIP.getLocalIp()+","+GetIP.getPublicIP();
//                            System.out.println(userIP);
//                            
//                            for(int i = 0; i< model.getSize(); i++){
//                                System.out.println(model.getSize());
//                                if(model.get(i).equals(user)||userIP.contains(user)){inside = true;}
//                            }
                            boolean own = GetIP.getPublicIP().contains(user) || GetIP.getLocalIp().contains(user);
                            if(!model.contains(user) && !own){model.addElement(user);}
//                            if(!inside){model.addElement(user);}
                        }
                        
                    } catch(Exception e){}                    
                }
            } catch (Exception e){}
        }
        
    }
        
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