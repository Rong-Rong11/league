package gui2;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dashboard dédié à la page Finance.
 */
public class FinanceDashboard extends JPanel {

    public FinanceDashboard() {
        setLayout(new BorderLayout());
        JTextField tf = new JTextField("FINANCE");
        tf.setEditable(false);
        tf.setHorizontalAlignment(JTextField.CENTER);
        add(tf, BorderLayout.CENTER);

        tf.setBackground(new Color(226, 226, 226));
    }
}
