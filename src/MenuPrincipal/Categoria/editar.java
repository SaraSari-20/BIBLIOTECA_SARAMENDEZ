/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Categoria;

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
    private JButton guardar, cancelar;
    private int idCategoria;
    
    public editar(JDialog dialogo, int idCategoria){
        super(dialogo,"Editar Categoría",true);
        this.idCategoria = idCategoria;
        setSize(300,200);
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
        
        c.gridx=0; c.gridy=0;
        contenedor.add(new JLabel("Nombre:"),c);
        c.gridx=1;
        campoNombre = new JTextField(15);
        contenedor.add(campoNombre,c);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        guardar = new JButton("Guardar");
        cancelar = new JButton("Cancelar");
        guardar.addActionListener(this);
        cancelar.addActionListener(this);
        panelBotones.add(guardar);
        panelBotones.add(cancelar);
        
        c.gridx=0; c.gridy=1; c.gridwidth=2;
        contenedor.add(panelBotones,c);
    }
    
    private void cargarDatos(){
        try(Connection cn = mysqlConexiones.getConexion();
            PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM categorias WHERE id=?")){
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                campoNombre.setText(rs.getString("nombre"));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==guardar){
            actualizarCategoria();
        }else if(e.getSource()==cancelar){
            this.dispose();
        }
    }
    
    private void actualizarCategoria(){
        String nombre = campoNombre.getText().trim();
        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(this,"El nombre no puede estar vacío.");
            return;
        }
        try(Connection cn = mysqlConexiones.getConexion();
            PreparedStatement ps = cn.prepareStatement("UPDATE categorias SET nombre=? WHERE id=?")){
            ps.setString(1,nombre);
            ps.setInt(2,idCategoria);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Categoría actualizada correctamente.");
            this.dispose();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
}
