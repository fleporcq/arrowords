package models;

import java.util.HashMap;
import java.util.Map;

import enums.Direction;

public abstract class Cell {

	private int x;

	private int y;

	private Map<Direction, Cell> connexions;

	public Cell() {
		super();
		this.connexions = new HashMap<Direction, Cell>();
	}

	public Map<Direction, Cell> getConnexions() {
		return this.connexions;
	}

	public void removeConnexion(Cell cell) {
		Direction direction = cell.getDirection(cell);
		if (direction != null) {
			this.connexions.remove(direction);
		}
	}

	public void addConnexion(Cell cell) {
		Direction direction = this.getDirection(cell);
		if (direction != null) {
			this.connexions.put(direction, cell);
		}
	}

	public Direction getDirection(Cell cell) {
		int testX = this.x - cell.x;
		int testY = this.y - cell.y;
		Direction direction = null;

		if (testX == 1 && testY == 1) {
			direction = Direction.UPLEFT;
		} //
		else if (testX == 0 && testY == 1) {
			direction = Direction.UP;
		} //
		else if (testX == -1 && testY == 1) {
			direction = Direction.UPRIGHT;
		} //
		else if (testX == 1 && testY == 0) {
			direction = Direction.LEFT;
		} //
		else if (testX == -1 && testY == 0) {
			direction = Direction.RIGHT;
		} //
		else if (testX == 1 && testY == -1) {
			direction = Direction.DOWNLEFT;
		} //
		else if (testX == 0 && testY == -1) {
			direction = Direction.DOWN;
		} //
		else if (testX == -1 && testY == -1) {
			direction = Direction.DOWNRIGHT;
		}

		return direction;
	}

	public Cell getCellOnUpLeft() {
		return this.connexions.get(Direction.UPLEFT);
	}

	public Cell getCellOnUp() {
		return this.connexions.get(Direction.UP);
	}

	public Cell getCellOnUpRight() {
		return this.connexions.get(Direction.UPRIGHT);
	}

	public Cell getCellOnLeft() {
		return this.connexions.get(Direction.LEFT);
	}

	public Cell getCellOnRight() {
		return this.connexions.get(Direction.RIGHT);
	}

	public Cell getCellOnDownLeft() {
		return this.connexions.get(Direction.DOWNLEFT);
	}

	public Cell getCellOnDown() {
		return this.connexions.get(Direction.DOWN);
	}

	public Cell getCellOnDownRight() {
		return this.connexions.get(Direction.DOWNRIGHT);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

}
