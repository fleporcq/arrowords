package controllers;

import models.Grid;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(15, 15);

        // Génération pseudo aléatoire des cases noires

        grid.generatePseudoRandomBlackCells(20, 2, 3);
        // grid.generateRandomBlackCells(20, 2, 3);

        render(grid);
    }
}