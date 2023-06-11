package org.RealEstate.service;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.ws.rs.core.Response;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.AppratmentRentFacade;
import org.RealEstate.facade.AppratmentSellFacade;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.LandFacade;
import org.RealEstate.facade.OfficeRentFacade;
import org.RealEstate.facade.OfficeSellFacade;
import org.RealEstate.facade.ShopRentFacade;
import org.RealEstate.facade.ShopSellFacade;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.utils.Utils;

@Stateless
public class PostService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private AppratmentRentFacade appratmentRent;
	@EJB
	private AppratmentSellFacade appratmentSell;
	@EJB
	private LandFacade land;
	@EJB
	private ChaletFacade chalet;
	@EJB
	private ShopRentFacade shopRent;
	@EJB
	private ShopSellFacade shopSellFacade;
	@EJB
	private OfficeRentFacade officeRentFacade;
	@EJB
	private OfficeSellFacade officeSellFacade;

	
	public Response mangmentAddPost(String jsonString) {

		return null;
	}

	private Object checkPostType(String jsonString) throws IOException {

		String postType = "";

		switch (postType) {
		case "APPRATMENT_RENT":
			AppratmentRent appratmentRent = Utils.getObjectFromString(jsonString, AppratmentRent.class);

		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
		case "LAND":

			Land Land = Utils.getObjectFromString(jsonString, Land.class);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);
		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);
		case "OFFICE_SALE":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);

		default:
			return null;
		}

	}

}
