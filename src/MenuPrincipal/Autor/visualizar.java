/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Autor;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import Recursos.mysqlConexiones;
/**
 *
 * @author practicante
 */
public class visualizar extends JDialog {
    private Container contenedor;
    private JLabel nombre, nacionalidad;
    private int idAutor;
    
    public visualizar(JDialog dialogo, int idAutor){
        super(dialogo,"Visualizar Autor",true);
        this.idAutor = idAutor;
        
        setSize(300,200);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        
        inicio();
        cargarDatos();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridLayout(3,1,5,5));
        
        nombre = new JLabel();
        nacionalidad = new JLabel();
        
        contenedor.add(new JLabel("Nombre:"));
        contenedor.add(nombre);
        contenedor.add(new JLabel("Nacionalidad:"));
        contenedor.add(nacionalidad);
    }
    
    private void cargarDatos(){
        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM autores WHERE id = ?")) {
            
            ps.setInt(1, idAutor);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                nombre.setText(rs.getString("nombre"));
                nacionalidad.setText(rs.getString("nacionalidad"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
