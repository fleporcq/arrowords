package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import play.Logger;

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
            this.setWhiteCell(this.getXForIndex(i), this.getYForIndex(i));
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

    public WhiteCell setWhiteCell(int x, int y) {
        WhiteCell whiteCell = new WhiteCell();
        whiteCell.setX(x);
        whiteCell.setY(y);
        return (WhiteCell) this.setCell(whiteCell);
    }

    public BlackCell setBlackCell(int x, int y) {
        BlackCell blackCell = new BlackCell();
        blackCell.setX(x);
        blackCell.setY(y);
        return (BlackCell) this.setCell(blackCell);

    }

    public Cell setCell(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        if (x < 1 || x > this.width) {
            Logger.error("x[%s] n'est pas dans la grille ", x);
        }
        else if (y < 1 || y > this.height) {
            Logger.error("y[%s] n'est pas dans la grille ", y);
        }
        else {
            cell.setConnectedCells(this);
            int index = this.getIndexForPosition(x, y);
            this.set(index, cell);
            if (cell instanceof BlackCell) {
                this.blackCellsCount++;
            }

            return cell;
        }
        return null;
    }

    private void resetCell(Cell cell) {

        this.setWhiteCell(cell.getX(), cell.getY());
        // Logger.debug("Reset cell : %s", cell);
        if (cell instanceof BlackCell) {
            this.blackCellsCount--;
        }
    }

    public void generatePseudoRandomBlackCells(int percent, int wordMinLength, int maxAlign, int maxConnectedBlackCells) {

        // Création de cases noires 1 fois sur 2 sur la première ligne
        // horizontale
        for (int i = 3; i <= this.width; i += 2) {
            this.setBlackCell(i, 1);
        }

        // Création de cases noires 1 fois sur 2 sur la première ligne verticale
        for (int i = 3; i <= this.height; i += 2) {
            this.setBlackCell(1, i);
        }

        this.generateRandomBlackCells(percent, wordMinLength, maxAlign, maxConnectedBlackCells);

    }

    public void generateRandomBlackCells(int percent, int wordMinLength, int maxAlign, int maxConnectedBlackCells) {

        // Création de la case noire en haut à gauche
        this.setBlackCell(1, 1);

        // TODO: voir pour légérement faire varier le pourcentage aléatoirement
        // Génération aleatoire des cases restantes tant qu'on a pas atteint le pourcentage requis
        while (this.blackCellsCount < percent * this.size() / 100) {

            Random randomX = new Random();
            Random randomY = new Random();
            int x = 1 + randomX.nextInt(this.width);
            int y = 1 + randomY.nextInt(this.height);

            // si la position peut être occupée
            if (this.isPossibleBlackCellPosition(x, y)) {

                BlackCell blackCell = this.setBlackCell(x, y);
                // Si l'ajout de la case créé des mots de taille incorrecte
                if (blackCell.isCreatingIncorrectWordLength(wordMinLength)) {
                    Logger.debug("Create orphan black cells %s", blackCell);
                    this.resetCell(blackCell);
                }
                // Si l'ajout de la case provoque un cycle on la retire
                else if (blackCell.isCreatingCycle()) {
                    Logger.debug("Cycle detected %s", blackCell);
                    this.resetCell(blackCell);
                }
                // Si l'ajout de la case provoque un faux cycle en bord de grille on la retire
                else if (blackCell.isCreatingBorderCycle()) {
                    Logger.debug("Border cycle detected %s", blackCell);
                    this.resetCell(blackCell);
                }
                // Si l'ajout de la case provoque une connexion de cases noires supérieur au max autorisé on la retire
                else if (blackCell.isExceedingMaxConnectedBlacCells(maxConnectedBlackCells)) {
                    Logger.debug("Maximum connected black cells exceeded %s", blackCell);
                    this.resetCell(blackCell);
                }
                // Si l'ajout de la case provoque un alignement supérieur au max autorisé on la retire
                else if (blackCell.isExceedingMaxAlign(maxAlign)) {
                    Logger.debug("Maximum alignment exceeded %s", blackCell);
                    this.resetCell(blackCell);
                }

            }

        }

    }

    private boolean isPossibleBlackCellPosition(int x, int y) {
        Cell cell = this.get(x, y);
        // Si la case est déjà occupé
        if (cell instanceof WhiteCell) {
            return true;
        }
        return false;
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
