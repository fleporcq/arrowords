package models;

import java.util.ArrayList;
import java.util.List;

import enums.Axis;

public class GridWord extends ArrayList<WhiteCell> {

    private Axis axis;

    private List<GridWord> crossWords;

    private String previousContent;

    private StringBuilder notIn;

    public GridWord() {
        this.notIn = new StringBuilder();
    }

    public void savePreviousContent() {
        this.previousContent = this.contentAsString();
    }

    public void loadPreviousContent() {
        if (this.previousContent != null) {
            this.setContent(this.previousContent);
            this.previousContent = null;
        }
    }

    public void addNotIn(String word) {
        if (this.notIn.length() > 0) {
            this.notIn.append("|");
        }
        this.notIn.append(word);
    }

    public void clearNotIn() {
        this.notIn = new StringBuilder();
    }

    public String getNotIn() {
        return this.notIn.toString();
    }

    public Axis getAxis() {
        return this.axis;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    public int getLength() {
        return this.size();
    }

    public WhiteCell getFirstCell() {
        if (this.size() > 0) {
            return this.get(0);
        }
        return null;
    }

    public String contentAsString() {
        StringBuilder contentBuilder = new StringBuilder();
        for (WhiteCell cell : this) {
            contentBuilder.append(cell.getLetter());
        }
        return contentBuilder.toString();
    }

    public void setContent(String word) {
        if (word != null) {

            int wordLength = word.length();

            if (wordLength == this.size()) {

                char[] letters = word.toCharArray();
                for (int i = 0; i < wordLength; i++) {
                    this.get(i).setLetter(letters[i]);
                }

            }
        }
    }

    public int countLetters() {
        int lettersCount = 0;
        for (WhiteCell cell : this) {
            if (cell.getLetter() != '.') {
                lettersCount++;
            }
        }
        return lettersCount;
    }

    public Float getComplexity() {
        int lettersCount = this.countLetters();
        int length = this.getLength();
        return lettersCount / Float.valueOf(length);
    }

    public int getMyComplexity() {
        int lettersCount = this.countLetters();
        int length = this.getLength();
        return ((lettersCount + 1) * length) + length;
    }

    public void setCrossWord(Grid grid) {
        this.crossWords = new ArrayList<GridWord>();
        for (WhiteCell cell : this) {
            List<GridWord> words = cell.getWords(grid);
            for (GridWord word : words) {
                if (word != this) {
                    this.crossWords.add(word);
                }
            }
        }
    }

    public List<GridWord> getCrossWords() {
        return this.crossWords;
    }

    @Override
    public String toString() {
        return "First cell : " + this.getFirstCell() + ", Length : " + this.getLength() + ", Axis : " + this.getAxis() + ", Content : " + this.contentAsString();
    }
}
