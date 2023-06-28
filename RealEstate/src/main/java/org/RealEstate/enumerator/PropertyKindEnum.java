package org.RealEstate.enumerator;

public enum PropertyKindEnum {

 	REALESTATE("Real Estate"),CHALET("Chalet");

	private String name;

	private PropertyKindEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
