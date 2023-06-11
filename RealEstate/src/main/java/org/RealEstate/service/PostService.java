package org.RealEstate.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import org.RealEstate.enumerator.PostStatus;
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
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.utils.Utils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
public class PostService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private AppratmentRentFacade appratmentRentFacade;
	@EJB
	private AppratmentSellFacade appratmentSellFacade;
	@EJB
	private LandFacade landFacade;
	@EJB
	private ChaletFacade chaletFacade;
	@EJB
	private ShopRentFacade shopRentFacade;
	@EJB
	private ShopSellFacade shopSellFacade;
	@EJB
	private OfficeRentFacade officeRentFacade;
	@EJB
	private OfficeSellFacade officeSellFacade;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public Response mangmentAddPost(MultipartFormDataInput input) {

		try {

			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			// form contain image
			List<InputPart> inputParts = uploadForm.get("file");
			System.out.println("inputParts size: " + inputParts.size());

			// json in data to save
			List<InputPart> data = uploadForm.get("data");
			if (data == null) {
				// return error

			}

			// TODO
			checkUserNbOfPostAllowed();

			String jsonDataFromRequest = data.get(0).getBodyAsString();
			savePost(jsonDataFromRequest);
			// manage save images
			uploadImagesMultiPart.uploadImage(inputParts);
		} catch (Exception e) {
			// return error
			e.printStackTrace();
		}

		return null;
	}

	private Object savePost(String jsonString) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		String postType = jsonNode.get("postType").asText();

		switch (postType) {
		case "APPRATMENT_RENT":
			AppratmentRent appratmentRent = Utils.getObjectFromString(jsonString, AppratmentRent.class);
			addCommonsField(appratmentRent);
			return appratmentRentFacade.mangmentSavePost(appratmentRent);
		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
			addCommonsField(appratmentSell);
			return appratmentSellFacade.mangmentSavePost(appratmentSell);
		case "LAND":

			Land Land = Utils.getObjectFromString(jsonString, Land.class);
			addCommonsField(Land);
			return landFacade.mangmentSavePost(Land);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
			// addCommonsField(chalet);
			return chaletFacade.mangmentSavePost(chalet);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
			addCommonsField(shopRent);
			return shopRentFacade.mangmentSavePost(shopRent);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);
			addCommonsField(shopSell);
			return shopSellFacade.mangmentSavePost(shopSell);
		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);
			addCommonsField(officeRent);
			return officeRentFacade.mangmentSavePost(officeRent);
		case "OFFICE_SALE":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);
			addCommonsField(officeSell);
			return officeSellFacade.mangmentSavePost(officeSell);
		default:
			// return error no post type
			return null;
		}

	}

	private void checkUserNbOfPostAllowed() {

	}

	private void addCommonsField(RealEstate realEstate) {
		realEstate.setPostStatus(PostStatus.PENDING);
	}

	private void addCommonsFieldChalet(Chalet chalet) {
		chalet.setPostStatus(PostStatus.PENDING);

	}

	private void checkPostConstraintFields(RealEstate realEstate) {

	}

	private void checkChaletContraintFields() {

	}

}
