package gui.panel.mapPanel.effectifPanel.tradePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.panel.mapPanel.effectifPanel.playerPanel.PlayerPortraitPanel;

public final class TradeEntryPanelFactory {
	private TradeEntryPanelFactory() {
	}

	public static JPanel buildHeaderPanel(JLabel dateLabel, JLabel partnerLabel, int portraitWidth, int portraitHeight,
			int lineGap) {
		JPanel headerPanel = new JPanel(new BorderLayout(lineGap, 0));
		headerPanel.setOpaque(false);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(dateLabel);
		textPanel.add(Box.createVerticalStrut(2));
		textPanel.add(partnerLabel);

		headerPanel.add(buildPortraitSpacer(portraitWidth, portraitHeight), BorderLayout.WEST);
		headerPanel.add(textPanel, BorderLayout.CENTER);
		return headerPanel;
	}

	public static JPanel buildTradePlayerLine(PlayerPortraitPanel portraitPanel, JLabel titleLabel, JLabel detailLabel,
			int lineGap) {
		JPanel linePanel = new JPanel(new BorderLayout(lineGap, 0));
		linePanel.setOpaque(false);

		JPanel portraitPanelContainer = new JPanel(new BorderLayout());
		portraitPanelContainer.setOpaque(false);
		portraitPanelContainer.add(portraitPanel, BorderLayout.NORTH);

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(titleLabel);
		textPanel.add(Box.createVerticalStrut(1));
		textPanel.add(detailLabel);

		linePanel.add(portraitPanelContainer, BorderLayout.WEST);
		linePanel.add(textPanel, BorderLayout.CENTER);
		return linePanel;
	}

	private static JPanel buildPortraitSpacer(int portraitWidth, int portraitHeight) {
		JPanel spacer = new JPanel();
		spacer.setOpaque(false);
		spacer.setPreferredSize(new Dimension(portraitWidth, portraitHeight));
		return spacer;
	}
}
