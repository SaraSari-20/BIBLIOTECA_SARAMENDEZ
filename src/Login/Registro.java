/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;
import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

import Recursos.mysqlConexiones;

/**
 *
 * @author practicante
 */
public class Registro extends JDialog implements ActionListener{
    private Container contenedor;
    private JTextField campoUsuario, campoNombre, campoCarne, campoProyecto, campoVersion; //foto?
    private JPasswordField campoContraseña;
    private JComboBox<String> comboRol, comboEstado;
    
    private JLabel etiquetaFoto;
    private JButton foto, cancelar,registrar;
    private File archivoFoto;
    
    private JFrame login;
    
    public Registro(JFrame login){
        super(login,"Registro",true);
        this.login = login;
        this.setSize(600,550);
        this.setLocationRelativeTo(login);
        this.setResizable(false);
        
        mysqlConexiones.getConexion();
        inicio();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets (5,5,5,5);
        c.anchor = GridBagConstraints.WEST;
        
        c.gridy = 0; c.gridx = 0; c.gridwidth = 2;
        JLabel nombre = new JLabel("Nombre Completo");
        contenedor.add(nombre,c);
        c.gridy = 1;
        campoNombre = new JTextField(30 );
        contenedor.add(campoNombre,c);
        
        c.gridwidth = 1;
        c.gridy = 2; c.gridx = 0;
        JLabel usuario = new JLabel("Usuario");
        contenedor.add(usuario,c);
        c.gridx = 1;
        campoUsuario = new JTextField(15);
        contenedor.add(campoUsuario,c);
        
        c.gridy = 3; c.gridx = 0;
        JLabel contraseña = new JLabel("Contraseña");
        contenedor.add(contraseña,c);
        c.gridx = 1;
        campoContraseña = new JPasswordField(15);
        contenedor.add(campoContraseña,c);
        
        
        c.gridwidth = 2; c.gridx = 0; 
        JPanel panelCombos1 = new JPanel();
        JPanel panelCombos2 = new JPanel();
        comboEstado = new JComboBox<>();
        comboRol = new JComboBox<>();
        cargarValoresEnum("usuarios","rol",comboRol);
        cargarValoresEnum("usuarios","estado",comboEstado);
        panelCombos1.add(new JLabel("Rol: "));
        panelCombos1.add(comboRol);
        panelCombos2.add(new JLabel("Estado: "));
        panelCombos2.add(comboEstado);
        
        contenedor.add(panelCombos1,c);
        
        contenedor.add(panelCombos2,c);
        
        panelCombos1.add(comboRol);
        panelCombos2.add(comboEstado);
        c.gridy = 4;
        contenedor.add(panelCombos1,c);
        c.gridy = 5;
        contenedor.add(panelCombos2,c);
        
        
        c.gridy = 6; c.gridwidth = 1;
        JLabel carne = new JLabel("Numero de Carne");
        contenedor.add(carne,c);
        c.gridx = 1;
        campoCarne = new JTextField(15);
        contenedor.add(campoCarne,c);
        
        c.gridy = 7; c.gridx = 0;
        JLabel Lfoto = new JLabel("Fotografia estudiante");
        contenedor.add(Lfoto,c);
        c.gridy = 8; c.gridwidth = 2; //c.gridx = 1;
        JPanel panelFoto = new JPanel();
        etiquetaFoto = new JLabel("Sin imagen");
        foto = new JButton("Seleccionar...");
        foto.addActionListener(this);
        panelFoto.add(etiquetaFoto);
        panelFoto.add(foto);
        contenedor.add(panelFoto,c);
        //implementar para poder añadir una foto y que se almacene en mi base de datos
        
        
        c.gridy = 9; c.gridx = 0; c.gridwidth = 1;
        JLabel proyecto = new JLabel("Proyecto");
        contenedor.add(proyecto,c);
        c.gridx = 1;
        campoProyecto = new JTextField(15);
        contenedor.add(campoProyecto,c);
        
        c.gridy = 10; c.gridx = 0;
        JLabel version = new JLabel("Version");
        contenedor.add(version,c);
        c.gridx = 1;
        campoVersion = new JTextField(15);
        contenedor.add(campoVersion,c);
        
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 11; c.gridx = 0; c.gridwidth = 2;
        JPanel panelBotones = new JPanel();
        cancelar = new JButton("Cancelar");
        registrar = new JButton("Registrar");
        cancelar.addActionListener(this);
        registrar.addActionListener(this);
        panelBotones.add(cancelar);
        panelBotones.add(registrar);
        contenedor.add(panelBotones,c);
        c.gridwidth = 1;

    }
    
    private void cargarValoresEnum(String tabla, String columna, JComboBox<String> combo) {
        try (Connection con = mysqlConexiones.getConexion()) {
            if (con == null) return;
            String sql = "SHOW COLUMNS FROM " + tabla + " LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, columna);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("Type"); // Ejemplo: enum('Administrador','Desarrollador','Usuario')
                type = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
                String[] valores = type.replace("'", "").split(",");
                for (String valor : valores) combo.addItem(valor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando ENUM: " + e.getMessage());
        }
    }
    
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona una foto");
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoFoto = chooser.getSelectedFile();
            etiquetaFoto.setText(archivoFoto.getName());
        }
    }

    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == foto){
            seleccionarFoto();
        }
        
        if(e.getSource() == registrar){
            registrarUsuario();
            this.dispose();
        }
        
        if(e.getSource() == cancelar){
            this.dispose();
        }
    }
    
    private void registrarUsuario() {
        String nombre = campoNombre.getText();
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());
        String carne = campoCarne.getText();
        String rol = (String) comboRol.getSelectedItem();
        String estado = (String) comboEstado.getSelectedItem();
        String proyecto = campoProyecto.getText();
        String version = campoVersion.getText();
        String hash = BCrypt.hashpw(contraseña, BCrypt.gensalt());

        if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.");
            return;
        }

        try (Connection con = mysqlConexiones.getConexion()) {
            if (con == null) return;

            // Inserta en usuarios
            String sqlUser = "INSERT INTO usuarios (username, password, rol, estado) VALUES (?, ?, ?, ?)";
            PreparedStatement psUser = con.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, usuario);
            psUser.setString(2, hash);
            psUser.setString(3, rol);
            psUser.setString(4, estado);
            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                String sqlInfo = "INSERT INTO acerca_de (usuario_id, numero_carne, nombres, foto_path, proyecto, version) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psInfo = con.prepareStatement(sqlInfo);
                psInfo.setInt(1, userId);
                psInfo.setString(2, carne);
                psInfo.setString(3, nombre);
                psInfo.setString(4, archivoFoto != null ? archivoFoto.getAbsolutePath() : null);
                psInfo.setString(5, proyecto);
                psInfo.setString(6, version);
                psInfo.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }
}
