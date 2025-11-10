/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MenuPrincipal;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import Recursos.mysqlConexiones;

/**
 *
 * @author practicante
 */
public class AcercaDe extends JDialog implements ActionListener {
    private Container contenedor;
    private JFrame padre;
    private JLabel nombre, carne, proyecto, version, fecha, fotoLabel;
    private JButton salir, cambiarFoto;
    private int usuarioId; // Puedes cambiarlo dinámicamente según el login
    private String rutaFotoActual;

    public AcercaDe(JFrame padre, int usuarioId) {
        super(padre, "Acerca De", true);
        this.padre = padre;
        this.usuarioId = usuarioId;

        this.setSize(600, 450);
        this.setLocationRelativeTo(padre);
        this.setResizable(false);

        inicio();
        cargarDatosUsuario(); // Llamamos al método para traer los datos de BD
    }

    private void inicio() {
        contenedor = getContentPane();
        contenedor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 5);

        // FOTO
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 3;
        fotoLabel = new JLabel();
        fotoLabel.setPreferredSize(new Dimension(120, 120));
        fotoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        contenedor.add(fotoLabel, c);

        // NOMBRE
        c.gridx = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        nombre = new JLabel("Nombre: ");
        contenedor.add(nombre, c);

        // CARNE
        c.gridy = 1;
        carne = new JLabel("Carné: ");
        contenedor.add(carne, c);

        // PROYECTO
        c.gridy = 2;
        c.gridwidth = 1;
        proyecto = new JLabel("Proyecto: ");
        contenedor.add(proyecto, c);

        // VERSION
        c.gridx = 2;
        version = new JLabel("Versión: ");
        contenedor.add(version, c);

        // BOTÓN CAMBIAR FOTO
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 1;
        cambiarFoto = new JButton("Cargar nueva Foto");
        cambiarFoto.addActionListener(this);
        contenedor.add(cambiarFoto, c);

        // FECHA
        c.gridx = 1;
        c.gridwidth = 2;
        fecha = new JLabel("Fecha: ");
        contenedor.add(fecha, c);

        // BOTÓN SALIR
        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = 3;
        salir = new JButton("Salir");
        salir.setPreferredSize(new Dimension(450, 25));
        salir.addActionListener(this);
        contenedor.add(salir, c);
    }

    private void cargarDatosUsuario() {
        Connection conn = mysqlConexiones.getConexion();
        PreparedStatement ps;
        ResultSet rs;
        try {
            String sql = "SELECT nombres, numero_carne, proyecto, version, fecha, foto_path FROM acerca_de WHERE usuario_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                nombre.setText("Nombre: " + rs.getString("nombres"));
                carne.setText("Carné: " + rs.getString("numero_carne"));
                proyecto.setText("Proyecto: " + rs.getString("proyecto"));
                version.setText("Versión: " + rs.getString("version"));
                fecha.setText("Fecha: " + rs.getString("fecha"));
                rutaFotoActual = rs.getString("foto_path");

                // Cargar imagen si existe
                if (rutaFotoActual != null && !rutaFotoActual.isEmpty()) {
                    ImageIcon icon = new ImageIcon(rutaFotoActual);
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    fotoLabel.setIcon(new ImageIcon(img));
                } else {
                    fotoLabel.setText("Sin foto");
                }
            } else {
                nombre.setText("Nombre: Desconocido");
                carne.setText("Carné: Desconocido");
                proyecto.setText("Proyecto: No asignado");
                version.setText("Versión: -");
                fecha.setText("Fecha: -");
                fotoLabel.setText("Sin foto");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar nueva foto");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();

            // Guardamos la ruta en la base de datos
            Connection conn = mysqlConexiones.getConexion();
            PreparedStatement ps;
            try {
                String sql = "UPDATE acerca_de SET foto_path = ? WHERE usuario_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, ruta);
                ps.setInt(2, usuarioId);
                int filas = ps.executeUpdate();

                if (filas > 0) {
                    ImageIcon icon = new ImageIcon(ruta);
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    fotoLabel.setIcon(new ImageIcon(img));
                    JOptionPane.showMessageDialog(this, "Foto actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la foto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cambiarFoto) {
            cambiarFoto();
        } else if (e.getSource() == salir) {
            this.dispose();
        }
    }
}

