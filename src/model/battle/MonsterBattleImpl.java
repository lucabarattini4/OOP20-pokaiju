package model.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.GameItem.GameItems;
import model.monster.Monster;
import model.monster.MonsterStats;
import model.monster.MonsterType;
import model.npc.NpcImpl;
import model.player.PlayerImpl;

public class MonsterBattleImpl implements MonsterBattle {
    private final static int EXP_MULTIPLER = 5;
    private final static int ESCAPE_RANGE = 10;
    private final static int ESCAPE_DIFFICULT = 5;
    private final static int CAPTURE_RANGE = 10;
    private final static int CAPTURE_DIFFICULT = 3;
    private final static int MONEY_WON = 70;
    private final static int MONEY_LOST = 50;

    private boolean battleStatus; // true if the battle enemy/player team is defeat, false otherwise
    private boolean areEndPP;
    private Monster playerCurrentMonster;
    private Monster enemy;
    private List<Monster> playerTeam;
    private List<Monster> enemyTeam;
    private PlayerImpl trainer;
    private Optional<NpcImpl> enemyTrainer;
    private Moves extraMoves;

    private MonsterBattleImpl(PlayerImpl trainer, List<Monster> enemyTeam) {
	this.trainer = trainer;
	this.battleStatus = true;
	this.enemyTrainer = null;
	this.playerTeam = trainer.allMonster();
	this.playerCurrentMonster = playerTeam.get(0);
	this.enemyTeam = new ArrayList<>(enemyTeam);
	this.enemy = enemyTeam.get(0);
	this.extraMoves = new MovesImpl("Testata", 30, MonsterType.NONE, 999);
	this.areEndPP = true;
    }

    public MonsterBattleImpl(PlayerImpl trainer, NpcImpl enemyTrainer) {
	this(trainer, enemyTrainer.allMonster());
	this.enemyTrainer = Optional.of(enemyTrainer);

    }

    public MonsterBattleImpl(PlayerImpl trainer, Monster wildMonster) {
	this(trainer, List.of(wildMonster));
    }

    @Override
    public Moves enemyAttack() {
	int x = (int) (Math.random() * this.enemy.getNumberOfMoves());
	while (!enemy.getMoves(x).checkPP()) {
	    x = (x + 1) % this.enemy.getNumberOfMoves();
	}
	;
	return enemy.getMoves(x);
    }

    @Override
    public boolean capture() {
	throwExceptionIfItIsOver();
	if (!enemy.getWild()) {
	    return false;
	}

	int attempt = (int) (Math.random() * CAPTURE_RANGE);
	if (attempt <= CAPTURE_DIFFICULT) {
	    // System.out.println(enemy.getName() + " è stato catturato");
	    int expReached = enemy.getLevel() * EXP_MULTIPLER;
	    playerCurrentMonster.incExp(expReached);
	    this.battleStatus = false;
	    return true;
	}
	// System.out.println("cattura fallita");
	return false;

    }

    @Override
    public boolean escape() {
	throwExceptionIfItIsOver();
	if (!enemy.getWild()) {
	    // System.out.println("Non puoi scappare");
	    return false;
	}
	int attempt = (int) (Math.random() * ESCAPE_RANGE);
	if (attempt <= ESCAPE_DIFFICULT) {
	    // System.out.println("Sei fuggito");
	    this.battleStatus = false;
	    return true;
	}
	// System.out.println("Fuga fallita");
	return false;

    }

    @Override
    public boolean playerChangeMonster(int index) {
	throwExceptionIfItIsOver();
	if (playerTeam.get(index) == playerCurrentMonster) {
	    System.out.println("Il mostro è già in campo");
	    return false;
	}
	if (playerTeam.get(index).isAlive()) {
	    playerCurrentMonster = playerTeam.get(index);
	    System.out.println("Cambio");
	    return true;
	}
	System.out.println("Il mostro selezionato è morto");
	return false;

    }

    @Override
    public boolean movesSelection(int moveIndex) {
	for (int c = 0; c < this.playerCurrentMonster.getNumberOfMoves(); c++) {
	    if (this.playerCurrentMonster.getMoves(c).checkPP()) {
		this.areEndPP = false;
	    }
	}
	if (this.areEndPP) {
	    this.turn(extraMoves);
	    return true;
	}
	if (this.playerCurrentMonster.getMoves(moveIndex).checkPP() && this.battleStatus
		&& this.playerCurrentMonster.isAlive()) {
	    this.playerCurrentMonster.getMoves(moveIndex).decPP();
	    this.turn(this.playerCurrentMonster.getMoves(moveIndex));
	    return true;
	}
	throwExceptionIfItIsOver();
	// System.out.println(playerCurrentMonster.getName() + " è troppo stanco per
	// usare questa mossa");
	return false;
    }

    private void turn(Moves monsterMove) {
	MonsterStats playerStats = this.playerCurrentMonster.getStats();
	MonsterStats enemyStats = this.enemy.getStats();
	int damage = monsterMove.getDamage(enemy.getType()) + playerStats.getAttack() - enemyStats.getDefense();

	if (playerStats.getSpeed() < enemyStats.getSpeed()) {
	    this.enemyTurn(playerStats, enemyStats);
	    if (allPlayerMonsterDeafeted()) { // player's team defeated
		this.battleStatus = false;
		this.trainer.setMoney(trainer.getMoney() - MONEY_LOST);
	    } else {
		enemy.setHealth(enemy.getHealth() - damage);
		if (!enemy.isAlive()) {

		    playerCurrentMonster.incExp(enemy.getLevel() * EXP_MULTIPLER);
		    // System.out.println(enemy.getName() + " è morto "); //enemy's team defeated
		    if (!areThereEnemies()) {
			// ending battle
			trainer.setMoney(trainer.getMoney() + MONEY_WON);
			if (this.enemyTrainer.isPresent()) {
			    enemyTrainer.get().isDefeated();
			}
			this.battleStatus = false;
		    } else {
			this.enemy = enemyTeam.stream().filter(m -> m.isAlive()).findAny().get(); // change enemy's
												  // monster
		    }
		}
	    }

	} else {
	    enemy.setHealth(enemy.getHealth() - damage);
	    // System.out.println(playerCurrentMonster.getName() + " usa " + att.getName() +
	    // " infliggendo "
	    // + att.getDamage(enemy.getType()) + " danni");

	    if (enemy.isAlive()) {

		this.enemyTurn(playerStats, enemyStats);
		if (allPlayerMonsterDeafeted()) { // player's team defeated
		    this.battleStatus = false;
		    this.trainer.setMoney(trainer.getMoney() - MONEY_LOST);
		}
	    } else {

		playerCurrentMonster.incExp(enemy.getLevel() * EXP_MULTIPLER);
		// System.out.println(enemy.getName() + " è morto "); //enemy's team defeated
		if (!areThereEnemies()) {
		    // ending battle
		    trainer.setMoney(trainer.getMoney() + MONEY_WON);
		    if (this.enemyTrainer.isPresent()) {
			enemyTrainer.get().isDefeated();
		    }
		    this.battleStatus = false;
		} else {
		    this.enemy = enemyTeam.stream().filter(m -> m.isAlive()).findAny().get(); // change enemy's monster
		}

	    }
	}
    }

    private void enemyTurn(MonsterStats playerStats, MonsterStats enemyStats) {
	Moves att = this.enemyAttack();
	int damage = att.getDamage(playerCurrentMonster.getType()) + enemyStats.getAttack() - playerStats.getDefense();
	att.decPP();
	playerCurrentMonster.setHealth(playerCurrentMonster.getHealth() - damage);
	System.out.println(enemy.getName() + " usa " + att.getName() + " infliggendo "
		+ att.getDamage(playerCurrentMonster.getType()) + " danni");
    }

    private boolean areThereEnemies() {

	return enemyTeam.stream().filter(m -> m.isAlive()).count() > 0;
    }

    @Override
    public boolean isCurrentMonsterAlive() {
	return this.playerCurrentMonster.isAlive();
    }

    private boolean allPlayerMonsterDeafeted() {
	return this.playerTeam.stream().filter(m -> m.isAlive()).count() == 0;

    }

    @Override
    public boolean isOver() {
	// TODO Auto-generated method stub
	return false;
    }

    private void throwExceptionIfItIsOver() {
	if (!this.battleStatus) {
	    throw new IllegalStateException();
	}
    }

    @Override
    public boolean useItem(GameItems item) {
	return item.use(playerCurrentMonster);
    }
}
