import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Window extends JFrame implements ActionListener, KeyListener{
        
	Container content = getContentPane();
    Container sideBar = getContentPane();
        
	JPanel users = new JPanel(new GridLayout(4,1));

	JPanel win = new JPanel();
    JPanel input = new JPanel();
    JPanel text = new JPanel();
    JFrame frame = new JFrame();
         
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	ImageIcon send = new ImageIcon("send.png");
    ImageIcon erase = new ImageIcon("erase.png");
    ImageIcon exit = new ImageIcon("exit.png");
    Image icon = new ImageIcon(getClass().getResource("icon.png")).getImage();
        
        
	JButton btn1 = new JButton("Send", send);
	JButton btn2 = new JButton("...", exit);
	JButton btn3 = new JButton("Clean", erase);
        
    JTextField field = new JTextField(38);
    JTextArea txtArea = new JTextArea(40,115);

	public Window() {
        super("Chatty");
        try {
            setSize(screenSize.width, screenSize.height-60);
            setDefaultCloseOperation(EXIT_ON_CLOSE); 
            // add(users);
            frame.pack();
            setVisible(true);
            setResizable(true);
            setLocationRelativeTo(null);
            setIconImage(icon);
            txtArea.setEditable(false);
                    
            input.add(btn2);
            input.add(btn3);
            input.add(btn1);
            input.add(field);
                    
            text.add(txtArea);
                    
            users.add(new JButton("A"));
            users.add(new JButton("B"));
            users.add(new JButton("C"));
            users.add(new JButton("D"));
                    
            content.add("Center", text);
            content.add("South", input);
            content.add("East", users);
                    
            btn1.addActionListener(this);
            btn2.addActionListener(this);
            btn3.addActionListener(this);

            btn2.setEnabled(false);
                    
            field.addKeyListener(this);
            
        } catch (Exception e) {System.out.println(e);}
		
	}
        
	public static void main(String[] args){
		new Window();
	}
        
    public void actionPerformed(ActionEvent event) {
        try {                
            if(event.getSource() == btn1){
                txtArea.append(field.getText() + "\n");
                field.setText("");
            } else if (event.getSource() == btn3){
                txtArea.setText("");
                btn2.setEnabled(true);
                btn2.setText("Exit");
            }else if (event.getSource() == btn2){
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
    public void keyTyped (KeyEvent typing){
    }
    public void keyReleased(KeyEvent released){
    }
}