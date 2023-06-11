package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum PostType {

	APPRATMENT_RENT(Utility.getMessage("appartment_rent",Utility.BUNDLE_FILE_NAME_EN)),
	APPRATMENT_SELL(Utility.getMessage("appartment_sell",Utility.BUNDLE_FILE_NAME_EN)),
	LAND(Utility.getMessage("land",Utility.BUNDLE_FILE_NAME_EN)),
	SHOP_RENT(Utility.getMessage("shop_rent",Utility.BUNDLE_FILE_NAME_EN)),
	SHOP_SELL(Utility.getMessage("shop_sell",Utility.BUNDLE_FILE_NAME_EN)),
	OFFICE_RENT(Utility.getMessage("office_rent",Utility.BUNDLE_FILE_NAME_EN)),
	OFFICE_SELL(Utility.getMessage("office_sell",Utility.BUNDLE_FILE_NAME_EN));

	private String name;

	private PostType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
