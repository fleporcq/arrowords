package controllers;

import models.Grid;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(10, 10);

        // Génération pseudo aléatoire des cases noires
        grid.generatePseudoRandomBlackCells(20, 2, 3, 4);

        render(grid);
    }
}