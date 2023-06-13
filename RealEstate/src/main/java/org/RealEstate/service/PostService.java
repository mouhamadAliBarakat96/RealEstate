package org.RealEstate.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.enumerator.PostStatus;
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
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.utils.Constants;
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

	public Response mangmentAddPost(MultipartFormDataInput input) {

		try {

			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			// form contain image
			List<InputPart> inputParts = uploadForm.get("file");

			if (inputParts == null || inputParts.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.AT_LAST_ONE_IMAGE_REQUIRED).build();

			}

			if (inputParts.size() > Constants.NB_IMAGE_IN_POST_ALLOWED) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED)
						.build();

			}

			// json in data to save
			List<InputPart> data = uploadForm.get("data");
			if (data == null) {

				return Response.status(Status.BAD_REQUEST).entity(Constants.EMPTY_REQUEST_DONT_CONTAIN_DATA).build();
			}

			// TODO
			checkUserNbOfPostAllowed();

			String jsonDataFromRequest = data.get(0).getBodyAsString();
			Object obj = savePost(jsonDataFromRequest, inputParts);

			return Response.status(Status.OK).entity(Utils.objectToString(obj)).build();

		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	
	}

	private Object savePost(String jsonString, List<InputPart> inputParts) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		String postType = jsonNode.get("postType").asText();

		switch (postType) {
		case "APPRATMENT_RENT":
			AppratmentRent appratmentRent = Utils.getObjectFromString(jsonString, AppratmentRent.class);
			addCommonsField(appratmentRent);
			checkPostConstraintFields(appratmentRent);
			return appratmentRentFacade.mangmentSavePost(appratmentRent, inputParts);
		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
			addCommonsField(appratmentSell);

			checkPostConstraintFields(appratmentSell);
			return appratmentSellFacade.mangmentSavePost(appratmentSell, inputParts);
		case "LAND":

			Land land = Utils.getObjectFromString(jsonString, Land.class);
			addCommonsField(land);

			checkPostConstraintFields(land);
			return landFacade.mangmentSavePost(land, inputParts);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
			addCommonsFieldChalet(chalet);
			checkChaletConstraintFields(chalet);

			return chaletFacade.mangmentSavePost(chalet, inputParts);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
			addCommonsField(shopRent);
			checkPostConstraintFields(shopRent);

			return shopRentFacade.mangmentSavePost(shopRent, inputParts);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);
			addCommonsField(shopSell);
			checkPostConstraintFields(shopSell);

			return shopSellFacade.mangmentSavePost(shopSell, inputParts);
		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);
			addCommonsField(officeRent);
			checkPostConstraintFields(officeRent);

			return officeRentFacade.mangmentSavePost(officeRent, inputParts);
		case "OFFICE_SALE":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);
			addCommonsField(officeSell);
			checkPostConstraintFields(officeSell);

			return officeSellFacade.mangmentSavePost(officeSell, inputParts);
		default:

			return null;
		}

	}

	private void checkUserNbOfPostAllowed() {

	}

	private void addCommonsField(RealEstate realEstate) {
		realEstate.setPostStatus(PostStatus.PENDING);
		realEstate.setPostDate(new Date());
	}

	private void addCommonsFieldChalet(Chalet chalet) {
		chalet.setPostStatus(PostStatus.PENDING);
		chalet.setPostDate(new Date());

	}

	private void checkPostConstraintFields(RealEstate realEstate) {

	}

	private void checkChaletConstraintFields(Chalet chalet) {

	}

}
