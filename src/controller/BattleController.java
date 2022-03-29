package controller;

import java.util.List;

import model.battle.Moves;
import model.gameitem.GameItems;
import model.monster.Monster;

public interface BattleController {
    /**
     * Choose the player move in the battle and starts the turn
     * 
     * @return true if you can choose the selected move
     */
    boolean chooseMove(int moveIndex);

    /**
     * Use an item
     * 
     * @param gameItem
     */
    void useItem(GameItems gameItem);

    /**
     * 
     * @return all player items
     */
    List<GameItems> getAllPlayerItems();

    /**
     * Change monster
     * 
     * @param monsterIndex
     */
    void changeMonster(int monsterIndex);

    /**
     * Get monster's moves
     * 
     * @return current player's monster moves
     */
    List<Moves> getMoves();

    /**
     * Get current player's monster
     * 
     * @return current player's monster
     */
    Monster getCurrentPlayerMonster();

    /**
     * 
     * @return current monster name
     */
    String getCurrentPlayerMonsterName();

    /**
     * 
     * @return current monster health
     */
    int getCurrentPlayerMonsterHp();

    /**
     * 
     * @return current monster level
     */
    int getCurrentPlayerMonsterLevel();

    /**
     * Get all player's monsters
     * 
     * @return player team
     */
    List<Monster> getPlayerTeam();

    /**
     * Get current enemy's monster
     * 
     * @return current enemy's monster
     */
    Monster getCurrentEnemyMonster();

    /**
     * 
     * @return current enemy monster name
     */
    String getCurrentEnemyMonsterName();

    /**
     * 
     * @return current enemy monster health
     */
    int getCurrentEnemyMonsterHp();

    /**
     * 
     * @return current enemy monster level
     */
    int getCurrentEnemyMonsterLevel();

    /**
     * 
     * @return current enemy move
     */
    Moves getCurrentEnemyMove();

    /**
     * Get all enemy's monsters
     * 
     * @return enemy team
     */
    List<Monster> getEnemyTeam();

    /**
     * The player tries to escape the battle
     * 
     * @return if fleeing is successful, false otherwise
     */
    boolean flee();

    /**
     * Try to catch the monster
     * 
     * @return true if captured, false otherwise
     */
    boolean capture();

    /**
     * 
     * @return if the battle ends
     */
    boolean isOver();
}
