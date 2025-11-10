/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

import MenuPrincipal.MenuPrincipal;
//import Recursos.mysqlConexiones;

/**
 *
 * @author practicante
 */
public class Acceder extends JDialog implements ActionListener{
    private Container contenedor;
    private JTextField campoUsuario, campoCarne;
    private JPasswordField campoContraseña;
    private JButton cancelar, acceder;
    
    private JFrame login;
    
    public Acceder(JFrame login){
        super(login,"Acceder",true);
        this.login = login;
        
        this.setSize(400,250);
        this.setLocationRelativeTo(login);
        this.setResizable(false);
        
        Recursos.mysqlConexiones.getConexion();
        inicio();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.CENTER;
        
        c.gridy = 0; c.gridx = 0;
        JLabel usuario = new JLabel("Usuario");
        contenedor.add(usuario,c);
        c.gridx = 1;
        campoUsuario = new JTextField(15);
        contenedor.add(campoUsuario,c);
        
        c.gridy = 1; c.gridx = 0;
        JLabel carne = new JLabel("Numero de Carne");
        contenedor.add(carne,c);
        c.gridx = 1;
        campoCarne = new JTextField(15);
        contenedor.add(campoCarne,c);
        
        c.gridy = 2; c.gridx = 0;
        JLabel contraseña = new JLabel("Contraseña");
        contenedor.add(contraseña,c);
        c.gridx = 1;
        campoContraseña = new JPasswordField(15);
        contenedor.add(campoContraseña,c);
        
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        cancelar = new JButton("Cancelar");
        acceder = new JButton("Acceder");
        cancelar.addActionListener(this);
        acceder.addActionListener(this);
        JPanel panelBotones = new JPanel();
        panelBotones.add(cancelar);
        panelBotones.add(acceder);
        contenedor.add(panelBotones,c);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == cancelar){
            this.dispose();
        }else if(e.getSource() == acceder){
            validarUsuario();
            this.dispose();
        }
    }
    
    private void validarUsuario() {
        String usuarioIngresado = campoUsuario.getText();
        String contraseñaIngresada = new String(campoContraseña.getPassword());

        if(usuarioIngresado.isEmpty() || contraseñaIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa usuario y contraseña.");
            return;
        }

        try (Connection con = Recursos.mysqlConexiones.getConexion()) {
            if(con == null) return;

            // Traemos el hash y el id del usuario
            String sql = "SELECT id, password FROM usuarios WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuarioIngresado);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int idUsuario = rs.getInt("id");
                String hashGuardado = rs.getString("password");

                // Verificamos el hash
                if(BCrypt.checkpw(contraseñaIngresada, hashGuardado)) {
                    JOptionPane.showMessageDialog(this, "Bienvenido " + usuarioIngresado);

                    // Abrimos menú principal y pasamos usuario y id
                    MenuPrincipal mp = new MenuPrincipal(usuarioIngresado, idUsuario);
                    mp.setVisible(true);
                    
                    if(login != null && login.isDisplayable()){
                        login.dispose();
                        this.dispose();
                    }else{
                        this.dispose();   
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }

        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al validar usuario: " + ex.getMessage());
        }
    }

}
