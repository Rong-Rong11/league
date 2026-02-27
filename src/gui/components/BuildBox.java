package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * Surcharge de constructeur :
 *
 * Cette classe possède deux constructeurs avec le même nom (BuildBox),
 * mais avec des paramètres différents.
 *
 * - Le premier constructeur permet d'injecter directement un JPanel
 *   comme contenu principal (ex : graphique, tableau, etc.).
 *
 * - Le second constructeur crée automatiquement un panneau placeholder
 *   à partir d'un simple texte, utile lorsque le contenu réel n'est
 *   pas encore disponible.
 *
 * Java choisit automatiquement le bon constructeur en fonction
 * des arguments passés lors de l'instanciation.
 */
public class BuildBox extends DashboardCard {

	private static final Color PLACEHOLDER_BACKGROUND = new Color(226, 226, 226);

	public BuildBox(String title, String subtitle, JPanel content) { //!premier constructeur
		setLayout(new BorderLayout());

		JPanel titlePart = new SectionTitle(title, subtitle);
		add(titlePart, BorderLayout.NORTH);

		add(content, BorderLayout.CENTER);
	}

	public BuildBox(String title, String subtitle, String placeholderText) {//!second constructeur
		setLayout(new BorderLayout());

		JPanel titlePart = new SectionTitle(title, subtitle);
		add(titlePart, BorderLayout.NORTH);

		JPanel placeholderPart = new JPanel(new BorderLayout());
		placeholderPart.setBackground(PLACEHOLDER_BACKGROUND);
		placeholderPart.setOpaque(true);
		placeholderPart.add(new JLabel(placeholderText, JLabel.CENTER), BorderLayout.CENTER);

		add(placeholderPart, BorderLayout.CENTER);
	}
}
