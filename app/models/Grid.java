package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import play.Logger;
import enums.Direction;

public class Grid extends ArrayList<Cell> {

    private int width;

    private int height;

    private int blackCellsCount;

    public Grid(int width, int height) {

        this.width = width;
        this.height = height;

        // Calcul de la capacité
        int capacity = this.width * this.height;

        // Création d'une grille vide
        List<Cell> whiteCells = Collections.nCopies(capacity, null);
        this.addAll(whiteCells);

        // Ajout des cases blanches
        for (int i = 0; i < capacity; i++) {
            WhiteCell whiteCell = new WhiteCell();
            whiteCell.setX(this.getXForIndex(i));
            whiteCell.setY(this.getYForIndex(i));
            this.addCell(whiteCell);
        }

    }

    public Cell get(int x, int y) {
        int index;
        Cell cell = null;
        try {
            index = this.getIndexForPosition(x, y);
            cell = this.get(index);
        }
        catch (IndexOutOfBoundsException e) {

        }
        return cell;
    }

    public WhiteCell addWhiteCell(int x, int y) {
        WhiteCell whiteCell = new WhiteCell();
        whiteCell.setX(x);
        whiteCell.setY(y);
        if (this.addCell(whiteCell)) {
            return whiteCell;
        }
        return null;
    }

    public BlackCell addBlackCell(int x, int y) {
        BlackCell blackCell = new BlackCell();
        blackCell.setX(x);
        blackCell.setY(y);

        if (this.addCell(blackCell)) {
            this.blackCellsCount++;
            return blackCell;
        }
        return null;

    }

    public boolean addCell(Cell cell) {
        boolean added = false;
        int x = cell.getX();
        int y = cell.getY();
        if (x < 1 || x > this.width) {
            Logger.error("x[%s] n'est pas dans la grille ", x);
        }
        else if (y < 1 || y > this.height) {
            Logger.error("y[%s] n'est pas dans la grille ", y);
        }
        else {
            this.setConnectedCells(cell);
            int index = this.getIndexForPosition(x, y);
            this.set(index, cell);
            added = true;

        }
        return added;
    }

    private void setConnectedCells(Cell cell) {
        // Ajoute les cases connexes à la case
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x != 0 || y != 0) {
                    Cell possibleConnexion = this.get(cell.getX() + x, cell.getY() + y);
                    if (possibleConnexion != null) {
                        cell.addConnexion(possibleConnexion);
                        possibleConnexion.addConnexion(cell);
                    }
                }
            }
        }

    }

    public boolean isCyclic(BlackCell blackCell, BlackCell previousBlackCell, List<BlackCell> checkedBlackCells) {

        boolean cycle = false;
        if (previousBlackCell == null) {
            checkedBlackCells = new ArrayList<BlackCell>();
        }

        if (checkedBlackCells.contains(blackCell)) {
            cycle = true;
        }
        else {
            checkedBlackCells.add(blackCell);
            Iterator<Entry<Direction, Cell>> iterator = blackCell.getConnexions().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry pair = iterator.next();
                Cell connexion = (Cell) pair.getValue();

                if (connexion instanceof BlackCell && connexion != previousBlackCell) {

                    cycle = this.isCyclic((BlackCell) connexion, blackCell, checkedBlackCells);
                    if (cycle) {
                        break;
                    }
                }
            }
        }
        return cycle;
    }

    public boolean isMaxAlign(BlackCell blackCell, int maxAlign) {

        int horizontalCount = blackCell.countBlackCellOnLeft() + blackCell.countBlackCellOnRight();
        int verticalCount = blackCell.countBlackCellOnUp() + blackCell.countBlackCellOnDown();

        if (horizontalCount >= maxAlign || verticalCount >= maxAlign) {
            return true;
        }

        return false;
    }

    private void removeBlackCell(BlackCell blackCell) {
        this.removeBlackCell(blackCell.getX(), blackCell.getY());
    }

    public void removeBlackCell(int x, int y) {
        int index = this.getIndexForPosition(x, y);

        Object cell = this.get(index);
        if (cell instanceof BlackCell) {

            BlackCell blackCell = (BlackCell) cell;

            // On retire la case noire de toutes les connexions
            Iterator<Entry<Direction, Cell>> iterator = blackCell.getConnexions().entrySet().iterator();
            while (iterator.hasNext()) {
                Entry pair = iterator.next();
                Cell connexion = (Cell) pair.getValue();
                connexion.removeConnexion(blackCell);
            }

            this.set(index, null);
            this.blackCellsCount--;
        }
    }

    public void generateRandomBlackCells(int percent, int maxAlign) {
        // Création de la case noire en haut à gauche
        this.addBlackCell(1, 1);

        // Création de cases noires 1 fois sur 2 sur la première ligne
        // horizontale
        for (int i = 3; i <= this.width; i += 2) {
            this.addBlackCell(i, 1);
        }

        // Création de cases noires 1 fois sur 2 sur la première ligne verticale
        for (int i = 3; i <= this.height; i += 2) {
            this.addBlackCell(1, i);
        }

        // TODO: voir pour légérement faire varier le pourcentage aléatoirement
        // Génération aleatoire des cases restantes
        while (this.blackCellsCount < percent * this.size() / 100) {

            Random randomX = new Random();
            Random randomY = new Random();
            int x = 2 + randomX.nextInt(this.width + 1 - 2);
            int y = 2 + randomY.nextInt(this.height + 1 - 2);

            // si la position peut être occupée
            if (this.isPossibleBlackCellPosition(x, y)) {

                BlackCell blackCell = this.addBlackCell(x, y);

                // Si l'ajout de la case provoque un cycle on la retire
                if (this.isCyclic(blackCell, null, null)) {
                    this.removeBlackCell(blackCell);
                }
                // Si l'ajout de la case provoque un alignement supérieur au max autorisé on la retire
                else if (maxAlign > 0 && this.isMaxAlign(blackCell, maxAlign)) {
                    this.removeBlackCell(blackCell);
                }
            }

        }

    }

    private boolean isPossibleBlackCellPosition(int x, int y) {
        boolean possiblePosition = true;

        // Si la case est déjà occupé
        if (this.get(x, y) instanceof BlackCell) {
            possiblePosition = false;
        }

        // Si on est sur la 2ème ligne et que x est pair
        if (y == 2 && x % 2 == 0) {
            possiblePosition = false;
        }
        // Si on est sur la 2ème colonne et que y est pair
        if (x == 2 && y % 2 == 0) {
            possiblePosition = false;
        }

        // Si on est sur la dernière case de la deuxième colonne
        if (x == 2 && y == this.height) {
            possiblePosition = false;
        }

        // Si on est sur la dernière case de la deuxième ligne
        if (y == 2 && x == this.width) {
            possiblePosition = false;
        }

        // Si la case n'est pas une des 4 dernières de la grille ( carré en bas
        // à droite)
        if (x == this.width && y == this.height) {
            possiblePosition = false;
        }
        if (x == this.width - 1 && y == this.height) {
            possiblePosition = false;
        }
        if (x == this.width && y == this.height - 1) {
            possiblePosition = false;
        }
        if (x == this.width - 1 && y == this.height - 1) {
            possiblePosition = false;
        }

        return possiblePosition;
    }

    public int countBlackCells() {
        return this.blackCellsCount;
    }

    private int getIndexForPosition(int x, int y) {
        return (y * this.width) - (this.width - x) - 1;
    }

    public int getYForIndex(int index) {
        return (index / this.width) + 1;
    }

    public int getXForIndex(int index) {
        return (index % this.width) + 1;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
