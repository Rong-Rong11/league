package gui.panel.common;

import java.awt.Component;

import javax.swing.JOptionPane;

public class InfoPopupUtil {
	public static void showInfoPopup(Component parentComponent, String title, String message) {
		JOptionPane.showMessageDialog(
				parentComponent,
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
