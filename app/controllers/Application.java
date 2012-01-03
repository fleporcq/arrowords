package controllers;

import models.Grid;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(15, 11);

        // Génération pseudo aléatoire des cases noires
        grid.generatePseudoRandomBlackCells(20, 2);
        // grid.generateRandomBlackCells(20, 2);

        render(grid);
    }
}