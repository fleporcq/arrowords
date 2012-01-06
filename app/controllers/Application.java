package controllers;

import java.util.List;

import models.Dictionary;
import models.Grid;
import models.GridWord;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(10, 10, 2);

        // Génération pseudo aléatoire des cases noires
        grid.generatePseudoRandomBlackCells(20, 3, 4);
        List<GridWord> words = grid.createGridWords();

        Dictionary dictionary = new Dictionary("fr.txt");
        grid.solve(dictionary);

        render(grid);
    }
}
