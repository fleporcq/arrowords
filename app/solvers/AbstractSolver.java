package solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.Dictionary;
import models.Grid;
import models.GridWord;
import play.Logger;

import com.google.gson.JsonObject;

public abstract class AbstractSolver extends Thread {

    protected Grid grid;

    protected Dictionary dictionary;

    private Long time;

    private boolean solved;

    protected int solvedCount;

    public AbstractSolver(Grid grid, Dictionary dictionary) {
        this.grid = grid;
        this.dictionary = dictionary;
    }

    @Override
    public void run() {

        this.startChrono();

        this.solved = false;
        this.solvedCount = 0;

        this.solve();

        this.solved = true;

        try {
            this.sleep(10000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected abstract void solve();

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

    private void startChrono() {
        this.time = System.nanoTime();
    }

    public Long getElapsedTime() {
        return TimeUnit.MILLISECONDS.convert(System.nanoTime() - this.time, TimeUnit.NANOSECONDS);
    }

    public static <T extends AbstractSolver> T findById(Long id) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == id && thread instanceof AbstractSolver) {
                return (T) thread;
            }
        }
        return null;
    }

    public static <T extends AbstractSolver> List<T> findAll() {
        List<T> solvers = new ArrayList<T>();
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread instanceof AbstractSolver) {
                solvers.add((T) thread);
            }
        }
        return solvers;
    }

    public static void stop(Long id) {
        AbstractSolver solver = findById(id);
        if (solver != null) {
            solver.stop();
        }
    }

    public int getSolvedCount() {
        return this.solvedCount;
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
