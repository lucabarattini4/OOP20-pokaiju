package model.battle;

import model.gameitem.GameItems;

public interface MonsterBattle {
	
	Moves enemyAttack();

	boolean escape();

	boolean capture();

	boolean isOver();

	boolean playerChangeMonster(int index);

	boolean movesSelection(int moveIndex);

	boolean isCurrentMonsterAlive();

	boolean useItem(GameItems item);
	
}
