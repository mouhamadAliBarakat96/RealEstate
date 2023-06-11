package org.RealEstate.enumerator;

public enum Unit {

	MONTHLY("monthly"), YEARLY("yearly");

	private String key;

	private Unit(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}