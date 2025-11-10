/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recursos;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author practicante
 */
public class Recursos {
        public static void cargarIcono(JFrame frame, int ancho, int altura){
            try{
                ImageIcon iconoOriginal = new ImageIcon(Recursos.class.getResource("/Imagenes/Mariano_logo.png"));
                Image iconoEscalado = iconoOriginal.getImage().getScaledInstance(ancho, altura, Image.SCALE_SMOOTH);
                frame.setIconImage(iconoEscalado); 
            }catch(Exception e){
                frame.setIconImage(null);
            }
        }
        
        public static void cargarImagenLabel(JLabel label, String ruta, int ancho, int altura){
            try{
                ImageIcon imagenOriginal = new ImageIcon(Recursos.class.getResource(ruta));
                Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(ancho, altura, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imagenEscalada));
                label.setText("");
            }catch(Exception e){
                label.setText("Imagen no encontrada");
                label.setIcon(null);
            }
        }
}
