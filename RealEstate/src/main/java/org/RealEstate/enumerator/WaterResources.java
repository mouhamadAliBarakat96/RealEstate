package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum WaterResources {

	CALCAREOUS_WATER(Utility.getMessage("calcareous_water")),
	SWEET_WATER(Utility.getMessage("sweet_water")),
	STATEWATER(Utility.getMessage("state_water")),
	NO_WATER(Utility.getMessage("no_water")),
	A_WELL(Utility.getMessage("a_well"));

	private String water;

	private WaterResources(String water) {
		this.water = water;
	}

	
}
