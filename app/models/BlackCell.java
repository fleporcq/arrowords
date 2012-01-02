package models;

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

    public int countBlackCellOnLeft() {
        Cell cell = this.getCellOnLeft();

        if (cell instanceof BlackCell) {
            BlackCell blackCell = (BlackCell) cell;
            return 1 + blackCell.countBlackCellOnLeft();
        }
        return 0;
    }

    public int countBlackCellOnRight() {
        Cell cell = this.getCellOnRight();

        if (cell instanceof BlackCell) {
            BlackCell blackCell = (BlackCell) cell;
            return 1 + blackCell.countBlackCellOnRight();
        }
        return 0;
    }

    public int countBlackCellOnUp() {
        Cell cell = this.getCellOnUp();

        if (cell instanceof BlackCell) {
            BlackCell blackCell = (BlackCell) cell;
            return 1 + blackCell.countBlackCellOnUp();
        }
        return 0;
    }

    public int countBlackCellOnDown() {
        Cell cell = this.getCellOnDown();

        if (cell instanceof BlackCell) {
            BlackCell blackCell = (BlackCell) cell;
            return 1 + blackCell.countBlackCellOnDown();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "[BlackCell] " + super.toString();
    }
}
