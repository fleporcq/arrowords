package solvers;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

import models.Dictionary;
import models.Grid;
import models.GridWord;

public class CSPSolver extends AbstractSolver {

    private Stack<GridWord> solvedWords;
    private Stack<GridWord> wordsToSolve;

    public CSPSolver(Grid grid, Dictionary dictionary) {
        super(grid, dictionary);
        this.solvedWords = new Stack<GridWord>();
        this.wordsToSolve = new Stack<GridWord>();
    }

    @Override
    protected void solve() {
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
                this.track(gridWord);
            }
            else {
                this.backtrack(gridWord);

            }
        }

    }

    private void track(GridWord gridWord) {
        this.solvedCount++;
        this.solvedWords.push(gridWord);
    }

    private void backtrack(GridWord gridWord) {

        gridWord.loadPreviousContent();

        this.wordsToSolve.push(gridWord);

        gridWord.clearNotIn();

        GridWord previousSolvedWord = null;

        while (this.solvedWords.size() > 0 && !gridWord.isCrossWords(previousSolvedWord)) {
            this.solvedCount--;
            previousSolvedWord = this.solvedWords.pop();
            if (gridWord.isCrossWords(previousSolvedWord)) {
                previousSolvedWord.addNotIn();
            }
            previousSolvedWord.loadPreviousContent();
            this.wordsToSolve.push(previousSolvedWord);
        }
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

}
