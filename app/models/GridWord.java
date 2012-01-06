package models;

import java.util.ArrayList;

import enums.Axis;

public class GridWord extends ArrayList<WhiteCell> {

    private Axis axis;

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
        int wordLength = word.length();
        if (wordLength == this.size()) {
            char[] letters = word.toCharArray();
            for (int i = 0; i < wordLength; i++) {
                this.get(i).setLetter(letters[i]);
            }
        }
    }

    @Override
    public String toString() {
        return "First cell : " + this.getFirstCell() + ", Length : " + this.getLength() + ", Axis : " + this.getAxis() + ", Content : " + this.contentAsString();
    }
}
