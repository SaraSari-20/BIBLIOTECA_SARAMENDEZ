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
public class nuevo extends JDialog implements ActionListener {
    private Container contenedor;
    private JTextField campoNombre;
    private JButton guardar, cancelar;
    
    public nuevo(JDialog dialogo){
        super(dialogo,"Nueva Categoría",true);
        setSize(300,200);
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
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==guardar){
            guardarCategoria();
        }else if(e.getSource()==cancelar){
            this.dispose();
        }
    }
    
    private void guardarCategoria(){
        String nombre = campoNombre.getText().trim();
        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }
        try(Connection cn = mysqlConexiones.getConexion();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO categorias(nombre) VALUES(?)")){
            ps.setString(1,nombre);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Categoría guardada correctamente.");
            this.dispose();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
}
