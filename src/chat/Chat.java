/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package chat;

/**
 *
 * @author Javier Palacios Botejara
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public Chat() {
        super("Chatty");
        serverConnect();
        try {
            txtArea.setEditable(false);
            text.add(txtArea); 
            
            input.add(sendbtn);
            input.add(exitbtn);
            input.add(erasebtn);
            input.add(field);
                    
            allusers.setLayout(new BorderLayout());
            userPane = new JScrollPane(allusers);
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
            frame.pack();
            setVisible(true);
            setLocationRelativeTo(null);
            
        } catch (Exception e) {System.out.println(e);}
		
    }
    
    public void serverConnect(){
        try {
            Socket mySocket = new Socket("192.168.1.78",37127);
            Package p = new Package();
            p.setStatus("online");
            p.setIp("192.168.1.78");
            
            ObjectOutputStream objp = new ObjectOutputStream(mySocket.getOutputStream());
            objp.writeObject(p);
            mySocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMsg() {
            
            try {
                try (Socket socket = new Socket("192.168.1.78",37127)) {
                    Package p = new Package();
                    p.setNick("");
                    p.setIp("");
                    p.setMove("");
                    p.setStatus("messaging");
                    p.setMsg(field.getText());
                    
                    ObjectOutputStream objp = new ObjectOutputStream(socket.getOutputStream());
                    objp.writeObject(p);
                    socket.close();
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
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
                txtArea.append(field.getText() + "\n");
                sendMsg();
                field.setText("");
            } else if (event.getSource() == erasebtn){
                txtArea.setText("");
            }else if (event.getSource() == exitbtn){
                System.exit(0);
            }
        } catch (Exception e){System.out.println(e);}
    }
    
    public void keyPressed(KeyEvent pressed) {
         try {
            if(pressed.getKeyCode() == KeyEvent.VK_ENTER){
                txtArea.append(field.getText() + "\n");
                field.setText("");
            }  
        } catch (Exception e){System.out.println(e);}
    }
    
    public void keyTyped (KeyEvent typing){}
    public void keyReleased(KeyEvent released){}
}

class Package implements Serializable{
    
    private String nick, ip, move, msg, status;
    
    public void setNick(String nick){this.nick = nick;}
    public void setIp(String ip){this.ip = ip;}
    public void setMove(String move){this.move = move;}
    public void setMsg(String msg){this.msg = msg;}
    public void setStatus(String st){this.status = st;}
    public String getNick(){return nick;}
    public String getIp(){return ip;}
    public String getMove(){return move;}
    public String getMsg(){return msg;}
    public String getStatus(){return status;}
}
