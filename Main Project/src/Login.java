
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bradley
 */
public class Login extends javax.swing.JFrame {
 DB.JavaConnection con = new DB.JavaConnection();
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
           
setLocationRelativeTo(null);
   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panLogin = new javax.swing.JPanel();
        lblSistema = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblNombreU = new javax.swing.JLabel();
        lblContraseñas = new javax.swing.JLabel();
        jtxtContra = new javax.swing.JPasswordField();
        btnEntrar = new javax.swing.JButton();
        lblSanJoaquin = new javax.swing.JLabel();
        lbl20 = new javax.swing.JLabel();
        jtxtNomU = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Bodega Municipalidad de Flores");

        panLogin.setBackground(new java.awt.Color(255, 255, 255));
        panLogin.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 102, 153)));
        panLogin.setForeground(new java.awt.Color(0, 102, 102));
        panLogin.setToolTipText("");

        lblSistema.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblSistema.setText("Sistema de Bodega  Municipalidad de Flores");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Escudo_muni.gif"))); // NOI18N

        lblNombreU.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombreU.setText("Nombre de usuario");

        lblContraseñas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblContraseñas.setText("Contraseña");

        btnEntrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEntrar.setText("Entrar");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });

        lblSanJoaquin.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        lblSanJoaquin.setText("San Joaquín de Flores, Heredia");

        lbl20.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        lbl20.setText("2016");

        jtxtNomU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNomUActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panLoginLayout = new javax.swing.GroupLayout(panLogin);
        panLogin.setLayout(panLoginLayout);
        panLoginLayout.setHorizontalGroup(
            panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoginLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreU)
                    .addComponent(lblContraseñas))
                .addGap(35, 35, 35)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtContra, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jtxtNomU))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(144, 144, 144))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                        .addComponent(lblSistema)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                        .addComponent(btnEntrar)
                        .addGap(202, 202, 202))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                        .addComponent(lblSanJoaquin)
                        .addGap(155, 155, 155))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panLoginLayout.createSequentialGroup()
                        .addComponent(lbl20)
                        .addGap(226, 226, 226))))
        );
        panLoginLayout.setVerticalGroup(
            panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSistema)
                .addGap(30, 30, 30)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreU)
                    .addComponent(jtxtNomU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContraseñas)
                    .addComponent(jtxtContra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnEntrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSanJoaquin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private boolean validarLogin() throws SQLException{
    //uncomplete
        try{
            String username = jtxtNomU.getText();
            System.out.println(username);
            String pass = jtxtContra.getText();
            System.out.println(pass);
            
            String sql = "SELECT nickname_login,contraseña FROM usuario ";
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
            ResultSet res = us.executeQuery();
            System.out.println(res.getString("nickname_login") + "\t" +res.getString("contraseña"));
            
          
       
     
            if ( res.getString("nickname_login").equals(username) && res.getString("contraseña").equals(pass) ) 
                { 
                    System.out.println("USUARIO O PASS CORRECTOS");
                    return true;
                } 
                else 
                { 
                    System.out.println("USUARIO Y PASS INCORRECTOS");
                    return false;
                   
                }
          
           
        }catch(SQLException E){
        System.out.println("NOT FOUND");
          return false;//cambiar para
    }
       
    }
    
    
    
    
    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
     try {
         if( validarLogin() == true){
             new Principal().setVisible(true);
             this.setVisible(false);
             
         }
     } catch (SQLException ex) {
         Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
         System.out.println("CANT LOGIN,WRONG USER OR PASS");
     }
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void jtxtNomUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNomUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNomUActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jtxtContra;
    private javax.swing.JTextField jtxtNomU;
    private javax.swing.JLabel lbl20;
    private javax.swing.JLabel lblContraseñas;
    private javax.swing.JLabel lblNombreU;
    private javax.swing.JLabel lblSanJoaquin;
    private javax.swing.JLabel lblSistema;
    private javax.swing.JPanel panLogin;
    // End of variables declaration//GEN-END:variables
}
