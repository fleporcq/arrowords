package models;

import java.util.ArrayList;
import java.util.List;

public class WhiteCell extends Cell {

    private char letter;

    public WhiteCell() {
        this.letter = '.';
    }

    public char getLetter() {
        return this.letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "[WhiteCell] " + super.toString();
    }

    public List<GridWord> getWords(Grid grid) {

        List<GridWord> words = new ArrayList<GridWord>();

        for (GridWord gridWord : grid.getWords()) {
            if (gridWord.contains(this)) {
                words.add(gridWord);
            }
            if (words.size() == 2) {
                break;
            }
        }

        return words;
    }

}
