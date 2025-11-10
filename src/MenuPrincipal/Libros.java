/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import Recursos.mysqlConexiones;
import MenuPrincipal.Libro.*;
/**
 *
 * @author practicante
 */
public class Libros extends JDialog implements ActionListener {
    private Container contenedor;
    private JButton nuevo, editar, borrar, visualizar, listar;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JFrame padre;
    
    public Libros(JFrame padre){
        super(padre,"Libros",true);
        this.padre = padre;
        
        setSize(700,400);
        setLocationRelativeTo(padre);
        setResizable(false);
        
        inicio();
        cargarLibros();
    }
    
    private void inicio(){
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        // --- Tabla ---
        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor", "Categoría", "Año", "Estado"}, 0
        ){
            @Override
            public boolean isCellEditable(int row, int column){ return false; }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scroll = new JScrollPane(tablaLibros);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 6;
        contenedor.add(scroll,c);
        
        // --- Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        nuevo = new JButton("Nuevo");
        editar = new JButton("Editar");
        borrar = new JButton("Eliminar");
        visualizar = new JButton("Visualizar");
        listar = new JButton("Listar");
        
        nuevo.addActionListener(this);
        editar.addActionListener(this);
        borrar.addActionListener(this);
        visualizar.addActionListener(this);
        listar.addActionListener(this);
        
        panelBotones.add(nuevo);
        panelBotones.add(editar);
        panelBotones.add(borrar);
        panelBotones.add(visualizar);
        panelBotones.add(listar);
        
        c.gridy = 1;
        c.gridx = 0;
        contenedor.add(panelBotones,c);
    }
    
    // --- Cargar todos los libros ---
    private void cargarLibros(){
        modeloTabla.setRowCount(0);
        String query = """
            SELECT l.id, l.titulo, a.nombre AS autor, c.nombre AS categoria, 
                   l.anio, l.stock
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            LEFT JOIN categorias c ON l.categoria_id = c.id
            ORDER BY l.id DESC;
        """;
        try(Connection cn = mysqlConexiones.getConexion();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(query)){
            
            while(rs.next()){
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("categoria"),
                    rs.getString("anio"),
                    rs.getString("stock")
                });
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }
    
    // --- Eliminar un libro seleccionado ---
    private void eliminarLibro(){
        int fila = tablaLibros.getSelectedRow();
        if(fila == -1){
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            try(Connection cn = mysqlConexiones.getConexion();
                PreparedStatement ps = cn.prepareStatement("DELETE FROM libros WHERE id=?")){
                
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.");
                cargarLibros();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    // --- Eventos de los botones ---
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == nuevo){
            nuevo ventana = new nuevo(this);
            ventana.setVisible(true);
            cargarLibros();
        } 
        else if(e.getSource() == editar){
            int fila = tablaLibros.getSelectedRow();
            if(fila != -1){
                int id = (int) modeloTabla.getValueAt(fila, 0);
                editar ventana = new editar(this,id);
                ventana.setVisible(true);
                cargarLibros();
            }else{
                JOptionPane.showMessageDialog(this,"Selecciona un libro para editar.");
            }
        } 
        else if(e.getSource() == borrar){
            eliminarLibro();
        } 
        else if(e.getSource() == visualizar){
            int fila = tablaLibros.getSelectedRow();
            if(fila != -1){
                int id = (int) modeloTabla.getValueAt(fila, 0);
                visualizar ventana = new visualizar(this,id);
                ventana.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this,"Selecciona un libro para visualizar.");
            }
        } 
        else if(e.getSource() == listar){
            cargarLibros();
        }
    }
}

