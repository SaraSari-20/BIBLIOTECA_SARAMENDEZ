/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Start;
import javax.swing.SwingUtilities;


import Login.Login;
/**
 *
 * @author practicante
 */
public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login log = new Login();
            log.setVisible(true);
        });
    }
}
