package models;

public class WhiteCell extends Cell {

	private char letter;

	public WhiteCell() {
		this.letter = '.';
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	@Override
	public String toString() {
		return "[WhiteCell] " + super.toString();
	}
}
