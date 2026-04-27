package gui.panel.financePanel;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.team.Team;

public class TeamFinanceProfilePanel extends JPanel {
	private final JLabel profileValueLabel;
	private final JLabel marketValueLabel;
	private final JLabel strategyValueLabel;
	private final JLabel ticketPriceValueLabel;
	private final JLabel capacityValueLabel;
	private final JLabel fanLoyaltyValueLabel;
	private final JLabel commercialAggressivenessValueLabel;
	private final JLabel historicalPrestigeValueLabel;
	private final JLabel fanBaseValueLabel;
	private final JLabel businessOpportunityValueLabel;
	private final JLabel pricingPowerValueLabel;
	private final JLabel luxuryTaxValueLabel;

	public TeamFinanceProfilePanel() {
		super();
		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
		setOpaque(false);

		profileValueLabel = FinanceViewFactory.infoLabel();
		marketValueLabel = FinanceViewFactory.infoLabel();
		strategyValueLabel = FinanceViewFactory.infoLabel();
		ticketPriceValueLabel = FinanceViewFactory.infoLabel();
		capacityValueLabel = FinanceViewFactory.infoLabel();
		fanLoyaltyValueLabel = FinanceViewFactory.infoLabel();
		commercialAggressivenessValueLabel = FinanceViewFactory.infoLabel();
		historicalPrestigeValueLabel = FinanceViewFactory.infoLabel();
		fanBaseValueLabel = FinanceViewFactory.infoLabel();
		businessOpportunityValueLabel = FinanceViewFactory.infoLabel();
		pricingPowerValueLabel = FinanceViewFactory.infoLabel();
		luxuryTaxValueLabel = FinanceViewFactory.infoLabel();

		buildContent();
	}

	private void buildContent() {
		JPanel profilePanel = FinanceViewFactory.infoPanel();
		addProfileRow(profilePanel, "Politique financiere", profileValueLabel);
		addProfileRow(profilePanel, "Taille du marche", marketValueLabel);
		addProfileRow(profilePanel, "Fidelite des fans", fanLoyaltyValueLabel);
		addProfileRow(profilePanel, "Agressivite commerciale", commercialAggressivenessValueLabel);
		addProfileRow(profilePanel, "Prestige historique", historicalPrestigeValueLabel);
		addProfileRow(profilePanel, "Base de fans", fanBaseValueLabel);
		addProfileRow(profilePanel, "Opportunites commerciales", businessOpportunityValueLabel);
		addProfileRow(profilePanel, "Pouvoir de prix", pricingPowerValueLabel);
		addProfileRow(profilePanel, "Strategie", strategyValueLabel);
		addProfileRow(profilePanel, "Taxe de luxe payee", luxuryTaxValueLabel);

		JPanel infrastructurePanel = FinanceViewFactory.infoPanel();
		addProfileRow(infrastructurePanel, "Capacite", capacityValueLabel);
		addProfileRow(infrastructurePanel, "Prix billet", ticketPriceValueLabel);

		setLayout(new java.awt.GridLayout(2, 1, 0, TeamFinanceViewPanel.RIGHT_COLUMN_SECTION_SPACING));
		add(profilePanel);
		add(infrastructurePanel);
	}

	private void addProfileRow(JPanel panel, String title, JLabel valueLabel) {
		panel.add(FinanceViewFactory.infoRow(title, valueLabel));
		panel.add(Box.createVerticalStrut(10));
	}

	public void updateForTeam(Team team, TeamFinanceViewPanel owner) {
		luxuryTaxValueLabel.setText(FinanceDataUtil.formatMoney(owner.getLuxuryTaxPaid(team)));
		profileValueLabel.setText(FinanceDataUtil.formatPolicy(owner.getFinancialPolicy(team)));
		marketValueLabel.setText(FinanceDataUtil.formatMarket(owner.getMarketSize(team)));
		strategyValueLabel.setText(FinanceDataUtil.formatStrategy(owner.getTransferStrategy(team)));
		fanBaseValueLabel.setText(owner.formatCoefficient(owner.getFanBase(team)));
		businessOpportunityValueLabel.setText(owner.formatCoefficient(owner.getBusinessOpportunity(team)));
		pricingPowerValueLabel.setText(owner.formatCoefficient(owner.getPricingPower(team)));
		ticketPriceValueLabel.setText(team.getStadium() == null ? "-" : FinanceDataUtil.formatMoney(team.getStadium().getTicketPrice()));
		capacityValueLabel.setText(team.getStadium() == null ? "-" : String.valueOf(team.getStadium().getCapacity()));
		fanLoyaltyValueLabel.setText(owner.formatCoefficient(owner.getFanLoyalty(team)));
		commercialAggressivenessValueLabel.setText(owner.formatCoefficient(owner.getCommercialAggressiveness(team)));
		historicalPrestigeValueLabel.setText(owner.formatCoefficient(owner.getHistoricalPrestige(team)));

		FinanceDataUtil.setPolicyColor(profileValueLabel, profileValueLabel.getText());
		FinanceDataUtil.setMarketColor(marketValueLabel, marketValueLabel.getText());
		FinanceDataUtil.setStrategyColor(strategyValueLabel, strategyValueLabel.getText());
		ticketPriceValueLabel.setForeground(gui.panel.common.DashboardPanelUtil.REVENUE_COLOR);
		capacityValueLabel.setForeground(gui.panel.common.DashboardPanelUtil.POLICY_BALANCED_COLOR);
		luxuryTaxValueLabel.setForeground(gui.panel.common.DashboardPanelUtil.EXPENSE_COLOR);
	}

	public void showEmptyState(String[] texts) {
		JLabel[] labels = getLabels();
		for (int index = 0; index < labels.length && index < texts.length; index++) {
			labels[index].setText(texts[index]);
			gui.panel.common.LabelStyleUtil.styleSubtitleLabel(labels[index], 12);
		}
	}

	private JLabel[] getLabels() {
		return new JLabel[] {
			profileValueLabel, marketValueLabel, strategyValueLabel, ticketPriceValueLabel, capacityValueLabel,
			fanLoyaltyValueLabel, commercialAggressivenessValueLabel, historicalPrestigeValueLabel,
			fanBaseValueLabel, businessOpportunityValueLabel, pricingPowerValueLabel, luxuryTaxValueLabel
		};
	}
}
