package controllers;

import java.util.List;

import models.Dictionary;
import models.Grid;
import play.mvc.Controller;
import solvers.CSPSolver;

public class Application extends Controller {
    private static final int MIN_WORDS_LENGTH = 2;
    private static final int BLACK_CELLS_MAX_ALIGN = 3;
    private static final int BLACK_CELLS_MAX_CONNECTED = 4;

    private static final String DICTIONARY = "fr.txt";

    public static final int MIN_BLACK_CELLS_PERCENT = 10;
    public static final int MAX_BLACK_CELLS_PERCENT = 25;
    public static final int DEFAULT_BLACK_CELLS_PERCENT = 20;
    public static final int MIN_HEIGHT = 5;
    public static final int MAX_HEIGHT = 10;
    public static final int DEFAULT_HEIGHT = 8;
    public static final int MIN_WIDTH = 5;
    public static final int MAX_WIDTH = 10;
    public static final int DEFAULT_WIDTH = 8;

    public static void index() {
        List<CSPSolver> solvers = CSPSolver.findAll();
        render(solvers);
    }

    public static void generate(int width, int height, int blackCellsPercent) {
        Grid grid = new Grid(width, height, MIN_WORDS_LENGTH);
        grid.generatePseudoRandomBlackCells(blackCellsPercent, BLACK_CELLS_MAX_ALIGN, BLACK_CELLS_MAX_CONNECTED);
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
