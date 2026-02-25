package gui2;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dashboard dédié à la page Classement.
 */
public class RankingDashboard extends JPanel {

    public RankingDashboard() {
        setLayout(new BorderLayout());
        JTextField tf = new JTextField("CLASSEMENT");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        add(tf, BorderLayout.CENTER);

        tf.setBackground(new Color(226, 226, 226));
    }
}
