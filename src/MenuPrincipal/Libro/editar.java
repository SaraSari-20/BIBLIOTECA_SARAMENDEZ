/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Libro;

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
    private JTextField campoTitulo, campoAnio;
    private JComboBox<String> comboAutor, comboCategoria, comboStock;
    private JButton guardar, cancelar;
    private int idLibro;
    
    public editar(JDialog dialogo, int idLibro){
        super(dialogo,"Editar Libro",true);
        this.idLibro = idLibro;
        
        setSize(400,350);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        
        inicio();
        cargarAutores();
        cargarCategorias();
        cargarDatos();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0; c.gridy = 0;
        contenedor.add(new JLabel("Título:"), c);
        c.gridx = 1;
        campoTitulo = new JTextField(20);
        contenedor.add(campoTitulo, c);
        
        c.gridx = 0; c.gridy = 1;
        contenedor.add(new JLabel("Autor:"), c);
        c.gridx = 1;
        comboAutor = new JComboBox<>();
        contenedor.add(comboAutor, c);
        
        c.gridx = 0; c.gridy = 2;
        contenedor.add(new JLabel("Categoría:"), c);
        c.gridx = 1;
        comboCategoria = new JComboBox<>();
        contenedor.add(comboCategoria, c);
        
        c.gridx = 0; c.gridy = 3;
        contenedor.add(new JLabel("Año:"), c);
        c.gridx = 1;
        campoAnio = new JTextField(4);
        contenedor.add(campoAnio, c);
        
        c.gridx = 0; c.gridy = 4;
        contenedor.add(new JLabel("Estado:"), c);
        c.gridx = 1;
        comboStock = new JComboBox<>(new String[]{"disponible", "prestado", "agotado"});
        contenedor.add(comboStock, c);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        guardar = new JButton("Guardar Cambios");
        cancelar = new JButton("Cancelar");
        guardar.addActionListener(this);
        cancelar.addActionListener(this);
        panelBotones.add(guardar);
        panelBotones.add(cancelar);
        
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        contenedor.add(panelBotones, c);
    }
    
    private void cargarAutores(){
        try (Connection cn = mysqlConexiones.getConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre FROM autores")) {
            comboAutor.removeAllItems();
            while(rs.next()){
                comboAutor.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error al cargar autores: " + e.getMessage());
        }
    }
    
    private void cargarCategorias(){
        try (Connection cn = mysqlConexiones.getConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre FROM categorias")) {
            comboCategoria.removeAllItems();
            while(rs.next()){
                comboCategoria.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }
    
    private void cargarDatos(){
        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM libros WHERE id = ?")) {
            
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                campoTitulo.setText(rs.getString("titulo"));
                campoAnio.setText(rs.getString("anio"));
                comboStock.setSelectedItem(rs.getString("stock"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == guardar){
            actualizarLibro();
        } else if(e.getSource() == cancelar){
            this.dispose();
        }
    }
    
    private void actualizarLibro(){
        String titulo = campoTitulo.getText().trim();
        String anio = campoAnio.getText().trim();
        String stock = comboStock.getSelectedItem().toString();
        
        if(titulo.isEmpty() || anio.isEmpty()){
            JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idAutor = Integer.parseInt(comboAutor.getSelectedItem().toString().split(" - ")[0]);
        int idCategoria = Integer.parseInt(comboCategoria.getSelectedItem().toString().split(" - ")[0]);

        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement(
                 "UPDATE libros SET titulo = ?, autor_id = ?, categoria_id = ?, anio = ?, stock = ? WHERE id = ?")) {
            
            ps.setString(1, titulo);
            ps.setInt(2, idAutor);
            ps.setInt(3, idCategoria);
            ps.setString(4, anio);
            ps.setString(5, stock);
            ps.setInt(6, idLibro);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
            this.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el libro: " + ex.getMessage());
        }
    }
}
