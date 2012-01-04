package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import enums.Direction;

public class BlackCell extends Cell {

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

    public boolean isExceedingMaxConnectedBlacCells(int max) {
        if (max < 1) {
            return false;
        }
        return this.isExceedingMaxConnectedBlacCells(max, null, null);
    }

    private boolean isExceedingMaxConnectedBlacCells(int max, BlackCell previousBlackCell, List<BlackCell> connectedBlackCells) {
        if (previousBlackCell == null) {
            connectedBlackCells = new ArrayList<BlackCell>();
        }

        if (connectedBlackCells.size() > max - 1) {
            return true;
        }

        Iterator<Entry<Direction, Cell>> iterator = this.getConnexions().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry pair = iterator.next();
            Cell connexion = (Cell) pair.getValue();

            if (connexion instanceof BlackCell && connexion != previousBlackCell) {
                connectedBlackCells.add((BlackCell) connexion);
                if (((BlackCell) connexion).isExceedingMaxConnectedBlacCells(max, this, connectedBlackCells)) {
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

    private boolean checkWordsLength(int minLength) {
        boolean correctLength = true;

        int whiteCellsOnUp = this.countWhiteCellsOn(Direction.UP);
        int whiteCellsOnRight = this.countWhiteCellsOn(Direction.RIGHT);
        int whiteCellsOnDown = this.countWhiteCellsOn(Direction.DOWN);
        int whiteCellsOnLeft = this.countWhiteCellsOn(Direction.LEFT);

        // on test si le nombre de cases blanches dans chaque axe (haut,droit,bas,gauche) est égaleà 0 ou 1 ou égale ou supérieur au minimum autorisé
        if (whiteCellsOnUp > 1 && whiteCellsOnUp < minLength) {
            correctLength = false;
        }
        if (whiteCellsOnRight > 1 && whiteCellsOnRight < minLength) {
            correctLength = false;
        }
        if (whiteCellsOnDown > 1 && whiteCellsOnDown < minLength) {
            correctLength = false;
        }
        if (whiteCellsOnLeft > 1 && whiteCellsOnLeft < minLength) {
            correctLength = false;
        }

        return correctLength;
    }

    private boolean canHaveAtLeastOneWord(int minLength) {
        boolean canHaveAtLeastOneWord = true;

        int whiteCellsOnDown = this.countWhiteCellsOn(Direction.DOWN);
        int whiteCellsOnRight = this.countWhiteCellsOn(Direction.RIGHT);

        // Si c'est une case du bord gauche
        if (this.getCellOn(Direction.LEFT) == null) {
            // si la case ne peut pas avoir de mot à droite
            if (whiteCellsOnRight < minLength) {
                // et si la case ne peut pas avoir de mot décalé
                Cell nextCellOnDown = this.getCellOn(Direction.DOWN);
                if (nextCellOnDown == null || nextCellOnDown instanceof BlackCell) {
                    canHaveAtLeastOneWord = false;
                }
                if (nextCellOnDown instanceof WhiteCell && nextCellOnDown.countWhiteCellsOn(Direction.RIGHT) < minLength) {
                    canHaveAtLeastOneWord = false;
                }
            }
        }
        // Si c'est une case du bord haut
        else if (this.getCellOn(Direction.UP) == null) {

            // si la case ne peut pas avoir de mot en dessous
            if (whiteCellsOnDown < minLength) {
                // et si la case ne peut pas avoir de mot décalé
                Cell nextCellOnRight = this.getCellOn(Direction.RIGHT);
                if (nextCellOnRight == null || nextCellOnRight instanceof BlackCell) {
                    canHaveAtLeastOneWord = false;
                }
                else if (nextCellOnRight instanceof WhiteCell && nextCellOnRight.countWhiteCellsOn(Direction.DOWN) < minLength) {
                    canHaveAtLeastOneWord = false;
                }
            }
        }
        // Si c'est une case interne et qu'elle ne peut pas avoir de mot ni à droite ni à gauche
        else if (whiteCellsOnRight < minLength && whiteCellsOnDown < minLength) {
            canHaveAtLeastOneWord = false;
        }
        return canHaveAtLeastOneWord;
    }

    public boolean isCreatingIncorrectWordLength(int minLength) {
        // On test si la case courante créé un mot de taille incorrecte
        if (!this.checkWordsLength(minLength) || !this.canHaveAtLeastOneWord(minLength)) {
            return true;
        }

        // On test si les n(minLength) cases au desssus créé un mot de taille incorrecte
        Cell nextCellOnUp = this.getCellOn(Direction.UP);
        for (int i = 0; i < minLength; i++) {
            if (nextCellOnUp == null) {
                break;
            }
            else if (nextCellOnUp instanceof BlackCell && (!((BlackCell) nextCellOnUp).checkWordsLength(minLength) || !((BlackCell) nextCellOnUp).canHaveAtLeastOneWord(minLength))) {
                return true;
            }
            nextCellOnUp = nextCellOnUp.getCellOn(Direction.UP);
        }

        // On test si les n(minLength) cases à gauche créé un mot de taille incorrecte
        Cell nextCellOnLeft = this.getCellOn(Direction.LEFT);
        for (int i = 0; i < minLength; i++) {
            if (nextCellOnLeft == null) {
                break;
            }
            else if (nextCellOnLeft instanceof BlackCell && (!((BlackCell) nextCellOnLeft).checkWordsLength(minLength) || !((BlackCell) nextCellOnLeft).canHaveAtLeastOneWord(minLength))) {

                return true;
            }
            nextCellOnLeft = nextCellOnLeft.getCellOn(Direction.LEFT);
        }

        return false;
    }

    @Override
    public String toString() {
        return "[BlackCell] " + super.toString();
    }
}
