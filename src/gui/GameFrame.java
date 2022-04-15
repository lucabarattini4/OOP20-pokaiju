package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Direction;
import controller.PlayerController;

public class GameFrame extends JFrame {
    private static final long serialVersionUID = -7927156597267134363L;
    static final String NEW_GAME_PANEL = "new game";
    static final String LOGIN_PANEL = "login panel";
    static final String MENU_PANEL = "menu";
    static final String MAP_PANEL = "map panel";
    static final String BATTLE_PANEL = "battle panel";
    static final String MERCHANT_PANEL = "merchant panel";
    static final String EVOLVE_PANEL = "evolve panel";

    private final int size;
    private final CardLayout cLayout = new CardLayout();
    private final Map<String, JPanel> subPanels = new HashMap<>();
    private final ImagesLoader imgLoad;
    private final JPanel mainPanel = new JPanel();
    private final PlayerController playerController;

    public GameFrame(PlayerController playerController) {
	this.playerController = playerController;
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setResizable(false);
	this.setContentPane(mainPanel);

	size = getMainPanelSize();
	imgLoad = new ImagesLoader(size, size, playerController.getMaximumBlocksInRow(),
		playerController.getMaximumBlocksInColumn());
	mainPanel.setPreferredSize(new Dimension(size, size));
	mainPanel.setBounds(0, 0, size, size);
	mainPanel.setLayout(cLayout);

	LoginPanel loginPanel = new LoginPanel();

	if (!subPanels.containsKey(MAP_PANEL)) {
	    JPanel gamePanel = buildMapPanel();
	    mainPanel.add(gamePanel, MAP_PANEL);
	    subPanels.put(MAP_PANEL, gamePanel);
	}

	loginPanel.getnewGame().addActionListener(e -> changePanel(NEW_GAME_PANEL));
	loginPanel.getquitGame().addActionListener(e -> System.exit(0));

	JPanel evolvePanel = new EvolutionPanel(playerController, this, imgLoad);

	JPanel newGamePanel = newGamePanel();

	JPanel menuPanel = buildMenuPanel();

	JPanel battlePanel = new BattlePanel(imgLoad, this);

	mainPanel.add(loginPanel, LOGIN_PANEL);
	mainPanel.add(newGamePanel, NEW_GAME_PANEL);
	mainPanel.add(menuPanel, MENU_PANEL);
	mainPanel.add(battlePanel, BATTLE_PANEL);
	mainPanel.add(evolvePanel, EVOLVE_PANEL);

	subPanels.put(LOGIN_PANEL, loginPanel);
	subPanels.put(NEW_GAME_PANEL, newGamePanel);
	subPanels.put(MENU_PANEL, menuPanel);
	mainPanel.add(BATTLE_PANEL, battlePanel);

	this.pack();
	this.setVisible(true);

    }

    private int getMainPanelSize() {
	double percScreen = 5 / 6;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int s = (int) (screenSize.getHeight() > screenSize.getWidth() ? screenSize.getWidth() : screenSize.getHeight());
	s = (int) (s * percScreen);
	System.out.println(s);
	return s;
    }

    private JPanel buildMapPanel() {
	TwoLayersPanel mapPanel = new TwoLayersPanel(playerController, imgLoad, this.getHeight(), this.getWidth());
	mapPanel.addKeyListener(new PlayerCommands(this));
	return mapPanel;
    }

    public void movePlayer(Direction dir) {
	TwoLayersPanel p = (TwoLayersPanel) subPanels.get(MAP_PANEL);
	PlayerPanel topPanel = p.getTopPanel();
	boolean animationOn = true;
	boolean canPlayerMove = this.playerController.movePlayer(dir);
	if (canPlayerMove) {
	    topPanel.setNextPosition(this.playerController.getPlayerPosition());
	    if (playerController.hasPlayerChangedMap()) {
		List<BufferedImage> mapImageSequence = imgLoad.getMapByID(this.playerController.getCurrentMapID());
		p.setMapImage(mapImageSequence);
		topPanel.setNpcs(this.playerController.getAllNpcs());
		animationOn = false;
	    }
	}

	if (animationOn) {
	    topPanel.animatedMove(dir, canPlayerMove);
	} else {
	    topPanel.staticMove();
	}
	changeToBattle();
	if (playerController.hasPlayerTriggeredEvent()) {
	    topPanel.setNpcs(this.playerController.getAllNpcs());
	}
    }

    private void changeToBattle() {
	if (playerController.hasBattleStarted()) {
	    BattlePanel b = (BattlePanel) (this.subPanels.get(BATTLE_PANEL));
	    b.setBattleController(this.playerController.getBattleController().get(), this.playerController);
	    changePanel(BATTLE_PANEL);
	}
    }

    public boolean playerInteraction() {
	TwoLayersPanel p = (TwoLayersPanel) subPanels.get(MAP_PANEL);
	PlayerPanel topPanel = p.getTopPanel();
	Optional<String> text = playerController.interact();
	if (text.isPresent()) {
	    topPanel.showText(text.get());
	}
	if (playerController.hasPlayerTriggeredEvent()) {
	    topPanel.setNpcs(this.playerController.getAllNpcs());
	}
	return text.isPresent();
    }

    public void endPlayerInteraction() {
	TwoLayersPanel p = (TwoLayersPanel) subPanels.get(MAP_PANEL);
	PlayerPanel topPanel = p.getTopPanel();
	topPanel.hideText();
	changeToBattle();
	if (this.playerController.hasMerchantInteractionOccurred()) {
	    changePanel(MERCHANT_PANEL);
	}
    }

    private JPanel buildMenuPanel() {
	return new MenuPanel(playerController, imgLoad, size, this);
    }

    private JPanel newGamePanel() {
	return new NewGamePanel(this.playerController, mainPanel, this);
    }

    void changePanel(String name) {
	cLayout.show(mainPanel, name);
	subPanels.get(name).requestFocusInWindow();
    }

}
