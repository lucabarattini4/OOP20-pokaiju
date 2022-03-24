package model.gameitem;

import java.util.Objects;

import model.monster.Monster;

public abstract class AbstractGameItem implements GameItems {
    private String nameItem;
    private int quantity;
    private String description;
    private GameItemTypes type;

    public AbstractGameItem(String nameItem, int quantity, String description, GameItemTypes type) {
	super();
	this.nameItem = nameItem;
	this.quantity = quantity;
	this.description = description;
	this.type = type;
    }

    public String getNameItem() {
	return nameItem;
    }

    public int getNumber() {
	return quantity;
    }

    public void setNumber(int number) {
	this.quantity = number;
    }

    public String getDescription() {
	return description;
    }

    public GameItemTypes getType() {
	return type;
    }

    public abstract boolean use(Monster m);

    @Override
    public String toString() {
	return "nameItem=" + nameItem + ", quantity=" + quantity + ", description=" + description + ", type=" + type;
    }

    @Override
    public int hashCode() {
	return Objects.hash(nameItem, type);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AbstractGameItem other = (AbstractGameItem) obj;
	return Objects.equals(nameItem, other.nameItem) && type == other.type;
    }

}