package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import enums.Direction;

public class BlackCell extends Cell {

    private String firstDefinition;

    private String secondDefinition;

    public String getFirstDefinition() {
        return this.firstDefinition;
    }

    public void setFirstDefinition(String firstDefinition) {
        this.firstDefinition = firstDefinition;
    }

    public String getSecondDefinition() {
        return this.secondDefinition;
    }

    public void setSecondDefinition(String secondDefinition) {
        this.secondDefinition = secondDefinition;
    }

    public boolean isExceedingMaxAlign(int maxAlign) {
        if (maxAlign < 1) {
            return false;
        }
        int horizontalCount = this.countBlackCellsOn(Direction.LEFT) + this.countBlackCellsOn(Direction.RIGHT);
        int verticalCount = this.countBlackCellsOn(Direction.UP) + this.countBlackCellsOn(Direction.DOWN);

        if (horizontalCount >= maxAlign || verticalCount >= maxAlign) {
            return true;
        }

        return false;
    }

    public boolean isCreatingCycle() {
        return this.isCreatingCycle(null, null);
    }

    private boolean isCreatingCycle(BlackCell previousBlackCell, List<BlackCell> checkedBlackCells) {

        if (previousBlackCell == null) {
            checkedBlackCells = new ArrayList<BlackCell>();
        }

        if (checkedBlackCells.contains(this)) {
            return true;
        }

        checkedBlackCells.add(this);
        Iterator<Entry<Direction, Cell>> iterator = this.getConnexions().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry pair = iterator.next();
            Cell connexion = (Cell) pair.getValue();

            if (connexion instanceof BlackCell && connexion != previousBlackCell) {

                if (((BlackCell) connexion).isCreatingCycle(this, checkedBlackCells)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCreatingBorderCycle() {
        return this.isCreatingBorderCycle(null, null);
    }

    private boolean isCreatingBorderCycle(BlackCell previousBlackCell, List<BlackCell> borderBlackCells) {
        if (previousBlackCell == null) {
            borderBlackCells = new ArrayList<BlackCell>();
        }
        if (this.isBorderCell()) {
            borderBlackCells.add(this);
        }

        if (borderBlackCells.size() > 1) {
            return true;
        }

        Iterator<Entry<Direction, Cell>> iterator = this.getConnexions().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry pair = iterator.next();
            Cell connexion = (Cell) pair.getValue();

            if (connexion instanceof BlackCell && connexion != previousBlackCell) {

                if (((BlackCell) connexion).isCreatingBorderCycle(this, borderBlackCells)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "[BlackCell] " + super.toString();
    }
}
