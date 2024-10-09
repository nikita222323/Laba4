
package GUI;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    JPanel panel = new Panel();

    public Window(){

        setTitle("Окно");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setSize(new Dimension(500,500));
        setLocationRelativeTo(null);
        getContentPane().add(panel);
    }
}
