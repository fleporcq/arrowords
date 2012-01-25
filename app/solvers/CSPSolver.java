package solvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import models.Dictionary;
import models.Grid;
import models.GridWord;
import play.Logger;

import com.google.gson.JsonObject;

public class CSPSolver extends Thread {

    private Grid grid;

    private Stack<GridWord> solvedWords;

    private Stack<GridWord> wordsToSolve;

    private Dictionary dictionary;

    private Long time;

    private boolean solved;

    public CSPSolver(Grid grid, Dictionary dictionary) {
        this.solved = false;
        this.grid = grid;
        this.dictionary = dictionary;
        this.solvedWords = new Stack<GridWord>();
        this.wordsToSolve = new Stack<GridWord>();

    }

    @Override
    public void run() {

        this.startChrono();

        this.wordsToSolve.addAll(this.grid.getWords());

        while (this.solvedWords.size() < this.grid.getWords().size()) {

            this.sortWordsByConstraint(this.wordsToSolve);

            GridWord gridWord = this.wordsToSolve.pop();

            gridWord.savePreviousContent();

            LinkedList<String> domain = this.dictionary.find(gridWord.contentAsString(), gridWord.getNotIn());

            Collections.shuffle(domain);

            boolean finded = false;

            for (String word : domain) {

                gridWord.setContent(word);

                boolean matchAllCrossWords = true;
                for (GridWord crossWord : gridWord.getCrossWords()) {
                    if (!this.dictionary.match(crossWord.contentAsString(), crossWord.getNotIn())) {
                        matchAllCrossWords = false;
                        break;
                    }

                }

                if (matchAllCrossWords) {
                    finded = true;
                    break;
                }

            }

            if (finded) {
                this.solvedWords.push(gridWord);
            }
            else {
                gridWord.loadPreviousContent();

                this.wordsToSolve.push(gridWord);

                if (this.solvedWords.size() > 0) {
                    GridWord previousSolvedWord = this.solvedWords.pop();
                    gridWord.clearNotIn();
                    previousSolvedWord.addNotIn();
                    previousSolvedWord.loadPreviousContent();
                    this.wordsToSolve.push(previousSolvedWord);
                }

            }
        }

        this.solved = true;

        try {
            this.sleep(10000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean checkSolution() {
        boolean solved = true;
        if (this.dictionary != null) {

            for (GridWord gridWord : this.grid.getWords()) {
                String word = gridWord.contentAsString();
                if (!this.dictionary.match(word)) {
                    if (solved) {
                        solved = false;
                    }
                    Logger.info("The word '%s' doesn't exist in the dictionary", word);
                }
            }
            return solved;
        }
        Logger.debug("Dictionary is null");
        return false;
    }

    private void sortWordsByConstraint(Stack<GridWord> wordsToSolve) {
        Collections.sort(wordsToSolve, new Comparator<GridWord>() {
            @Override
            public int compare(GridWord gw1, GridWord gw2) {
                Float c1 = computeConstraint(gw1);
                Float c2 = computeConstraint(gw2);
                return c1.compareTo(c2);
            }
        });
    }

    public static Float computeConstraint(GridWord word) {
        int lettersCount = word.countLetters();
        int length = word.getLength();
        return lettersCount / Float.valueOf(length);
    }

    // public int computeConstraint() {
    // int lettersCount = this.countLetters();
    // int length = this.getLength();
    // return ((lettersCount + 1) * length) + length;
    // }

    private void startChrono() {
        this.time = System.nanoTime();
    }

    public Long getElapsedTime() {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - this.time, TimeUnit.NANOSECONDS);
    }

    public static CSPSolver findById(Long id) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == id && thread instanceof CSPSolver) {
                return (CSPSolver) thread;
            }
        }
        return null;
    }

    public static List<CSPSolver> findAll() {
        List<CSPSolver> solvers = new ArrayList<CSPSolver>();
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread instanceof CSPSolver) {
                solvers.add((CSPSolver) thread);
            }
        }
        return solvers;
    }

    public static void stop(Long id) {
        CSPSolver solver = findById(id);
        if (solver != null) {
            solver.stop();
        }
    }

    public int getSolvedCount() {
        return this.solvedWords.size();
    }

    public int getTotalCount() {
        return this.grid.getWords().size();
    }

    public Grid getGrid() {
        return this.grid;
    }

    public JsonObject getProgression() {

        JsonObject json = new JsonObject();
        JsonObject infos = new JsonObject();
        json.addProperty("solved", this.solved);
        infos.addProperty("solvedCount", this.getSolvedCount());
        infos.addProperty("totalCount", this.getTotalCount());
        infos.addProperty("elapsedTime", this.getElapsedTime());
        json.add("grid", this.grid.getAsJson());
        json.add("infos", infos);

        return json;
    }
}
