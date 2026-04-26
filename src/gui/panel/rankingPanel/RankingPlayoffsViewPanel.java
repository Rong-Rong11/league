package gui.panel.rankingPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.components.PlayoffsImageBracketPanel;
import gui.panel.common.DashboardCard;
import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.LabelStyleUtil;
import gui.panel.common.PlaceholderPanel;
import gui.panel.common.RoundedPanel;
import gui.panel.common.ThemeAware;
import process.orchestrator.interf.GUIInterface;

public class RankingPlayoffsViewPanel extends JPanel implements ThemeAware {
	private final GUIInterface guiInterface;
	private PlayoffsImageBracketPanel bracketPanel;

	public RankingPlayoffsViewPanel(GUIInterface guiInterface) {
		this.guiInterface = guiInterface;
		bracketPanel = new PlayoffsImageBracketPanel();
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		refreshPlayoffs();
	}

	public void refreshPlayoffs() {
		removeAll();

		if (!guiInterface.hasPlayoffsStarted() || !guiInterface.hasPlayoffData()) {
			add(buildEmptyPanel(), BorderLayout.CENTER);
			revalidate();
			repaint();
			return;
		}

		JPanel content = new JPanel(new BorderLayout(0, 16));
		content.setOpaque(false);
		content.add(buildSummaryCard(), BorderLayout.NORTH);
		content.add(buildBracketCard(), BorderLayout.CENTER);
		add(content, BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	private JPanel buildEmptyPanel() {
		return new PlaceholderPanel("Les playoffs ne sont pas encore disponibles. "
				+ "Terminez ou simulez la saison reguliere pour generer le tableau.");
	}

	private JPanel buildSummaryCard() {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout(12, 0));
		card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(buildTitleLabel());
		textPanel.add(Box.createVerticalStrut(4));
		textPanel.add(buildSubtitleLabel());
		addChampionLabel(textPanel);

		JPanel badgesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		badgesPanel.setOpaque(false);
		badgesPanel.add(createBadge("Round", guiInterface.getCurrentPlayoffRoundLabel()));
		badgesPanel.add(createBadge("Series", String.valueOf(guiInterface.getPlayoffSeriesCount())));

		card.add(textPanel, BorderLayout.CENTER);
		card.add(badgesPanel, BorderLayout.EAST);
		return card;
	}

	private JLabel buildTitleLabel() {
		JLabel titleLabel = new JLabel("Tableau des playoffs");
		LabelStyleUtil.styleTitleLabel(titleLabel, 16);
		return titleLabel;
	}

	private JLabel buildSubtitleLabel() {
		int qualifiedTeams = guiInterface.getPlayoffQualifiedTeamCount();
		JLabel subtitleLabel = new JLabel(qualifiedTeams + " equipes qualifiees pour la phase finale.");
		LabelStyleUtil.styleSubtitleLabel(subtitleLabel, 12);
		return subtitleLabel;
	}

	private void addChampionLabel(JPanel textPanel) {
		String championName = guiInterface.getPlayoffChampionName();
		if (championName == null || championName.equals("")) {
			return;
		}
		JLabel championLabel = new JLabel("Champion NBA : " + championName);
		LabelStyleUtil.styleValueLabel(championLabel, 13);
		textPanel.add(Box.createVerticalStrut(6));
		textPanel.add(championLabel);
	}

	private JPanel buildBracketCard() {
		DashboardCard card = new DashboardCard();
		card.setLayout(new BorderLayout());
		card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		bracketPanel.applyTheme();
		bracketPanel.refreshFromPlayoffsData(guiInterface.getPlayoffPositionMap());
		card.add(bracketPanel, BorderLayout.CENTER);
		return card;
	}

	private RoundedPanel createBadge(String label, String value) {
		RoundedPanel badge = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), 18);
		badge.setBackground(DashboardPanelUtil.getPrimaryActionColor());
		badge.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
		badge.setPreferredSize(new Dimension(120, 34));

		JLabel badgeLabel = new JLabel(label + " : " + value);
		badgeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		badgeLabel.setForeground(DashboardPanelUtil.getPrimaryActionTextColor());
		badge.add(badgeLabel);
		return badge;
	}

	@Override
	public void applyTheme() {
		bracketPanel.applyTheme();
		refreshPlayoffs();
	}
}
