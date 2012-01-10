package models;

import java.util.ArrayList;
import java.util.List;

import enums.Axis;

public class GridWord extends ArrayList<WhiteCell> {

    private Axis axis;

    private String previousContent;

    private List<String> notIn;

    public GridWord() {
        this.notIn = new ArrayList<String>();
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
                this.previousContent = this.contentAsString();
                char[] letters = word.toCharArray();
                for (int i = 0; i < wordLength; i++) {
                    this.get(i).setLetter(letters[i]);
                }
            }
        }
    }

    public void resetContent() {
        this.notIn.add(this.contentAsString());
        this.setContent(this.previousContent);
    }

    public List<String> getNotIn() {
        return this.notIn;
    }

    public void resetNotIn() {
        this.notIn.clear();
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

    public Integer getComplexity() {
        int lettersCount = this.countLetters();
        int length = this.getLength();
        return ((lettersCount + 1) * length) + length;
        // return lettersCount;
    }

    @Override
    public String toString() {
        return "First cell : " + this.getFirstCell() + ", Length : " + this.getLength() + ", Axis : " + this.getAxis() + ", Content : " + this.contentAsString();
    }
}
