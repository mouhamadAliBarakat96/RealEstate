package org.RealEstate.enumerator;

public enum CountEnum {
	    ONE(1),
	    TWO(2),
	    THREE(3),
	    FOUR(4),
	    FIVE(5),
	    SIX(6),
	    SEVEN(7),
	    MORE(0);
	private int value;

	private CountEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
