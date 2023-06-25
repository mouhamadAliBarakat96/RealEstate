package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum WaterResources {

	 CALCAREOUS_WATER(Utility.getMessage("calcareous_water", Utility.BUNDLE_FILE_NAME))
	,SWEET_WATER(Utility.getMessage("sweet_water", Utility.BUNDLE_FILE_NAME))
	,STATEWATER(Utility.getMessage("state_water", Utility.BUNDLE_FILE_NAME))
	,NO_WATER(Utility.getMessage("no_water", Utility.BUNDLE_FILE_NAME))
	,A_WELL(Utility.getMessage("a_well", Utility.BUNDLE_FILE_NAME));
	
	
	private String water;

	private WaterResources(String water) {
		this.water = water;
	}
}
