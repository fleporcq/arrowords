package controllers;

import models.Dictionary;
import models.Grid;
import play.mvc.Controller;
import solvers.CSPSolver;

public class Application extends Controller {

    public static void index() {

        // Création d'une grille
        Grid grid = new Grid(10, 10, 2);

        // Génération pseudo aléatoire des cases noires
        grid.generatePseudoRandomBlackCells(20, 3, 4);

        // Création des mots vide
        // grid.createGridWords();

        // Résolution de la grille
        // Dictionary dictionary = new Dictionary("fr.txt");
        // grid.solve(dictionary);

        // Solver solver = new Solver(grid, dictionary);
        // solver.start();
        // System.out.println(grid.getAsJson());
        render(grid);
        // Vérification de la solution
        // grid.checkSolution();

        // Affichage de la grille
        // render(grid);
    }

    public static void solve(String jsonGrid) {
        Grid grid = new Grid(jsonGrid);
        Dictionary dictionary = new Dictionary("fr.txt");
        CSPSolver solver = new CSPSolver(grid, dictionary);
        solver.run();
        render("Application/index.html", grid);
    }

    public static void stop(Long id) {
        CSPSolver.stop(id);
    }

    public static void grid(Long id) {

        CSPSolver solver = CSPSolver.findById(id);

        if (solver != null) {

            Grid grid = solver.getGrid();
            render("Application/index.html", grid);
        }
    }
}
