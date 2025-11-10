/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import Recursos.*;
import Login.Login;

//import Recursos.mysqlConexiones;

/**
 *
 * @author practicante
 */
public class MenuPrincipal extends JFrame implements ActionListener{
    private Container contenedor;
    private JButton acercaDe,autores, categorias, libros, cerrarSesion;
    private int idUsuario;
    private String usuario;
    
    public MenuPrincipal(String usuario, int idUsuario){
        this.usuario = usuario;
        this.idUsuario = idUsuario;
        this.setTitle("Menu Principal");
        this.setSize(400,300);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        Recursos.cargarIcono(this, 64, 64);
        mysqlConexiones.getConexion();
        inicio();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,5,5,5);
        
        c.gridwidth = 3;
        c.gridy = 0; c.gridx = 0;
        JLabel titulo = new JLabel("Bienvenido " + usuario);
        titulo.setFont(new Font("Arial",15,20));
        contenedor.add(titulo,c);
        
        c.gridy = 1;
        acercaDe = new JButton("Acerca de...");
        acercaDe.addActionListener(this);
        contenedor.add(acercaDe,c);
        
        c.gridy = 2; c.gridwidth = 1;
        autores = new JButton("Autores");
        autores.addActionListener(this);
        contenedor.add(autores,c);
        
        c.gridx = 1;
        categorias = new JButton("Categorias"); 
        categorias.addActionListener(this);
        contenedor.add(categorias,c);
        
        c.gridx = 2;
        libros = new JButton("Libros");
        libros.addActionListener(this);
        contenedor.add(libros,c);
        
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 3;
        cerrarSesion = new JButton("Cerrar Sesión");
        cerrarSesion.addActionListener(this);
        cerrarSesion.setBackground(new Color(220, 70, 70));
        cerrarSesion.setForeground(Color.WHITE);
        contenedor.add(cerrarSesion, c);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == acercaDe){
            AcercaDe ad = new AcercaDe(this,idUsuario);
            ad.setVisible(true);
        }else if(e.getSource() == autores){
            Autores au = new Autores(this);
            au.setVisible(true);
        }else if(e.getSource() == categorias){
            Categorias ca = new Categorias(this);
            ca.setVisible(true);
        }else if(e.getSource() == libros){
            Libros li = new Libros(this);
            li.setVisible(true);
        }else if(e.getSource() == cerrarSesion){
            cerrarSesion();
        }
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            this.dispose(); // Cierra la ventana actual
            SwingUtilities.invokeLater(() -> {
                Login login = new Login();
                login.setVisible(true); // Muestra la ventana de inicio
            });
        }
    }
}
