package org.RealEstate.enumerator;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.model.StoreHouseRent;
import org.RealEstate.model.StoreHouseSell;
import org.RealEstate.utils.Utility;

public enum PostType {

	APPRATMENT_RENT(Utility.getMessage("appartment_rent"),Utility.getMessage("appartment_rent",Utility.BUNDLE_FILE_NAME_AR)),
	APPRATMENT_SELL(Utility.getMessage("appartment_sell"),Utility.getMessage("appartment_sell",Utility.BUNDLE_FILE_NAME_AR)),
	LAND(Utility.getMessage("land"),Utility.getMessage("land",Utility.BUNDLE_FILE_NAME_AR)),
	SHOP_RENT(Utility.getMessage("shop_rent"),Utility.getMessage("shop_rent",Utility.BUNDLE_FILE_NAME_AR)),
	SHOP_SELL(Utility.getMessage("shop_sell"),Utility.getMessage("shop_sell",Utility.BUNDLE_FILE_NAME_AR)),
	OFFICE_RENT(Utility.getMessage("office_rent"),Utility.getMessage("office_rent",Utility.BUNDLE_FILE_NAME_AR)),
	OFFICE_SELL(Utility.getMessage("office_sell"),Utility.getMessage("office_sell",Utility.BUNDLE_FILE_NAME_AR)),
	STORE_HOUSE_SELL(Utility.getMessage("store_house_sell"),Utility.getMessage("store_house_sell",Utility.BUNDLE_FILE_NAME_AR)),
	STORE_HOUSE_RENT(Utility.getMessage("store_house_rent"),Utility.getMessage("store_house_rent",Utility.BUNDLE_FILE_NAME_AR));
	
	private String name;
	private String name_ar;

	private PostType(String name,String name_ar) {
		this.name = name;
		this.name_ar=name_ar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Class<? extends RealEstate> getEntityType(String postType) {
		switch (postType) {
		case "APPRATMENT_RENT":

			return AppratmentRent.class;
		case "APPRATMENT_SELL":

			return AppratmentSell.class;
		case "LAND":

			return Land.class;

		case "SHOP_RENT":

			return ShopRent.class;
		case "SHOP_SELL":

			return ShopSell.class;
		case "OFFICE_RENT":

			return OfficeRent.class;
		case "OFFICE_SELL":

			return OfficeSell.class;

		case "STORE_HOUSE_SELL":

			return StoreHouseSell.class;
		case "STORE_HOUSE_RENT":

			return StoreHouseRent.class;

		default:

			return null;
		}
	}

	public static PostType findEnum(String postType) {
		switch (postType) {
		case "APPRATMENT_RENT":

			return APPRATMENT_RENT;
		case "APPRATMENT_SELL":

			return APPRATMENT_SELL;
		case "LAND":

			return LAND;

		case "SHOP_RENT":

			return SHOP_RENT;
		case "SHOP_SELL":

			return SHOP_SELL;
		case "OFFICE_RENT":

			return OFFICE_RENT;
		case "OFFICE_SELL":

			return OFFICE_SELL;
		case "STORE_HOUSE_SELL":

			return STORE_HOUSE_SELL;
		case "STORE_HOUSE_RENT":

			return STORE_HOUSE_RENT;
		default:

			return null;
		}
	}

	public String getName_ar() {
		return name_ar;
	}

	public void setName_ar(String name_ar) {
		this.name_ar = name_ar;
	}
	
}
