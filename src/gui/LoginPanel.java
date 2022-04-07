package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.PlayerController;

public class LoginPanel extends JPanel {
    private final JButton continueGame = new JButton(" CONTINUE ");
    private final JButton newGame = new JButton(" NEW GAME ");
    private final JButton quitGame = new JButton(" QUIT GAME ");
    private final PlayerController playerController;

    public LoginPanel(PlayerController playerController) {
	this.playerController = playerController;
	init();
    }

    private void init() {
	this.setLayout(new GridBagLayout());
	continueGame.setEnabled(continueButtonVisibility());
	final GridBagConstraints cons = new GridBagConstraints();
	cons.gridy = 0;
	cons.fill = GridBagConstraints.HORIZONTAL;
	cons.ipady = 50;
	cons.weightx = 0;
	cons.insets = new Insets(50, 0, 0, 0);
	this.add(continueGame, cons);
	cons.gridy++;
	this.add(newGame, cons);
	cons.gridy++;
	this.add(quitGame, cons);
    }

    public JButton getContinue() {
	return this.continueGame;
    }

    public JButton getnewGame() {
	return this.newGame;
    }

    public JButton getquitGame() {
	return this.quitGame;
    }

    private boolean continueButtonVisibility() {
	return true;// controllare se ce il salvataggio
    }
}
