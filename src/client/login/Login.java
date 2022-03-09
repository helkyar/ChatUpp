
package client.login;

//La clase Color crea color utilizando los valores dados en RGBA (red,green,blue,alpha)
import java.awt.Color;
//Proporciona clases e interfaz para dibujar bordes especializados alrededor de un componente Swing
import javax.swing.border.Border;
//clase Factory que produce diferentes tipos de bordes (instancias border)
import javax.swing.BorderFactory;
/*Java AWT rastrea el evento del teclado a través de KeyListener. El KeyListener
obtendrá el KeyEvent que revela datos de la interacción del usuario con el teclado
*/
import java.awt.event.KeyEvent;
/*indica que una ventana ha cambiado de estado. Este evento de bajo nivel es generado
por un objeto Ventana cuando se abre, cierra, activa, desactiva, iconifica o deiconifica,
o cuando el foco se transfiere dentro o fuera de la Ventana*/
import java.awt.event.WindowEvent;
//Esta es una implementación de TableModel que usa un Vector de vectores para almacenar los objetos de valor de celda
import javax.swing.table.DefaultTableModel;
//Te conecta o importa con el paquete de client y la clase java chat
import client.Chat;
//Te conecta o importa con el paquete de client y la clase java Send
import client.Send;
////Te conecta o importa con el paquete de client.helpers y la clase GetIP
import client.helpers.GetIP;
//Una implementación de la interfaz Icon que pinta iconos a partir de imágenes.
import javax.swing.ImageIcon;
/*Una versión extendida de java.awt.Frame que agrega soporte para la arquitectura de componentes JFC/Swing.
Construye un nuevo marco que inicialmente es invisible.*/
import javax.swing.JFrame;
//facilita la aparición de un cuadro de diálogo estándar que solicita a los usuarios un valor o les informa sobre algo.
import javax.swing.JOptionPane;
//Te conecta o importa con el paquete de server y la clase java DBConnection
import server.DBConnection;

/**
 *
 * @author Míldred Ramírez, Cristian Echauri, Houssam Amrouch, Mateo Crespí, 
 * Javier Palacios 
 */
public class Login extends javax.swing.JFrame {
    DefaultTableModel model = null;

    int islogin; //creamos la variable islogin
    //Stringclass representa cadenas de caracteres.
    String loginuser; 
    String password;
    String registerlogin;
    /**
     * Creates new form Login
     */
    public Login() {
        super("Chatty"); //se utiliza para referirse al objeto de clase padre inmediato.
        setIconImage(Chat.LOGO); //Establece la imagen que se mostrará como el icono de esta ventana.
        
        initComponents();
        /*Establece la operación que ocurrirá por defecto cuando el usuario inicie
        un "cierre" en este marco. DISPOSE_ON_CLOSE (definido en WindowConstants): 
        oculta y elimina automáticamente el marco después de invocar cualquier objeto 
        WindowListener registrado.*/
       
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//        centramos el form
        setLocationRelativeTo(null);
//        creamos un borde amarillo para el titulo del panel
//        los cuatro números son los bordes de la casilla, cuanto mas altos, mayor grosor de la linea        
//         con el 0, en la parte superior del titulo del panel no ponemos borde
        Border jpanel_titulo_borde = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.YELLOW);
//        fijamos el borde al jpanel del título
       jPanel_titulo.setBorder(jpanel_titulo_borde);
//        Creamos un borde para las casillas de minimizar y cerrar
//  Creamos un bordenaranja para el panel global
        Border glob_borde_panel = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.orange);
        jPanel1.setBorder(glob_borde_panel);
        
        //        Creamos un borde para las casillas de minimizar y cerrar
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);       
        jLabel_minimizar.setBorder(jlabel_borde);
        jLabel_cerrar.setBorder(jlabel_borde);
        
        // create a border for the create acconut jlabel
        Border label_create_accont_border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray);
        jLabel_Create_account.setBorder(label_create_accont_border);
        
        // crear los bordes de las labels al removerlas
         Border label_icons_bordes = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(153,153,153));
        jLabel_user.setBorder(label_icons_bordes);
        jLabel_password.setBorder(label_icons_bordes);
        
        //crear borde para casilla usuario y contraseña
        Border casilla_bordes = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.white);
        txtUser.setBorder(casilla_bordes);
        txtPassword.setBorder(casilla_bordes);
        
        txtPassword.setTransferHandler(null);
        
        //Make it visible
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_minimizar = new javax.swing.JLabel();
        jLabel_cerrar = new javax.swing.JLabel();
        jPanel_titulo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel_user = new javax.swing.JLabel();
        jLabel_password = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        login = new javax.swing.JToggleButton();
        jLabel_Create_account = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 0));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel_minimizar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_minimizar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_minimizar.setText("-");
        jLabel_minimizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_minimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_minimizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel_minimizarMouseExited(evt);
            }
        });

        jLabel_cerrar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_cerrar.setText("x");
        jLabel_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_cerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_cerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel_cerrarMouseExited(evt);
            }
        });

        jPanel_titulo.setBackground(new java.awt.Color(0, 240, 0));
        jPanel_titulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel_tituloMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Login");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel_tituloLayout = new javax.swing.GroupLayout(jPanel_titulo);
        jPanel_titulo.setLayout(jPanel_tituloLayout);
        jPanel_tituloLayout.setHorizontalGroup(
            jPanel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_tituloLayout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(86, 86, 86))
        );
        jPanel_tituloLayout.setVerticalGroup(
            jPanel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_tituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(28, 28, 28))
        );

        jLabel_user.setIcon(new ImageIcon("img/password.png"));

        jLabel_password.setIcon(new ImageIcon("img/user.png"));

        txtUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtUser.setForeground(new java.awt.Color(153, 153, 153));
        txtUser.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUser.setText("Username");
        txtUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUserFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUserFocusLost(evt);
            }
        });
        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserKeyPressed(evt);
            }
        });

        txtPassword.setForeground(new java.awt.Color(153, 153, 153));
        txtPassword.setText("Username");
        txtPassword.setToolTipText("password");
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        login.setBackground(new java.awt.Color(0, 84, 140));
        login.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setText("login");
        login.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login.setMaximumSize(new java.awt.Dimension(81, 31));
        login.setMinimumSize(new java.awt.Dimension(81, 31));
        login.setPreferredSize(new java.awt.Dimension(81, 31));
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginMouseExited(evt);
            }
        });
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });

        jLabel_Create_account.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel_Create_account.setForeground(new java.awt.Color(255, 51, 51));
        jLabel_Create_account.setText(">> ¿No tienes cuenta?  Crea una aquí!");
        jLabel_Create_account.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_Create_account.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_Create_accountMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel_Create_accountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel_Create_accountMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel_user, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUser))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel_password, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(login, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_Create_account, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_user, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_password, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel_Create_account, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDesktopPane1.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(jPanel_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jLabel_minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_minimizar)
                        .addComponent(jLabel_cerrar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDesktopPane1))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_minimizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizarMouseEntered
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
        jLabel_minimizar.setBorder(jlabel_borde);
        jLabel_minimizar.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_minimizarMouseEntered

    private void jLabel_minimizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizarMouseExited
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
        jLabel_minimizar.setBorder(jlabel_borde);
        jLabel_minimizar.setForeground(Color.black);
    }//GEN-LAST:event_jLabel_minimizarMouseExited

    private void jLabel_cerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseExited
        // TODO add your handling code here:
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
        jLabel_cerrar.setBorder(jlabel_borde);
        jLabel_cerrar.setForeground(Color.black);
    }//GEN-LAST:event_jLabel_cerrarMouseExited

    private void jLabel_cerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseEntered
         Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
        jLabel_cerrar.setBorder(jlabel_borde);
        jLabel_cerrar.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_cerrarMouseEntered

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtUserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUserFocusGained
        if(txtUser.getText().trim().toLowerCase().equals("username")){
            txtUser.setText("");
            txtUser.setForeground(Color.black);
            //poner un borde amarillo al jlabel 
            Border jlabel_icon = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW);
            jLabel_user.setBorder(jlabel_icon);
        }
        
    }//GEN-LAST:event_txtUserFocusGained

    private void txtUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUserFocusLost
        if(txtUser.getText().equals("")){
            txtUser.setText("\tUsername");
            txtUser.setForeground(Color.gray);
            //poner un borde amarillo al jlabel 
            Border jlabel_icon = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW);
            jLabel_user.setBorder(jlabel_icon);
        }
    }//GEN-LAST:event_txtUserFocusLost

    private void jLabel_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseClicked
       dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jLabel_cerrarMouseClicked

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){verifyLogin();}
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){verifyLogin();}
    }//GEN-LAST:event_txtUserKeyPressed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        verifyLogin();     
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jPanel_tituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_tituloMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel_tituloMouseClicked

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
        verifyLogin();
    }//GEN-LAST:event_loginActionPerformed

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        //if(txtPassword.getPassword().equals("Username")){
            txtPassword.setText("");
            txtPassword.setForeground(Color.black);
            //poner un borde amarillo al jlabel 
            Border jlabel_icon = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW);
            jLabel_user.setBorder(jlabel_icon);
        //}
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        if(txtPassword.getPassword().length < 1)
        {
            txtPassword.setText("Username");
            txtPassword.setForeground(Color.gray);
            //poner un borde amarillo al jlabel 
            Border jlabel_icon = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW);
            jLabel_user.setBorder(jlabel_icon);
        }
    }//GEN-LAST:event_txtPasswordFocusLost

    private void jLabel_Create_accountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_Create_accountMouseClicked
        Register rf = new Register();
        rf.setVisible(true);
        rf.pack();
        rf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_jLabel_Create_accountMouseClicked

    private void jLabel_Create_accountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_Create_accountMouseEntered
        Border label_border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red);
        jLabel_Create_account.setBorder(label_border);
    }//GEN-LAST:event_jLabel_Create_accountMouseEntered

    private void jLabel_Create_accountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_Create_accountMouseExited
        Border label_create_accont_border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray);
        jLabel_Create_account.setBorder(label_create_accont_border);
    }//GEN-LAST:event_jLabel_Create_accountMouseExited

    private void loginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginMouseEntered
         //fijar el fondo del jbuton
        login.setBackground(new Color(0,101,183));
    }//GEN-LAST:event_loginMouseEntered

    private void loginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginMouseExited
        //fijar el fondo del jbuton
        login.setBackground(new Color(0,84,104));
    }//GEN-LAST:event_loginMouseExited

    public void verifyLogin(){
        loginuser = txtUser.getText();
        password = String.valueOf(txtPassword.getPassword());
        
        if(loginuser.trim().length() > 1 && password.trim().length() > 1){
            //Login message sets ip adress as its own to recive the server response
            Send.message((String) GetIP.getLocalIp().get(1), password, loginuser, "login","");
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, complete los campos de usuario y contraseña.");
        }
    }
    
    public void setLogin(String registerlogin){
        if(!registerlogin.isEmpty()){txtUser.setText(registerlogin);}
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel_Create_account;
    private javax.swing.JLabel jLabel_cerrar;
    private javax.swing.JLabel jLabel_minimizar;
    private javax.swing.JLabel jLabel_password;
    private javax.swing.JLabel jLabel_user;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel_titulo;
    private javax.swing.JToggleButton login;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

}