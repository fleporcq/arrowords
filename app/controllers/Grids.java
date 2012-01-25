package controllers;

import java.util.List;

import models.Dictionary;
import models.Grid;
import play.mvc.Controller;
import solvers.CSPSolver;

public class Grids extends Controller {

    private static final int MIN_WORDS_LENGTH = 2;
    private static final int BLACK_CELLS_PERCENT = 25;
    private static final int BLACK_CELLS_MAX_ALIGN = 4;
    private static final int BLACK_CELLS_MAX_CONNECTED = 6;
    private static final String DICTIONARY = "fr.txt";

    public static void index() {
        List<CSPSolver> solvers = CSPSolver.findAll();
        render(solvers);
    }

    public static void generate(int width, int height) {
        Grid grid = new Grid(width, height, MIN_WORDS_LENGTH);
        grid.generatePseudoRandomBlackCells(BLACK_CELLS_PERCENT, BLACK_CELLS_MAX_ALIGN, BLACK_CELLS_MAX_CONNECTED);
        renderJSON(grid.getAsJson().toString());
    }

    public static void solve(String jsonGrid) {
        if (jsonGrid != null) {
            Grid grid = new Grid(jsonGrid);
            Dictionary dictionary = new Dictionary(DICTIONARY);
            CSPSolver solver = new CSPSolver(grid, dictionary);
            solver.start();
            renderText(solver.getId());
        }

    }

    public static void getProgression(Long solverId) {
        CSPSolver solver = CSPSolver.findById(solverId);
        if (solver != null) {
            renderJSON(solver.getProgression().toString());
        }
    }

    public static void stop(Long solverId) {

        CSPSolver.stop(solverId);

    }
}
