package gui2;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dashboard dédié à la page Match.
 */
public class MatchDashboard extends JPanel {

    public MatchDashboard() {
        setLayout(new BorderLayout());
        JTextField tf = new JTextField("MATCH");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        add(tf, BorderLayout.CENTER);

        tf.setBackground(new Color(226, 226, 226));
    }
}
