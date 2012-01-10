package controllers;

import models.Dictionary;
import models.Grid;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(10, 10, 2);

        // Génération pseudo aléatoire des cases noires
        grid.generatePseudoRandomBlackCells(20, 3, 4);
        // Création des mots vide
        grid.createGridWords();
        // Résolution de la grille
        Dictionary dictionary = new Dictionary("fr.txt");
        grid.solve(dictionary);
        // Vérification de la solution
        grid.checkSolution();

        // Affichage de la grille
        render(grid);
    }
}
