package client.login;

import client.Chat;
import client.Send;
import client.helpers.GetIP;
import java.awt.Color;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Academia
 */
public class Register extends javax.swing.JFrame {

    DefaultTableModel model = null;
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/ccw?autoReconnect=true&useSSL=false";
    String user = "root";
    String pass = "";
    Connection conn = null;
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;
    int islogin;

    //creamos una variable para fijar la ipagen al path
    String image_path = null;

    public Register() {
        super("Chatty");
        setIconImage(Chat.LOGO);
        
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //        centramos el form
        this.setLocationRelativeTo(null);
        this.setTitle("Registry");
        userPassword.setTransferHandler(null);
        userPassword2.setTransferHandler(null);
        Border jpanel_titulo_borde = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.YELLOW);
        //        fijamos el borde al jpanel del título
        jPanel_titulo.setBorder(jpanel_titulo_borde);
        //        Creamos un borde para las casillas de minimizar y cerrar
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
        jLabel_minimizar.setBorder(jlabel_borde);
        jLabel_cerrar.setBorder(jlabel_borde);

        //Creamos un borde para las casillas de texto y contraseña
        Border field_border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.WHITE);
        userUser.setBorder(field_border);
        userName.setBorder(field_border);
        userSurname.setBorder(field_border);
        userNick.setBorder(field_border);
        userPassword.setBorder(field_border);
        userPassword2.setBorder(field_border);

        //creamos un boton de grupo para la selección del género
        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton_Masculino);
        bg.add(jRadioButton_Femenino);
        bg.add(jRadioButton_Otros);
        
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
        jButton_Registro = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        userUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        userSurname = new javax.swing.JTextField();
        userNick = new javax.swing.JTextField();
        userName = new javax.swing.JTextField();
        jRadioButton_Masculino = new javax.swing.JRadioButton();
        jRadioButton_Femenino = new javax.swing.JRadioButton();
        jRadioButton_Otros = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jButton_SeleccionImagen = new javax.swing.JButton();
        jLabel_imgpath = new javax.swing.JLabel();
        userPassword = new javax.swing.JPasswordField();
        userPassword2 = new javax.swing.JPasswordField();
        frameLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 0));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel_minimizar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel_minimizar.setText("-");
        jLabel_minimizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_minimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_minimizarMouseClicked(evt);
            }
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

        jPanel_titulo.setBackground(new java.awt.Color(0, 84, 140));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Registro");

        javax.swing.GroupLayout jPanel_tituloLayout = new javax.swing.GroupLayout(jPanel_titulo);
        jPanel_titulo.setLayout(jPanel_tituloLayout);
        jPanel_tituloLayout.setHorizontalGroup(
            jPanel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_tituloLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel3)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel_tituloLayout.setVerticalGroup(
            jPanel_tituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_tituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jButton_Registro.setBackground(new java.awt.Color(235, 47, 6));
        jButton_Registro.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton_Registro.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Registro.setText("Registro");
        jButton_Registro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton_Registro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton_RegistroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton_RegistroMouseExited(evt);
            }
        });
        jButton_Registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RegistroActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Confirmar contraseña:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Usuario:");
        jLabel2.setMaximumSize(new java.awt.Dimension(205, 22));
        jLabel2.setMinimumSize(new java.awt.Dimension(205, 22));

        userUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Contraseña:");
        jLabel4.setMaximumSize(new java.awt.Dimension(205, 22));
        jLabel4.setMinimumSize(new java.awt.Dimension(205, 22));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Nombre real:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Nickname:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Apellidos:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Imágenes(WIP):");

        userSurname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        userNick.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        userName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jRadioButton_Masculino.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton_Masculino.setText("Masculino");

        jRadioButton_Femenino.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton_Femenino.setText("Femenino");

        jRadioButton_Otros.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton_Otros.setText("Otros");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Género:");

        jButton_SeleccionImagen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton_SeleccionImagen.setText("Seleccione Imagen");
        jButton_SeleccionImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SeleccionImagenActionPerformed(evt);
            }
        });

        jLabel_imgpath.setFont(new java.awt.Font("Sylfaen", 0, 11)); // NOI18N
        jLabel_imgpath.setText("Image Path");

        userPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        userPassword2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_Registro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(userName)
                                    .addComponent(userPassword2)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(userNick, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(userSurname)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jRadioButton_Masculino)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton_Femenino)
                                .addGap(43, 43, 43)
                                .addComponent(jRadioButton_Otros)
                                .addGap(0, 186, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_SeleccionImagen)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_imgpath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userUser)
                            .addComponent(userPassword))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton_Masculino)
                    .addComponent(jRadioButton_Femenino)
                    .addComponent(jRadioButton_Otros))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_SeleccionImagen)
                    .addComponent(jLabel_imgpath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addComponent(jButton_Registro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        frameLogin.setText("jButton1");
        frameLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(frameLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125)
                .addComponent(jLabel_minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frameLogin)
                            .addComponent(jPanel_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void jLabel_minimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_minimizarMouseClicked
        //para minimizar la ventana al hacer click
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel_minimizarMouseClicked

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

    private void jLabel_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseClicked
       dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jLabel_cerrarMouseClicked

    private void jLabel_cerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseEntered
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
        jLabel_cerrar.setBorder(jlabel_borde);
        jLabel_cerrar.setForeground(Color.white);
    }//GEN-LAST:event_jLabel_cerrarMouseEntered

    private void jLabel_cerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_cerrarMouseExited
        Border jlabel_borde = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
        jLabel_cerrar.setBorder(jlabel_borde);
        jLabel_cerrar.setForeground(Color.black);
    }//GEN-LAST:event_jLabel_cerrarMouseExited

    private void jButton_RegistroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_RegistroMouseEntered
        //fijar el fondo del jbuton
        jButton_Registro.setBackground(new Color(225, 20, 2));
    }//GEN-LAST:event_jButton_RegistroMouseEntered

    private void jButton_RegistroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_RegistroMouseExited
        //fijar el fondo del jbuton
        jButton_Registro.setBackground(new Color(235, 47, 6));
    }//GEN-LAST:event_jButton_RegistroMouseExited
                                                                                      
    private void frameLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameLoginActionPerformed
        Chat.sessionFrame = new Login();
        setVisible(false);
    }//GEN-LAST:event_frameLoginActionPerformed

    private void jButton_RegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RegistroActionPerformed
        String nick = userUser.getText();

        String pwd = String.valueOf(userPassword.getPassword());
        String pwd2 = String.valueOf(userPassword2.getPassword());
                   
        String name = userName.getText();
        String surname = userSurname.getText();
        String email = userNick.getText();
        
        String genre;
        if (jRadioButton_Masculino.isSelected()){genre = "Masculino";}
        else if (jRadioButton_Femenino.isSelected()){genre = "Femenino";}
        else {genre = "Otros";}       
        
        String data = nick+"~"+pwd+"~"+name+"~"+surname+"~"+email+"~"+genre;
        String registerFail = "";
        
        //Forbid "~" symbol
         //Check empty inputs
        if(nick.trim().equals("") || name.trim().equals("") || surname.trim().equals("") ||
                email.trim().equals("") || pwd.trim().equals("") || pwd2.trim().equals("")){
            registerFail += "\n\tLos campos no pueden estar vacíos";
            
            JOptionPane.showMessageDialog(new JFrame(), 
                    "Por favor, rellena todos los campos del formulario.",
                    "Formulario incompleto", 
                    JOptionPane.ERROR_MESSAGE);
        
        } else if (!pwd.equals(pwd2)) {
            registerFail += "\n\tLas contraseñas no coinciden";
            
            JOptionPane.showMessageDialog(new JFrame(), 
                    "Las contraseñas no coinciden.",
                    "Error en doble comprobación", 
                    JOptionPane.ERROR_MESSAGE);
        }
        
        else {
            Send.message((String) GetIP.getLocalIp().get(1), registerFail, data, "register", "");
        }
    }//GEN-LAST:event_jButton_RegistroActionPerformed

    private void jButton_SeleccionImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SeleccionImagenActionPerformed
        //Seleccionar una imagen y fijarla en la jlabel del image path
        String path = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        //extensión archivo
        FileNameExtensionFilter extension = new FileNameExtensionFilter("*Images", "jpg", "png", "jpeg");
        chooser.addChoosableFileFilter(extension);

        int filestate = chooser.showSaveDialog(null);
        //comprobar si el usuario selecciona una imagen
        if (filestate == JFileChooser.APPROVE_OPTION) {

            File selectedImage = chooser.getSelectedFile();
            path = selectedImage.getAbsolutePath();
            jLabel_imgpath.setText(path);

            image_path = path;
        }
    }//GEN-LAST:event_jButton_SeleccionImagenActionPerformed
    
    //crear una funcion para verificar las casillas vacias
//    public boolean verifyFields()
//    {
//        String usuario = userUser.getText();
//        String nombreReal = userName.getText();
//        String apellidos = userSurname.getText();
//        String nickname= userNick.getText();
//        String pass1 = String.valueOf(userPassword.getPassword());
//        String pass2 = String.valueOf(userPassword2.getPassword());
//        
//         // mirar casillas vacias
//        if(usuario.trim().equals("") || nombreReal.trim().equals("") || apellidos.trim().equals("") ||
//                nickname.trim().equals("") || pass1.trim().equals("") || pass2.trim().equals(""))
//        {
//            JOptionPane.showMessageDialog(null, "Una o más casillas están vacias","Casilla vacía",2);
//            return false;
//        }
//        
//        // comprobasr si las dos contraseñas son iguales o no
//        else if(!pass1.equals(pass2))
//        {
//           JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden","Confirmar la contraseña",2); 
//           return false;
//        }
//        
//        // Si todo está bien se continua
//        else{
//            return true;
//    }
//    }
    //crear una funcion para comprobar si el usuario creado ya existe en la base de datos
//      public boolean checkUsername(String username){
//        
//        PreparedStatement st;
//        ResultSet rs;
//        boolean username_exist = false;
//        
//        String query = "SELECT * FROM `users` WHERE `username` = ?";
//        
//        try {
//            
////            st = My_CNX.getConnection().prepareStatement(query);
//            st.setString(1, username);
//            rs = st.executeQuery();
//        
//            if(rs.next())
//            {
//                username_exist = true;
//                JOptionPane.showMessageDialog(null, "Este usuario ya existe", "Usuario incorrecto", 2);
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return username_exist;
//    }
//    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton frameLogin;
    private javax.swing.JButton jButton_Registro;
    private javax.swing.JButton jButton_SeleccionImagen;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_cerrar;
    private javax.swing.JLabel jLabel_imgpath;
    private javax.swing.JLabel jLabel_minimizar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel_titulo;
    private javax.swing.JRadioButton jRadioButton_Femenino;
    private javax.swing.JRadioButton jRadioButton_Masculino;
    private javax.swing.JRadioButton jRadioButton_Otros;
    private javax.swing.JTextField userName;
    private javax.swing.JTextField userNick;
    private javax.swing.JPasswordField userPassword;
    private javax.swing.JPasswordField userPassword2;
    private javax.swing.JTextField userSurname;
    private javax.swing.JTextField userUser;
    // End of variables declaration//GEN-END:variables
}
