/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Categoria;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import Recursos.mysqlConexiones;
/**
 *
 * @author practicante
 */
public class visualizar extends JDialog {
    private JLabel nombre;
    private int idCategoria;
    
    public visualizar(JDialog dialogo, int idCategoria){
        super(dialogo,"Visualizar Categoría",true);
        this.idCategoria = idCategoria;
        setSize(300,150);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        inicio();
        cargarDatos();
    }
    
    private void inicio(){
        setLayout(new GridLayout(2,1,5,5));
        add(new JLabel("Nombre:",SwingConstants.CENTER));
        nombre = new JLabel("",SwingConstants.CENTER);
        add(nombre);
    }
    
    private void cargarDatos(){
        try(Connection cn = mysqlConexiones.getConexion();
            PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM categorias WHERE id=?")){
            ps.setInt(1,idCategoria);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nombre.setText(rs.getString("nombre"));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
}

