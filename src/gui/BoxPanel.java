package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.PlayerController;

public class BoxPanel extends JPanel {
    private static final int NUMBEROFMONSTERPERPAGE = 10;
    private final PlayerController playerController;
    private final CardLayout cardLayout = new CardLayout();
    private List<Integer> playerMonsterIdList = new ArrayList<Integer>();
    private List<Integer> boxMonsterIdList = new ArrayList<Integer>();
    private int idBoxMonster;
    private int idPlayerMonster;
    private final JButton exchange = new JButton("Exchange");
    private final JButton deposit = new JButton("Deposit");
    private final JButton take = new JButton("Take");
    private final JButton cancel = new JButton("Cancel");
    int exchangecont;

    public BoxPanel(PlayerController playerController) {
	this.playerController = playerController;
	setButtons();
	setList();
	init();
    }

    private void setList() {
	playerMonsterIdList = this.playerController.getMonstersId();
	// TODO initialization of boxlist
    }

    private void setButtons() {
	exchange.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
		update();
	    }
	});
	deposit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
		update();

	    }
	});
	take.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
		update();

	    }
	});
	cancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(null, "Sure cancel?", "Warning", JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
		    update();
		}
	    }
	});
    }

    private void init() {
	this.setLayout(new BorderLayout());
	exchangecont = 0;
	setButtonDisabled(exchange);
	setButtonDisabled(deposit);
	setButtonDisabled(take);
	JPanel playerMonstersPanel = new JPanel(new GridLayout(0, 1));
	playerMonstersPanel.setPreferredSize(new Dimension(200, 500));

	for (int playerMonsterid : playerMonsterIdList) {
	    playerMonstersPanel.add(setPlayerMonsterPanel(playerMonsterid, playerMonstersPanel));
	}
	JPanel boxPanel = new JPanel();
	boxPanel.setLayout(cardLayout);
	
	List<JPanel> contentPanel = new ArrayList<JPanel>();

	int remainelement = boxMonsterIdList.size();
	for (int a = 0; a < numberOfPage(boxMonsterIdList); a++) {
	    int moltiplicatore = NUMBEROFMONSTERPERPAGE * a;
	    JPanel pagePanel = new JPanel(new GridLayout(NUMBEROFMONSTERPERPAGE, 1));
	    for (int i = 0; i < (((remainelement - NUMBEROFMONSTERPERPAGE) > 0) ? NUMBEROFMONSTERPERPAGE
		    : remainelement); i++) {
		int id = boxMonsterIdList.get(i + moltiplicatore);
		pagePanel.add(setBoxMonster(id, boxPanel));
	    }
	    remainelement = Math.abs(remainelement - NUMBEROFMONSTERPERPAGE);
	    contentPanel.add(pagePanel);
	}

	int index = 1;
	for (JPanel panel : contentPanel) {
	    boxPanel.add(panel, Integer.toString(index));
	    index++;
	}

	JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
	JButton previusPage = new JButton("Previews box");
	JButton nextPage = new JButton("Next box");
	bottomPanel.add(previusPage);
	bottomPanel.add(nextPage);

	nextPage.addActionListener(e -> cardLayout.next(boxPanel));
	previusPage.addActionListener(e -> cardLayout.previous(boxPanel));

	JPanel topPanel = new JPanel(new GridLayout(1, 5));

	topPanel.add(exchange);
	topPanel.add(deposit);
	topPanel.add(take);
	topPanel.add(cancel);

	this.add(topPanel, BorderLayout.NORTH);
	this.add(boxPanel, BorderLayout.CENTER);
	this.add(playerMonstersPanel, BorderLayout.EAST);
	this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setJCheckBoxProp(JCheckBox checkbox) {
	checkbox.setBorder(BorderFactory.createLineBorder(Color.blue));
	checkbox.setBorderPainted(true);
	checkbox.setHorizontalAlignment(SwingConstants.CENTER);
	checkbox.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void setPanelEnabled(java.awt.Container cont, Boolean isEnabled) {
	cont.setEnabled(isEnabled);

	java.awt.Component[] components = cont.getComponents();

	for (int i = 0; i < components.length; i++) {
	    if (components[i] instanceof java.awt.Container) {
		setPanelEnabled((java.awt.Container) components[i], isEnabled);
	    }
	    components[i].setEnabled(isEnabled);
	}
    }

    private void setIdBoxMonster(int idBoxMonster) {
	this.idBoxMonster = idBoxMonster;
    }

    private void setIdPlayerMonster(int idPlayerMonster) {
	this.idPlayerMonster = idPlayerMonster;
    }

    private JPanel setPlayerMonsterPanel(int monsterId, JPanel playerMonstersPanel) {
	JPanel panel = new JPanel(new GridLayout(1, 2));
	panel.setBorder(BorderFactory.createLineBorder(Color.black));
	JLabel label = new JLabel();
	label.setText(" " + this.playerController.getMonsterNameById(monsterId) + "  "
		+ this.playerController.getMonsterLevel(monsterId) + "  "
		+ this.playerController.getMonsterHealth(monsterId) + "/"
		+ this.playerController.getMonsterMaxHealth(monsterId));
	setLabelProp(label);
	JCheckBox checkBoxPlayer = new JCheckBox();
	setJCheckBoxProp(checkBoxPlayer);
	checkBoxPlayer.setText(Integer.toString(monsterId));
	checkBoxPlayer.setForeground(new Color(0, 0, 0, 0));
	checkBoxPlayer.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JCheckBox cb = (JCheckBox) e.getSource();
		int result = JOptionPane.showConfirmDialog(null, "Sure?", "Warning", JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
		    if (cb.isSelected()) {
			setIdPlayerMonster(Integer.parseInt(cb.getText()));
			setPanelEnabled(playerMonstersPanel, false);
			setButtonEnabled(deposit);
			exchangecont++;
			if (exchangecont == 2) {
			    setButtonEnabled(exchange);
			}
		    }
		} else {
		    cb.setSelected(false);
		}
	    }
	});
	panel.add(label);
	panel.add(checkBoxPlayer);
	return panel;

    }

    private JCheckBox setBoxMonster(int boxMonsterId, JPanel boxPanel) {
	JPanel panel = new JPanel(new GridLayout(1, 2));
	panel.setBorder(BorderFactory.createLineBorder(Color.black));
	JLabel label = new JLabel();
	label.setText(" " + this.playerController.getMonsterNameById(boxMonsterId) + "  "
		+ this.playerController.getMonsterLevel(boxMonsterId) + "  "
		+ this.playerController.getMonsterHealth(boxMonsterId) + "/"
		+ this.playerController.getMonsterMaxHealth(boxMonsterId));
	setLabelProp(label);
	JCheckBox checkBoxPlayer = new JCheckBox();
	setJCheckBoxProp(checkBoxPlayer);
	checkBoxPlayer.setText(Integer.toString(boxMonsterId));
	checkBoxPlayer.setForeground(new Color(0, 0, 0, 0));

	checkBoxPlayer.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JCheckBox cb = (JCheckBox) e.getSource();
		int result = JOptionPane.showConfirmDialog(null, "Sure?", "Warning", JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
		    if (cb.isSelected()) {
			setIdBoxMonster(Integer.parseInt(cb.getText()));
			setPanelEnabled(boxPanel, false);
			setButtonEnabled(take);
			exchangecont++;
			if (exchangecont == 2) {
			    setButtonEnabled(exchange);
			}
		    }
		} else {
		    cb.setSelected(false);
		}
	    }
	});
	return checkBoxPlayer;
    }

    private int numberOfPage(List<Integer> boxNames) {
	return ((int) boxNames.size() / NUMBEROFMONSTERPERPAGE) + 1;
    }

    private void setButtonEnabled(JButton button) {
	button.setEnabled(true);
    }

    private void setButtonDisabled(JButton button) {
	button.setEnabled(false);
    }

    private void setLabelProp(JLabel label) {
	label.setHorizontalAlignment(SwingConstants.CENTER);
	label.setVerticalAlignment(SwingConstants.CENTER);
    }

    public void update() {
	this.removeAll();
	this.init();
	this.validate();
    }

}
