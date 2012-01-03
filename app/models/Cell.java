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

    public void addConnexion(Cell cell) {
        Direction direction = this.getDirection(cell);
        if (direction != null) {
            this.connexions.put(direction, cell);
        }
    }

    public void setConnectedCells(Grid grid) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 || y != 0) {
                    Cell possibleConnexion = grid.get(this.getX() + x, this.getY() + y);
                    if (possibleConnexion != null) {
                        this.addConnexion(possibleConnexion);
                        possibleConnexion.addConnexion(this);
                    }
                }
            }
        }

    }

    public Direction getDirection(Cell cell) {
        int testX = this.x - cell.x;
        int testY = this.y - cell.y;
        Direction direction = null;

        if (testX == 1 && testY == 1) {
            direction = Direction.UPLEFT;
        }
        else if (testX == 0 && testY == 1) {
            direction = Direction.UP;
        }
        else if (testX == -1 && testY == 1) {
            direction = Direction.UPRIGHT;
        }
        else if (testX == 1 && testY == 0) {
            direction = Direction.LEFT;
        }
        else if (testX == -1 && testY == 0) {
            direction = Direction.RIGHT;
        }
        else if (testX == 1 && testY == -1) {
            direction = Direction.DOWNLEFT;
        }
        else if (testX == 0 && testY == -1) {
            direction = Direction.DOWN;
        }
        else if (testX == -1 && testY == -1) {
            direction = Direction.DOWNRIGHT;
        }

        return direction;
    }

    public Cell getCellOn(Direction direction) {
        return this.connexions.get(direction);
    }

    public boolean isBorderCell() {
        for (Direction direction : Direction.values()) {
            Cell connexion = this.getCellOn(direction);
            if (connexion == null) {
                return true;
            }
        }
        return false;
    }

    public int countBlackCellsOn(Direction direction) {
        Cell cell = this.getCellOn(direction);

        if (cell instanceof BlackCell) {
            BlackCell blackCell = (BlackCell) cell;
            return 1 + blackCell.countBlackCellsOn(direction);
        }
        return 0;
    }

    public int countWhiteCellsOn(Direction direction) {
        Cell cell = this.getCellOn(direction);

        if (cell instanceof WhiteCell) {
            WhiteCell whiteCell = (WhiteCell) cell;
            return 1 + whiteCell.countWhiteCellsOn(direction);
        }
        return 0;
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
