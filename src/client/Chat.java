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
import java.awt.font.TextAttribute;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.AttributedString;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

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
        
    private final JButton sendbtn = new JButton(new ImageIcon("img/send.png"));
    private final JButton exitbtn = new JButton("X");
    private final JButton erasebtn = new JButton("#");
    private final JButton login = new JButton("Login");
    private final JButton register = new JButton("Register");
    private final JButton call = new JButton("Call");
    private final JButton newgroup = new JButton("         +          ");
    
    private final JTextField userinput = new JTextField(38);
    private final JTextArea chatxt = new JTextArea(20,50);
        
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
    
    //DATA MANAGEMENT VARIABLES_______________________________________________
    private Map<String, String> chatstorage = new HashMap<>();
    String[] optionUsersGroup;
    private String groupUsers = "";
    
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
        screen.add(chatxt);
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
                        else if (p.getStatus().equals("creategroup")){optionUsersGroup = p.getMsg().split("~");}
                        else if (p.getStatus().equals("groupusers")){informChatUsers(p.getInfo(), p.getNick(), "");}
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
            for(String chatid : p.getObj().keySet()){
                //chatid, chatname, msgs
                informChatUsers(chatid, p.getObj().get(chatid)[0], p.getObj().get(chatid)[1]);
            }          
        } else if(p.getMsg().equals("X")){userInfo.setText("\n\tWrong credentials");}        
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
            for(String chatid : p.getObj().keySet()){
                //chatid, chatname, msgs
                informChatUsers(chatid, p.getObj().get(chatid)[0], p.getObj().get(chatid)[1]);
            }            
        } else{userInfo.setText(p.getMsg()+p.getNick());}         
        
        try { Thread.sleep(5000);} catch (InterruptedException ex) {}
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
        //remove login and register btns if guest
        if(!nick.equals("~guest~")){
            options.remove(login);
            options.remove(register);            
        }
        Send.message((String) GetIP.getLocalIp().get(1), "", nick, "getusers","");
    }
    
    private void setUsersOnline(Package p){     
        this.nick = p.getNick();   
        nickLabel.setText("username: "+nick);
        
        for(String ip : p.getObj().keySet()){
            String user = p.getObj().get(ip)[0];
            boolean own = (GetIP.getLocalIp().contains(ip));
//          || GetIP.getPublicIP().contains(p.getObj().get(user))
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
                            System.out.println("userbutton updated from server online: "+ip+"//"+adress);
                            chatID = ihatejava;
                            adress = ip;
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
                     unselectButtons(e);
                     System.out.println("userbutton from server online: "+ip+"//"+adress);
                     adress = ip;
                     
                 });
                 btn.setName(chatid);

                 users.add(btn);         
                 users.setVisible(false);
                 users.setVisible(true); 
                }
            }
        }            
            
    }
           
    private void unselectButtons(ActionEvent e) {
        for(Component btn : connect.getComponents()){            
            try{((JToggleButton)btn).setSelected(true);
            } catch (Exception ex){continue;}
        }
        ((JToggleButton)e.getSource()).setSelected(true);
        chatxt.setText(chatstorage.get(chatID));
        chat.setVisible(true);
    }

    private void sendMessage(Package p){
        chatxt.append(p.getMsg()+"\n");
            //chatid needed
        chatstorage.put(chatID,chatxt.getText());
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
                String msg = timeFormat.format(new Date())+"["+nick+"]:\n"+userinput.getText() + "\n";
                System.out.println("sending msg: "+adress+", "+msg+", "+nick+", "+chatID);
                Send.message( adress, msg, nick, "messaging", chatID);
                 
                chatxt.append(msg);
                String txt = chatxt.getText() + "\n";                   
                chatstorage.put(chatID, txt); //overwrite previous
                userinput.setText("");
                        
            } catch (Exception e){e.printStackTrace();}
        }else if(event.getSource() == erasebtn){chatxt.setText("");}
        else if(event.getSource() == exitbtn){System.exit(0);}
        else if(event.getSource() == login){sessionFrame = new Login();}
        else if(event.getSource() == register){sessionFrame = new Register();}
        else if(event.getSource() == newgroup){createNewGroup();}
        else if(event.getSource() == call){makeCamCall();}
        else { JOptionPane.showMessageDialog(this, "Select a chat");}
    }
    
    public void sendToChat(KeyEvent pressed) {
        if(adress.length()>1){
            String msg = timeFormat.format(new Date())+"["+nick+"]:\n"+userinput.getText() + "\n";            
            try {
                if(pressed.getKeyCode() == KeyEvent.VK_ENTER && !userinput.getText().equals("")){
                    Send.message( adress, msg, nick, "messaging", chatID);
                    
                    chatxt.append(msg);
                    String txt = chatxt.getText() + "\n";
                    
                    chatstorage.put(chatID, txt);
                    userinput.setText("");
                }  
            } catch (Exception e){e.printStackTrace();}
        }else { JOptionPane.showMessageDialog(this, "Select a chat");}
    }
    
    public void makeCamCall(){
        if(call.getText().equals("Call")){
            call.setText("HangUp");
            JPanel cam = new JPanel();
            cam.add(new JLabel(new ImageIcon("img/activo.png")));
            screen.add(cam,0);
            screen.setVisible(false);
            screen.setVisible(true);
        } else {
            call.setText("Call");
            screen.remove(0);
            screen.setVisible(false);
            screen.setVisible(true);
        }
    }
    
    private void createNewGroup() {
      if(nick.equals("~guest~")){return;}

      //CALL DB FOR USERS
      Send.message((String) GetIP.getLocalIp().get(1), "", "", "creategroup", "");
      String groupname = JOptionPane.showInputDialog("Set group name");
      
      //ADDING USERS OPTIONS
      String[] users = optionUsersGroup;
      JPanel selectuser = new JPanel();
      for(String user : users){
          if(!user.equals(nick) && !user.equals("")){
            JButton adduser = new JButton(user);
            adduser.addActionListener((ActionEvent e)->{
                if(!nick.equals("~guest~")){groupUsers += nick;}
                groupUsers += "~"+e.getActionCommand();
                selectuser.remove(adduser);
                selectuser.setVisible(false); selectuser.setVisible(true);
            });
            selectuser.add(adduser);
          }
      }
      JScrollPane scroll = new JScrollPane(selectuser);
      scroll.setPreferredSize(new Dimension(80,60));
      
      int option =
      JOptionPane.showOptionDialog(this, scroll, "Select group users", 1, 1, CHATLOGO, new Object[]{"ok","cancel"},null);
      
      //SEND SELECTION CONFIRMATION TO SERVER && CANCEL
      if(option == 0){Send.message("", groupUsers, groupname, "groupusers", "");}
  
      //[SERVER]
      //create db group entry
      //update DB group-users
      //requests from status gruop get ips from db using groupname
      //msg is send like get users online
      //-------------------------------------------------------------------
      //Delete group & delete user (is the same)
      //Add new user
    }
    
    private void informChatUsers(String chatid, String groupname, String msg) {
      //CREATE SWING COMPONENT FOR USER-TO-USER
      if(!chatid.contains("~g~")){
         String user = chatid.split("~")[0].equals(nick) ? 
                 chatid.split("~")[1] : chatid.split("~")[0];
         
         chatstorage.put(chatid, msg);
         JToggleButton btn = new JToggleButton(user+"                   ", CHATLOGO);
         btn.addActionListener((ActionEvent e) -> {
            chatxt.setText(chatstorage.get(chatid));
            chatID = chatid;
            System.out.println("userbutton from server memory: "+adress);
            adress = "";
         });
         btn.setName(chatid);
         
         users.add(btn);         
         users.setVisible(false);
         users.setVisible(true); 
      } else {        
     //CREATE SWING COMPONENT FOR GROUPS    
        groupname = groupname.length()<"group    ".length() ? groupname+"            " : groupname;

        JToggleButton gbtn = new JToggleButton(groupname, new ImageIcon("img/group.png"));
        gbtn.addActionListener((ActionEvent e) -> {
          chatxt.setText(chatstorage.get(chatid));
          chatID = chatid;
          System.out.println("grupbutton from server memory: "+adress);
          adress = "";
        });
        gbtn.setName(chatid);

        groups.add(gbtn);         
        groups.setVisible(false);
        groups.setVisible(true);
      }
    }
}
//(X)TOGGLE TOGGLE-BUTTONS
//(X)2 CHATS AT THE SAME TIME BREAKS THINGS
//(X)(?)USER SWAP
//(X)NEW USER NOT ADDED (no actualiza)
//(>)Send msg if usser is not connected


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