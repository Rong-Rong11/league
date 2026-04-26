package gui.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import gui.panel.common.DashboardPanelUtil;
import gui.panel.common.ThemeAware;

public class LoadingDashboard extends JPanel implements ThemeAware {

   private JLabel titleLabel;
   private JLabel subtitleLabel;
   private JProgressBar progressBar;

   public LoadingDashboard() {
      create();
      organize();
      applyTheme();
   }

   private void create() {
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(120, 220, 120, 220));

      titleLabel = new JLabel("Chargement de la saison", JLabel.CENTER);
      titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));

      subtitleLabel = new JLabel("Preparation du calendrier, des matchs et des finances...", JLabel.CENTER);
      subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

      progressBar = new JProgressBar(0, 100);
      progressBar.setStringPainted(true);
      progressBar.setValue(0);
   }

   private void organize() {
      JPanel centerPanel = new JPanel(new BorderLayout(0, 28));
      centerPanel.setOpaque(false);

      JPanel textPanel = new JPanel(new BorderLayout(0, 12));
      textPanel.setOpaque(false);
      textPanel.add(titleLabel, BorderLayout.NORTH);
      textPanel.add(subtitleLabel, BorderLayout.CENTER);

      centerPanel.add(textPanel, BorderLayout.NORTH);
      centerPanel.add(progressBar, BorderLayout.CENTER);

      add(centerPanel, BorderLayout.CENTER);
   }

   public void reset() {
      progressBar.setValue(0);
   }

   public void setProgress(int value, String text) {
      progressBar.setValue(value);
      progressBar.setString(value + "%");
      subtitleLabel.setText(text);
   }

   @Override
   public void applyTheme() {
      setBackground(DashboardPanelUtil.DASHBOARD_BACKGROUND_COLOR);
      titleLabel.setForeground(DashboardPanelUtil.TITLE_TEXT_COLOR);
      subtitleLabel.setForeground(DashboardPanelUtil.SUBTITLE_TEXT_COLOR);
      progressBar.setForeground(DashboardPanelUtil.getPrimaryActionColor());
      progressBar.setBackground(DashboardPanelUtil.BUTTON_SURFACE_COLOR);
   }
}
