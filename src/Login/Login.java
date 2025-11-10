/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Recursos.Recursos; 
/**
 *
 * @author practicante
 */
public class Login extends JFrame implements ActionListener{
    private Container contenedor;
    private JButton registrar,acceder;
    
    public Login(){
        setTitle("Inicio");
        setSize(400,300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        Recursos.cargarIcono(this, 64, 64);
        inicio();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.CENTER;
        
        c.gridy = 0; c.gridx = 0; c.gridwidth = 2;
        JLabel titulo = new JLabel("Bienvenido a Biblioteca");
        contenedor.add(titulo,c);
        
        c.gridy = 1;
        JLabel logo = new JLabel();
        Recursos.cargarImagenLabel(logo, "/Imagenes/Mariano_logo.png", 100, 100);
        contenedor.add(logo,c);
        
        c.gridy = 2;
        JPanel panelBotones = new JPanel();
        acceder = new JButton("Acceder");
        registrar = new JButton("Registrarse");
        acceder.addActionListener(this);
        registrar.addActionListener(this);
        panelBotones.add(acceder);
        panelBotones.add(registrar);
        contenedor.add(panelBotones,c);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == acceder){
            Acceder a = new Acceder(this);
            a.setVisible(true);
        }
        
        if(e.getSource() == registrar){
            Registro r = new Registro(this);
            r.setVisible(true);
        }
    }
}
