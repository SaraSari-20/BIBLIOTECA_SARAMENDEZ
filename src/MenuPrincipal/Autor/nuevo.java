/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Autor;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import Recursos.mysqlConexiones;
/**
 *
 * @author practicante
 */
public class nuevo extends JDialog implements ActionListener {
    private Container contenedor;
    private JTextField campoNombre;
    private JComboBox<String> comboNacionalidad;
    private JButton guardar, cancelar;
    private JDialog dialogo;
    
    public nuevo(JDialog dialogo){
        super(dialogo,"Nuevo Autor",true);
        this.dialogo = dialogo;
        
        setSize(350,250);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        
        inicio();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre
        c.gridx = 0; c.gridy = 0;
        contenedor.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        campoNombre = new JTextField(15);
        contenedor.add(campoNombre, c);
        
        // Nacionalidad (ComboBox)
        c.gridx = 0; c.gridy = 1;
        contenedor.add(new JLabel("Nacionalidad:"), c);
        c.gridx = 1;
        comboNacionalidad = new JComboBox<>(new String[]{
            "Guatemalteco", "Mexicano", "Argentino", "Chileno", "Peruano", "Colombiano"
        });
        contenedor.add(comboNacionalidad, c);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        guardar = new JButton("Guardar");
        cancelar = new JButton("Cancelar");
        guardar.addActionListener(this);
        cancelar.addActionListener(this);
        panelBotones.add(guardar);
        panelBotones.add(cancelar);
        
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        contenedor.add(panelBotones, c);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == guardar){
            guardarAutor();
        } else if(e.getSource() == cancelar){
            this.dispose();
        }
    }
    
    private void guardarAutor(){
        String nombre = campoNombre.getText().trim();
        String nacionalidad = comboNacionalidad.getSelectedItem().toString();

        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO autores(nombre, nacionalidad) VALUES(?, ?)")) {
            
            ps.setString(1, nombre);
            ps.setString(2, nacionalidad);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Autor guardado correctamente.");
            this.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el autor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
