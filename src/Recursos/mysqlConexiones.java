/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recursos;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author practicante
 */
public class mysqlConexiones {
    private static final String url = "jdbc:mysql://localhost:3306/Biblioteca?serverTimezone=UTC&useSSL=false";
    private static final String user = "USUARIO";
    private static final String pass = "CLAVE";
    
    private JFrame padre;
    private JDialog dialog;
    
    public static Connection getConexion(){
        Connection conexion = null;
        try{
            conexion = DriverManager.getConnection(url , user , pass);
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al cocnectar con la base de datos: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }
}
