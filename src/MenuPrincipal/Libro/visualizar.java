/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal.Libro;

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
    private JLabel titulo, autor, categoria, anio, stock;
    private int idLibro;
    
    public visualizar(JDialog dialogo, int idLibro){
        super(dialogo,"Visualizar Libro",true);
        this.idLibro = idLibro;
        
        setSize(400,250);
        setLocationRelativeTo(dialogo);
        setResizable(false);
        
        inicio();
        cargarDatos();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridLayout(5,2,5,5));
        
        contenedor.add(new JLabel("Título:"));
        titulo = new JLabel();
        contenedor.add(titulo);
        
        contenedor.add(new JLabel("Autor:"));
        autor = new JLabel();
        contenedor.add(autor);
        
        contenedor.add(new JLabel("Categoría:"));
        categoria = new JLabel();
        contenedor.add(categoria);
        
        contenedor.add(new JLabel("Año:"));
        anio = new JLabel();
        contenedor.add(anio);
        
        contenedor.add(new JLabel("Estado:"));
        stock = new JLabel();
        contenedor.add(stock);
    }
    
    private void cargarDatos(){
        String sql = """
            SELECT l.titulo, a.nombre AS autor, c.nombre AS categoria, l.anio, l.stock
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            LEFT JOIN categorias c ON l.categoria_id = c.id
            WHERE l.id = ?
        """;
        
        try (Connection cn = mysqlConexiones.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                titulo.setText(rs.getString("titulo"));
                autor.setText(rs.getString("autor"));
                categoria.setText(rs.getString("categoria"));
                anio.setText(rs.getString("anio"));
                stock.setText(rs.getString("stock"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }
}

