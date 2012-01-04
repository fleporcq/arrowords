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

    public String getContent() {
        StringBuilder contentBuilder = new StringBuilder();
        for (WhiteCell cell : this) {
            contentBuilder.append(cell.getLetter());
        }
        return contentBuilder.toString();
    }

    @Override
    public String toString() {
        return "First cell : " + this.getFirstCell() + ", Length : " + this.getLength() + ", Axis : " + this.getAxis() + ", Content : " + this.getContent();
    }
}
