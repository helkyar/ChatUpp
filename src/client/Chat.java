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
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import org.slf4j.*;
import packager.Package;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.AttributedString;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Timer;
import packager.VideoPackage;

/**
 * 
 * @author Javier Palacios
 */
public class Chat extends JFrame implements ActionListener{
    
    private final DateFormat timeFormat = new SimpleDateFormat("kk:mm ");
    public static final ImageIcon CHATLOGO = new ImageIcon("img/logo.png");
    public static final Image LOGO = CHATLOGO.getImage();
    
    private final JPanel chat = new JPanel();
    private final JPanel options = new JPanel();
    private final JPanel screen = new JPanel(); //text chat and cam
    private final JPanel input = new JPanel(); //facilitate adding componnents
    private final JPanel connect = new JPanel();
    private final JPanel users = new JPanel();
    private final JPanel groups = new JPanel();
    private final JScrollPane scrollUsers;
    private final JScrollPane scrollGroups; 
        
    private ButtonGroup bg = new ButtonGroup();
    private final JButton sendbtn = new JButton(new ImageIcon("img/send.png"));
    private final JButton exitbtn = new JButton("X");
    private final JButton erasebtn = new JButton("#");
    private final JButton login = new JButton("Login");
    private final JButton register = new JButton("Register");
    private final JButton call = new JButton("Call");
    private final JButton newgroup = new JButton("         +          ");
    private final JButton addmember = new JButton("(+)Member");
    private final JButton delmember = new JButton("(-)Member");
    
    private final JTextField userinput = new JTextField(38);
    private final JTextArea chatxt = new JTextArea(20,50);
    private final JScrollPane chatTxtContainer = new JScrollPane (chatxt, 
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
    private JTextArea userInfo;
    private JFrame infoPopup;    
    //setting it as static to prevent lossing reference on session change doesn't work
    public static JFrame sessionFrame;
    
    private String serverIP = "";
    private boolean onlyonce = true;
    private String adress = "";
    private String nick = "~guest";
    private String chatID;
    private JLabel nickLabel = new JLabel("username: "+nick);
    private final String groupSlug = "~g~";
    
    //VIDEOCALL MANAGEMENT VARIABLES____________________________________________
    private String clientIp = (String) GetIP.getLocalIp().get(1);
    private ArrayList<String> callParticipants = new ArrayList<>();
    private ArrayList<P2P> socketCallList = new ArrayList<>();
    private AtomicBoolean onCall = new AtomicBoolean(); // default value is false
    private int videoCallPort = 9091;
    private JPanel camFrame;
    private static Map<String, JLabel> userCam = new HashMap<>(); 
    
    //DATA MANAGEMENT VARIABLES_______________________________________________
    private Map<String, String> chatstorage = new HashMap<>();
    private String groupUsers="";
    private String groupname;
    private int op = 2;
    
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
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));
        users.setLayout(new BoxLayout(users, BoxLayout.Y_AXIS));
        groups.setLayout(new BoxLayout(groups, BoxLayout.Y_AXIS));
        scrollUsers.setPreferredSize(new Dimension(115,210));
        scrollGroups.setPreferredSize(new Dimension(115,210));
        
        chat.setVisible(false);
        chat.setLayout(new BorderLayout(10,10));
        input.setLayout(new FlowLayout());
        chatxt.setEditable(false);
    
    //Add them___________________________________________________________
        connect.add(scrollUsers);
        connect.add(scrollGroups);
  
        groups.add(newgroup);
        input.add(sendbtn);
        input.add(userinput);
        input.add(nickLabel);
        screen.add(chatTxtContainer);
        chat.add("Center",screen);
        chat.add("South",input); 
        
        options.add(call);
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
        newgroup.addActionListener(this); 
        addmember.addActionListener(this);
        delmember.addActionListener(this);
        call.addActionListener(this);
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
        infoPopup = new Information(); //POP-UP
        new RecieveMsg(); //Start listening for server response
        new RecieveFrame(); //Start listening for P2P communications, not used until onCall.
        new ImageSender(); //Sends image to other users, not used until onCall.
        getServerIP();
    }  
           
    private void getServerIP() {
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
            if(!serverIP.equals("")){((Timer)e.getSource()).stop();}
            userInfo.append("   Maybe your Internet is down?\n");
            userInfo.append("   Or our server is fucked...\n");
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
            else{askForUsersOnline(nick);}
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
                        else if (p.getStatus().equals("managegroup")){serverMembersResponse(p);}
                        else if (p.getStatus().equals("groupusers")){informChatUsers(p.getInfo(), p.getNick(), "");}                        
                        else if (p.getStatus().equals("changeusers")){refreshGroups(p);}                        
                //=====================================================================================    
                
                //=====================================================================================
                //                 CLIENT-TO-CLIENT COMMUNICACTION MANAGING
                //=====================================================================================
                        else if (p.getStatus().equals("calluser")){prepareIpsForCall(p);} 
                
                //===================================================================================== 
                    } catch(Exception e){
                        System.out.println(e);
                    }                    
                }
            } catch (Exception e){
                System.out.println(e);
            }
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
        try { Thread.sleep(3000);} catch (InterruptedException ex) {System.out.println(ex);}
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
            nick = p.getNick();
            askForUsersOnline(p.getNick());
            for(String chatid : p.getObj().keySet()){
                //chatid, chatname, msgs, last_ip
                informChatUsers(chatid, p.getObj().get(chatid)[0], p.getObj().get(chatid)[1]);
            }          
        } else if(p.getMsg().equals("X")){userInfo.setText("\n\tWrong credentials");}        
          else {userInfo.setText("\n\tServer error, please restart");}  
        
        try { Thread.sleep(2000);} catch (InterruptedException ex) {System.out.println(ex);}
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
            nick = p.getNick();
            askForUsersOnline(p.getNick());             
            for(String chatid : p.getObj().keySet()){
                //chatid, chatname, msgs
                informChatUsers(chatid, p.getObj().get(chatid)[0], p.getObj().get(chatid)[1]);
            }            
        } else{userInfo.setText(p.getMsg()+p.getNick());}         
        
        try { Thread.sleep(5000);} catch (InterruptedException ex) {System.out.println(ex);}
        //Close Login Info POP-UP
        infoPopup.dispatchEvent(new WindowEvent(infoPopup, WindowEvent.WINDOW_CLOSING));
        //Close Login/Register window
        if(p.getMsg().equals(""))
        sessionFrame.dispatchEvent(new WindowEvent(sessionFrame, WindowEvent.WINDOW_CLOSING));
    }
    
//===========================================================================================
//                     USER-TO-USER LINK
//===========================================================================================
    private void askForUsersOnline(String nick){
       Send.message((String) GetIP.getLocalIp().get(1), "", nick, "getusers","");
    }
    
    private void setUsersOnline(Package p){ 
        if(p.getIp().equals((String) GetIP.getLocalIp().get(1))){
            this.nick = p.getNick();   
            nickLabel.setText("username: "+nick);
            if(!nick.contains("~guest")){
                options.remove(login); options.remove(register);
            }
        }
        
        for(String ip : p.getObj().keySet()){
            String user = p.getObj().get(ip)[0];
            boolean own = (GetIP.getLocalIp().contains(ip) || GetIP.getPublicIP().contains(ip));
         
            if(!own && !nick.equals(user)){ 
            //create chatid and store possible previous chat info
              String chatid =              
              nick.compareTo(user)>0 ? nick+"~"+user:user+"~"+nick;
              if(chatstorage.containsKey(chatid)){
                  //get the btn, change the listener
                  for(int i = 0; i < users.getComponentCount(); i++){
                      JToggleButton btn = (JToggleButton) users.getComponents()[i];
                      if(btn.getName().equals(chatid)){
                          final String ihatejava = chatid;
                          btn.addActionListener((ActionEvent e) -> {
                            chatxt.setText(chatstorage.get(ihatejava));
                            chatID = ihatejava;
                            adress = ip;
                            chat.setVisible(true); //how to avoid calling allways this
                          });
                      }
                  }
              } else {
                 chatstorage.put(chatid,p.getMsg()); //mesage in case there is a server error it get anounced in the chat
                 //add user btn
                 final String ihatejava = chatid;
                 JToggleButton btn = new JToggleButton(user+"                ", CHATLOGO);
                 btn.addActionListener((ActionEvent e) -> {
                     chatID = ihatejava;
                     removeGroupButtons();
                     adress = ip;                     
                     chat.setVisible(true); //how to avoid calling allways this
                 });
                 btn.setName(chatid);
                 bg.add(btn);
                 users.add(btn);         
                 users.repaint();
                 users.validate();
                }
            }
        }            
            
    }

    private void sendMessage(Package p){
        String id = p.getInfo();
        if(!p.getNick().equals(nick)){
            if(chatID.equals(id)){chatxt.append(p.getMsg()+"\n");}
            //chatid needed
            String msg = chatstorage.get(id)+p.getMsg()+"\n";
            chatstorage.replace(id, msg);
        }
        JToggleButton chat = null;
        if(p.getInfo().contains(groupSlug)){
        for(Component btn : groups.getComponents()){
            try{
                if(((JToggleButton)btn).getName().equals(p.getInfo())){
                    chat = (JToggleButton)btn;
                    groups.remove(chat);
                    break;
                }
            } catch (Exception ex){System.out.println("fuck");continue;}
        } groups.add(chat, 1);
        }else{
        for(Component btn : users.getComponents()){
            try{
                if(((JToggleButton)btn).getName().equals(p.getInfo())){
                    chat = (JToggleButton)btn;
                    users.remove(chat);
                    break;
                }
            } catch (Exception ex){System.out.println(ex);continue;}
        }users.add(chat, 0);
        }
        connect.repaint();
        
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
        if(event.getSource() == sendbtn){
            try { 
                String msg = timeFormat.format(new Date())+"["+nick+"]:\n"+userinput.getText() + "\n";
                Send.message( adress, msg, nick, "messaging", chatID);
                 
                chatxt.append(msg);
                String txt = chatxt.getText() + "\n";                   
                chatstorage.put(chatID, txt); //overwrite previous
                userinput.setText("");
                        
            } catch (Exception e){System.out.println(e);}
        }else if(event.getSource() == erasebtn){chatxt.setText("");}
        else if(event.getSource() == exitbtn){System.exit(0);}
        else if(event.getSource() == login){sessionFrame = new Login();}
        else if(event.getSource() == register){sessionFrame = new Register();}
        else if(event.getSource() == newgroup){createNewGroup();}
        else if(event.getSource() == addmember){addGroupMember();}
        else if(event.getSource() == delmember){delGroupMember();}
        else if(event.getSource() == call){makeCamCall();}
        else { JOptionPane.showMessageDialog(this, "Select a chat");}
    }
    
    public void sendToChat(KeyEvent pressed) {
        String msg = timeFormat.format(new Date())+"["+nick+"]:\n"+userinput.getText() + "\n";            
        try {
            if(pressed.getKeyCode() == KeyEvent.VK_ENTER && !userinput.getText().equals("")){
                Send.message( adress, msg, nick, "messaging", chatID);
                chatxt.append(msg);
                String txt = chatxt.getText() + "\n";
                  
                chatstorage.put(chatID, txt);
                userinput.setText("");
            }  
        } catch (Exception e){System.out.println(e);}            
    }
    
// ===========================================================================
//                      CAM
// ===========================================================================
    public synchronized void makeCamCall(){
        if(call.getText().equals("Call")){
            
            if (!chatID.isEmpty()){
                Send.message( clientIp, "", nick, "calluser", chatID);
            }

        } else {
            call.setText("Call");
            screen.remove(0);
            screen.repaint();
            onCall.set(false);
        }
    }
    
    public void prepareIpsForCall(Package p){
        
        if( !p.getMsg().isEmpty() && p.getMsg().contains(",") && !onCall.get()){
            callParticipants = new ArrayList<>(Arrays.asList(p.getMsg().split(",")));
            call.setText("HangUp");
            camFrame = new JPanel();
            onCall.set(true);
            for(String nombre : callParticipants){
                JLabel aux = new JLabel();
                aux.setName(nombre);
                aux.setText(nombre);
                camFrame.add(aux);
                userCam.put(nombre, aux);
            }
            screen.add(camFrame,0);
            screen.repaint();
        }
    }
    
    public void showFrame( VideoPackage p){
        userCam.get(p.getIp()).setIcon(p.getFrame());
    }
    
    class ImageSender implements Runnable{
       
        ImageSender(){
            Thread sender = new Thread(this);
            sender.start();
        }
        
        @Override
        public void run() {
            try {
                while(true){
                    
                    Thread.sleep(500);
                    if(onCall.get()){
                        if ( !callParticipants.isEmpty() && socketCallList.isEmpty()){
                            for(String ip : callParticipants){
                                socketCallList.add(new P2P( clientIp,  ip, videoCallPort));
                            }
                        }
                        // TODO if no cam, send resource/icon.   
                        Webcam cam= Webcam.getDefault();
                        cam.open();
                        BufferedImage br;
                        br= cam.getImage();
                        ImageIcon ic;
                        ic= new ImageIcon(br);
                        for(P2P conection : socketCallList){
                            System.out.println("SE HA ENVIADO IMAGEN A : "+ conection.getReceiver());
                            conection.message(ic);
                        }
                    }
                }
            } catch (WebcamException | InterruptedException e){
                chatxt.append("Error al enviar imagen\n"+e.getMessage());
            }
        }                     
    }
    
    class RecieveFrame implements Runnable{
       
        RecieveFrame(){
            Thread lintening = new Thread(this);
            lintening.start();
        }
        
        @Override
        public void run() {
            try { 
                ServerSocket port = new ServerSocket(videoCallPort);
                VideoPackage p;
                
                while(true){
                    try (Socket mysocket = port.accept()) {
                        System.out.println("HEMOS RECIBIDO PACKETE");
                        chatxt.append("HEMOS RECIBIDO PACKETE");
                        ObjectInputStream entrada = new ObjectInputStream(mysocket.getInputStream());
                        p = (VideoPackage) entrada.readObject();
                        System.out.println("SE HA RECIBIDO IMAGEN DE LA IP "+ p.getIp());
                        chatxt.append("SE HA RECIBIDO IMAGEN DE LA IP "+ p.getIp());
                //=====================================================================================
                //                 CLIENT-TO-CLIENT VIDEO SHARE MANAGING
                //=====================================================================================                        
                        if(p.getStatus().equals("videotransfer")){showFrame(p);}                    

                //===================================================================================== 
                    } catch(Exception e){}                    
                }
            } catch (Exception e){}
        }                     
    }
// ===========================================================================
//                      GROUP CREATION
// ===========================================================================
    private void createNewGroup() {
      if(nick.startsWith("~guest")){return;}

      //CALL DB FOR USERS
      groupname = JOptionPane.showInputDialog("Set group name");
      manageMembersGroup(0);
    }

    private void addGroupMember(){
        manageMembersGroup(1);
        op = 0;
    }

    private void delGroupMember(){ 
        manageMembersGroup(2);
        op = 1;
    }
    
    private void manageMembersGroup(int action){  
        String retrieve = "";
        if(action == 0){retrieve = "all";}
        else if(action == 1){retrieve = "add";}
        else if(action == 2){retrieve = "del";}
      //retrieves all users 
      Send.message((String) GetIP.getLocalIp().get(1), retrieve, "", "managegroup", chatID);
    }
    
    private void serverMembersResponse(Package p){
        if(p.getMsg().equals("")){return;}
      //ADDING USERS OPTIONS
      String[] users = p.getMsg().split("~");
      JPanel selectuser = new JPanel();
      for(String user : users){
        //Avoid creator to be selected = mistake
          if(/*!user.equals(nick) && */!user.equals("")){
            JButton adduser = new JButton(user);
            adduser.addActionListener((ActionEvent e)->{
//                if(!nick.equals("~guest~")){groupUsers += nick;}
                groupUsers += "~"+e.getActionCommand();
                selectuser.remove(adduser);
                selectuser.setVisible(false);
                selectuser.setVisible(true);    
                System.out.println("All users: "+groupUsers);
            });
            selectuser.add(adduser);
          }
      }
      
      JScrollPane scroll = new JScrollPane(selectuser);
      scroll.setPreferredSize(new Dimension(80,60));
      
      int option = JOptionPane.showOptionDialog(this, scroll, "Select group users", 1, 1, CHATLOGO, new Object[]{"ok","cancel"},null); 
      if(groupUsers.length() > 2){groupUsers = groupUsers.substring(1);}
      
        //SEND SELECTION CONFIRMATION TO SERVER && CANCEL
      if(option == 0 && op==0){Send.message("", groupUsers, "add", "changeusers", p.getInfo());}
      else if(option == 0&& op==1){Send.message("", groupUsers, "del", "changeusers", p.getInfo());}
      else if(option == 0&& op==2){Send.message("", groupUsers, groupname, "groupusers", "");}
      groupUsers = "";
    }
        
    private void removeGroupButtons(){
        options.remove(addmember);
        options.remove(delmember);
        options.repaint();
    }

    private void addGroupButtons(){        
        options.add(addmember);
        options.add(delmember);
        options.repaint();
    }

// ===========================================================================
//                      REFRESH GROUPS CHAT
// ===========================================================================
    private void refreshGroups(Package p) {
        
          if(p.getNick().equals("add")){
              for(Component btn : groups.getComponents()){
              try{
                //add if !allbtns.getName().equals(id)
              if(!((JToggleButton)btn).getName().equals(p.getInfo())){
                    groups.add((JToggleButton)btn);
                    bg.add((JToggleButton)btn);
                }}catch (Exception e){continue;}
              }
          }
          else{                            
              for(Component btn : groups.getComponents()){
                  try{
              //remove if btn(name) == id
                if(((JToggleButton)btn).getName().equals(p.getInfo())){
                    groups.remove((JToggleButton)btn);
                }}catch (Exception e){continue;}
              }
          }
          //repaint() can't be used, generates "floating" buttons
          groups.setVisible(false);
          groups.setVisible(true);
    }
    
// ===========================================================================
//                      CHAT MEMORY
// ===========================================================================
    private void informChatUsers(String chatid, String groupname, String msg) {
      //CREATE SWING COMPONENT FOR USER-TO-USER
      System.out.println(chatid+" | "+groupname+" | "+msg);
      if(!chatid.contains(groupSlug)){
         String user = chatid.split("~")[0].equals(nick) ? 
                chatid.split("~")[1] : chatid.split("~")[0];
         
         chatstorage.put(chatid, msg);
         JToggleButton btn = new JToggleButton(user+"                   ", CHATLOGO);
         btn.addActionListener((ActionEvent e) -> {
            //unselectButtons(e);
            removeGroupButtons();
            chatxt.setText(chatstorage.get(chatid));
            chatID = chatid;
            chat.setVisible(true);
//            adress = ""; 
         });
         btn.setName(chatid);     
         bg.add(btn);
         users.add(btn);         
         users.repaint();
         users.validate();
         
      } else {   
          System.out.println("creating group");
     //CREATE SWING COMPONENT FOR GROUPS    
        groupname = groupname.length()<"group    ".length() ? groupname+"            " : groupname;

        JToggleButton gbtn = new JToggleButton(groupname, new ImageIcon("img/group.png"));
        gbtn.addActionListener((ActionEvent e) -> {
            //unselectButtons(e);
            addGroupButtons();
            chatxt.setText(chatstorage.get(chatid));
            chatID = chatid;
            chat.setVisible(true);
            adress = "";
          });
          gbtn.setName(chatid);
          bg.add(gbtn);
          groups.add(gbtn);         
          groups.repaint();
          groups.validate();
        }
    }

}
//(XX)REMOVE GROUP FROM DATABASE FAILS
//(XX)REFRESH GROUPS ON CREATION FAILS
//(X)2 CHATS AT THE SAME TIME BREAKS THINGS -> Use just one
//(X)TOGGLE TOGGLE-BUTTONS
//(X)CHATS REPEAT IF THERE IS POST-LOGIN
//(X)USER FROM MEMORY AND ONLINE PERSIST

/**[SERVIDOR]---------------------------------------------------------------
 ->Al detectar al usuario creo el chat si no existe previamente
 + (comprobar si el chat existe)
 * chatid.split("~")[0].equals(nick) || chatid.split("~")[1].equals(nick)
 *                                   &&
 * chatid.split("~")[0].equals(nick) || chatid.split("~")[1].equals(nick)
 + (crear el chat)
 * chatid=nick+"~"+nick;
 * preferencia por nick alfanuméricamente superior [char] (mayúsculas incluidas)
 * 
 * crear el código del chat a cada momento de conectarse alguien
 * pescar los chats en los que está cualquiera de los dos
 * comprobar si es el que tienen en común sí? -> asignar : crear
 * 
 * --------------------------------------------------------------------------
 * 
 ->Al clicar sobre el usuario busco el chat si existe y lo renderizo
 * btn.getName() en el mapa de <chatid,txt> -> setText();
 * 
 ->Al escribir el chat se guarda en un mapa con clave ip
 * mapa.put(chatid,txt+msg)
 * 
 ->Al enviar el servidor guarda en BD (chatid, nick, msg)
 * chatid ? update : insert;
 * 
 ->Al responder el servidor se busca el chat en el mapa y se añade el msg
 * txt = mapa.get(chatid) -> mapa.put(chatid,txt+msg);
 * 
 ->Al llegar un mensaje o escribir el botón cambia de índice a 0
 * btn:getComponents(); if(btn.getName()==ip)b = btn;
 * remove(b); add(b,0);
 *  
 ->Crear grupo -> btn
 * se genera una id compleja ~g~adsfasdfqm243rq2+wÇq23
 * pedirá un nombre para el grupo que se podrá cambiar con menú contextual(*)
 * pedirá añadir usuarios: (servidor) lista de usuarios registrados como btns
 * al clicar sobre un botón se añade el usuario y el botón desaparece 
 * cuando abres un grupo en los botones de opciones aparece añadir usuario
 * al clicar aparecen los usuarios del servidor no registrados
 * al añadir el servidor establece la relación usuario~chat
 * cunado abres un grupo en los botones de opciones aparece eliminar usuario
 * al clicar aparecen los usuarios del chat registrados 
 * al eliminar el servidor elimina la relación usuario~chat
 * 
 ->Los grupos a los que se pertenece aparecen al iniciar sesión
 * 
 -> GUEST CAN'T CREATE GROUPS AND CHATS WON'T SAVE TO DB
 */