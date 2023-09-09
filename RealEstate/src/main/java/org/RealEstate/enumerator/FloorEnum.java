package org.RealEstate.enumerator;

public enum FloorEnum {
	  FIRST(1),
	  SECOND(2),
	  THIRD(3),
	  FOURTH(4),
	  FIFTH(5),
	  SIXTH(6),
	  SEVENTH(7),
	  EIGHTH(8),
	  NINTH(9),
	  TENTH(10),
	  ELEVENTH(11),
	  TWELVETH(12);
	
	private int floor;

	private FloorEnum(int floor) {
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
}
