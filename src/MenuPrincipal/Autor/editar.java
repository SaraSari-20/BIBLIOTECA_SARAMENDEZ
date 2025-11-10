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
public class editar extends JDialog implements ActionListener {
    private Container contenedor;
    private JTextField campoNombre;
    private JComboBox<String> comboNacionalidad;
    private JButton guardar, cancelar;
    private int idAutor;
    
    public editar(JDialog dialogo, int idAutor){
        super(dialogo,"Editar Autor",true);
        this.idAutor = idAutor;
        
        setSize(350,250);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        
        inicio();
        cargarDatos();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0; c.gridy = 0;
        contenedor.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        campoNombre = new JTextField(15);
        contenedor.add(campoNombre, c);
        
        c.gridx = 0; c.gridy = 1;
        contenedor.add(new JLabel("Nacionalidad:"), c);
        c.gridx = 1;
        comboNacionalidad = new JComboBox<>(new String[]{
            "Guatemalteco", "Mexicano", "Argentino", "Chileno", "Peruano", "Colombiano"
        });
        contenedor.add(comboNacionalidad, c);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        guardar = new JButton("Guardar Cambios");
        cancelar = new JButton("Cancelar");
        guardar.addActionListener(this);
        cancelar.addActionListener(this);
        panelBotones.add(guardar);
        panelBotones.add(cancelar);
        
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        contenedor.add(panelBotones, c);
    }
    
    private void cargarDatos(){
        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM autores WHERE id = ?")) {
            
            ps.setInt(1, idAutor);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                campoNombre.setText(rs.getString("nombre"));
                comboNacionalidad.setSelectedItem(rs.getString("nacionalidad"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == guardar){
            actualizarAutor();
        } else if(e.getSource() == cancelar){
            this.dispose();
        }
    }
    
    private void actualizarAutor(){
        String nombre = campoNombre.getText().trim();
        String nacionalidad = comboNacionalidad.getSelectedItem().toString();

        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement("UPDATE autores SET nombre = ?, nacionalidad = ? WHERE id = ?")) {
            
            ps.setString(1, nombre);
            ps.setString(2, nacionalidad);
            ps.setInt(3, idAutor);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Autor actualizado correctamente.");
            this.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el autor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
