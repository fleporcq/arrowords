package models;

public class BlackCell extends Cell {

	private String firstDefinition;

	private String secondDefinition;

	public String getFirstDefinition() {
		return firstDefinition;
	}

	public void setFirstDefinition(String firstDefinition) {
		this.firstDefinition = firstDefinition;
	}

	public String getSecondDefinition() {
		return secondDefinition;
	}

	public void setSecondDefinition(String secondDefinition) {
		this.secondDefinition = secondDefinition;
	}

	@Override
	public String toString() {
		return "[BlackCell] " + super.toString();
	}
}
