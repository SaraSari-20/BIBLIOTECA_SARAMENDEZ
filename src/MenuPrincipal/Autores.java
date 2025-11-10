/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import MenuPrincipal.Autor.*;
import Recursos.mysqlConexiones;

/**
 *
 * @author practicante
 */
public class Autores extends JDialog implements ActionListener{
    private Container contenedor;
    private JButton nuevo, editar,borrar,visualizar,listar;
    
    private JTable tablaAutores;
    private DefaultTableModel modeloTabla;
    
    private JFrame padre;
    
    public Autores(JFrame padre){
        super(padre,"Autores",true);
        this.padre = padre;
        
        this.setSize(500,400);
        this.setLocationRelativeTo(padre);
        this.setResizable(false);
        
        //Recursos.mysqlConexiones.getConexion();
        inicio();
        cargarAutores();
    }
    
    private void inicio() {
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        // --- Tabla ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Nacionalidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // que no se pueda editar directamente
            }
        };
        tablaAutores = new JTable(modeloTabla);
        tablaAutores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAutores.getTableHeader().setReorderingAllowed(false); // no mover columnas

        JScrollPane scroll = new JScrollPane(tablaAutores);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 5;
        c.weighty = 1.0;
        contenedor.add(scroll, c);

        // --- Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

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
        c.gridwidth = 5;
        c.weighty = 0;
        contenedor.add(panelBotones, c);
    }
    
    private void cargarAutores() {
        modeloTabla.setRowCount(0); // limpia la tabla
        try (Connection cn = mysqlConexiones.getConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre, nacionalidad FROM autores")) {

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("nacionalidad")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los autores: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarAutor() {
        int fila = tablaAutores.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un autor para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este autor?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection cn = mysqlConexiones.getConexion();
                 PreparedStatement ps = cn.prepareStatement("DELETE FROM autores WHERE id = ?")) {

                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Autor eliminado correctamente.");
                cargarAutores();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el autor: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nuevo) {
            nuevo ventana = new nuevo(this);
            ventana.setVisible(true);
            cargarAutores();

        } else if (e.getSource() == editar) {
            
            int fila = tablaAutores.getSelectedRow();
            if(fila != -1){
                int idAutor = (int) modeloTabla.getValueAt(fila, 0);
                editar ventana = new editar(this, idAutor);
                ventana.setVisible(true);
            }

        } else if (e.getSource() == borrar) {
            eliminarAutor();

        } else if (e.getSource() == visualizar) {
            int fila = tablaAutores.getSelectedRow();
            if(fila != -1){
                int idVisualizar = (int) modeloTabla.getValueAt(fila, 0);
                visualizar ventana = new visualizar(this, idVisualizar);
                ventana.setVisible(true);
            }

        } else if (e.getSource() == listar) {
            cargarAutores();
        }
    }
}

