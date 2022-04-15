package model.player;

import java.util.ArrayList;
import java.util.List;

import model.monster.Monster;

public class MonsterStorageImpl implements MonsterStorage {

	private static final int MAX_NUMBER_OF_BOX = 10;
	private static final int MAX_SIZE_OF_BOX = 10;
	private static final String INITIAL_BOX_NAME = "BOX";

	private List<MonsterBox> monsterBoxes;
	private int currentMonsterBoxIndex;
	private Player player;

	//TODO max monster fuori
	//TODO add monster
	//TODO Costruttore
	//TODO liste circolari
	
	public  MonsterStorageImpl(Player player, List<MonsterBox> boxes) {
		this.player = player;
		this.monsterBoxes = new ArrayList<>(boxes);
		this.currentMonsterBoxIndex = 1;
		if (monsterBoxes.size() > MAX_NUMBER_OF_BOX) {
			this.monsterBoxes = monsterBoxes.subList(0, MAX_NUMBER_OF_BOX);
		} else {
			generateBoxs(MAX_NUMBER_OF_BOX - monsterBoxes.size());
		}
	}
	
	public MonsterStorageImpl(Player player) {
		this.player = player;
		this.currentMonsterBoxIndex = 1;
		this.monsterBoxes = new ArrayList<>();
		generateBoxs(MAX_NUMBER_OF_BOX);
		
	}
	
	

	private void generateBoxs(int n) {
		MonsterBox box;
		
		for (int i = n; i < MAX_NUMBER_OF_BOX; i++) {
			box = new MonsterBoxImpl(INITIAL_BOX_NAME + i, MAX_SIZE_OF_BOX);
			this.monsterBoxes.add(box);
		}
	}
	private MonsterBox getFirstBoxFree(){
		for(int i = 1; i< MAX_NUMBER_OF_BOX; i++) {
			if(!this.monsterBoxes.get(i).isFull()) {
				return this.monsterBoxes.get(i);
			}
			
		}
		return null;
	}
	@Override
	public void addMonster(Monster monster) {
		if(!this.getCurrentBox().isFull()) {
			this.getCurrentBox().addMonster(monster);
		}
		else {	
			MonsterBox monsterBox = getFirstBoxFree();			
			if(monsterBox!=null) {
				monsterBox.addMonster(monster);
			}
		}
		
	}

	@Override
	public boolean depositMonster(Monster monster) {
		if (this.player.removeMonster(monster)) {
			getCurrentBox().addMonster(monster);
			return true;
		}
		return false;
	}

	@Override
	public boolean withdrawMonster(int monsterID) {
		if (isInBox(monsterID)) {
			getCurrentBox().removeMonster(monsterID);
			if (!this.player.isTeamFull()) {
				this.player.addMonster(getCurrentBox().getMonster(monsterID).get());
				return true;
			}
		}
		return false;
	}

	private boolean isInBox(int monsterID) {
		for (Monster monster : getCurrentBox().getAllMonsters()) {
			if (monster.getId() == monsterID) {
				return true;
			}
		}
		return false;
	}

	private MonsterBox getCurrentBox() {
		return this.monsterBoxes.get(currentMonsterBoxIndex);
	}

	@Override
	public boolean exchange(Monster monster, int monsterID) {

		if (this.player.getAllMonsters().contains(monster) && isInBox(monsterID)) {
			getCurrentBox().exchange(monster, monsterID);
		}

		return false;
	}

	@Override
	public void nextBox() {
		this.currentMonsterBoxIndex = (this.currentMonsterBoxIndex + 1)%MAX_SIZE_OF_BOX;

	}

	@Override
	public void previousBox() {
		if (this.currentMonsterBoxIndex > 1) {
			this.currentMonsterBoxIndex--;
		}
	}

	@Override
	public String getCurrentBoxName() {
		return getCurrentBox().getName();
	}

	@Override
	public List<Monster> getCurrentBoxMonsters() {
		return getCurrentBox().getAllMonsters();
	}
	
	public int getMaxSizeOfBox() {
		return MAX_SIZE_OF_BOX;
	}
	
	public int currentBoxSize() {
		return this.getCurrentBoxMonsters().size();
	}
}
