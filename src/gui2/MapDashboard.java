package gui2;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dashboard dédié à la page Carte.
 */
public class MapDashboard extends JPanel {

    public MapDashboard() {
        setLayout(new BorderLayout());
        JTextField tf = new JTextField("CARTE");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        add(tf, BorderLayout.CENTER);

        tf.setBackground(new Color(226, 226, 226));
    }
}
