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
import MenuPrincipal.Categoria.*;
/**
 *
 * @author practicante
 */
public class Categorias extends JDialog implements ActionListener {
    private Container contenedor;
    private JButton nuevo, editar, borrar, visualizar, listar;
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    private JFrame padre;
    
    public Categorias(JFrame padre){
        super(padre,"Categorías",true);
        this.padre = padre;
        
        setSize(500,400);
        setLocationRelativeTo(padre);
        setResizable(false);
        
        inicio();
        cargarCategorias();
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
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){ return false; }
        };
        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scroll = new JScrollPane(tablaCategorias);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 5;
        contenedor.add(scroll,c);
        
        // --- Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
        nuevo = new JButton("Nueva");
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
    
    private void cargarCategorias(){
        modeloTabla.setRowCount(0);
        try(Connection cn = mysqlConexiones.getConexion();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM categorias")){
            
            while(rs.next()){
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre")
                });
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage());
        }
    }
    
    private void eliminarCategoria(){
        int fila = tablaCategorias.getSelectedRow();
        if(fila == -1){
            JOptionPane.showMessageDialog(this, "Selecciona una categoría para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            try(Connection cn = mysqlConexiones.getConexion();
                PreparedStatement ps = cn.prepareStatement("DELETE FROM categorias WHERE id=?")){
                
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Categoría eliminada.");
                cargarCategorias();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == nuevo){
            nuevo ventana = new nuevo(this);
            ventana.setVisible(true);
            cargarCategorias();
        } else if(e.getSource() == editar){
            int fila = tablaCategorias.getSelectedRow();
            if(fila != -1){
                int id = (int) modeloTabla.getValueAt(fila, 0);
                editar ventana = new editar(this,id);
                ventana.setVisible(true);
                cargarCategorias();
            }
        } else if(e.getSource() == borrar){
            eliminarCategoria();
        } else if(e.getSource() == visualizar){
            int fila = tablaCategorias.getSelectedRow();
            if(fila != -1){
                int id = (int) modeloTabla.getValueAt(fila, 0);
                visualizar ventana = new visualizar(this,id);
                ventana.setVisible(true);
            }
        } else if(e.getSource() == listar){
            cargarCategorias();
        }
    }
}
