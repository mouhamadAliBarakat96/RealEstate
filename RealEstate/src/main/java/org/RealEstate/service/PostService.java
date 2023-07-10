package org.RealEstate.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.dto.PaginationResponse;
import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.UserCategory;
import org.RealEstate.facade.AppratmentRentFacade;
import org.RealEstate.facade.AppratmentSellFacade;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.LandFacade;
import org.RealEstate.facade.OfficeRentFacade;
import org.RealEstate.facade.OfficeSellFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.ShopRentFacade;
import org.RealEstate.facade.ShopSellFacade;
import org.RealEstate.facade.StoreHouseRentFacade;
import org.RealEstate.facade.StoreHouseSellFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.model.StoreHouseRent;
import org.RealEstate.model.StoreHouseSell;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.NumberPatternDetector;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
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
	private StoreHouseRentFacade storeHouseRentFacade;
	@EJB
	private StoreHouseSellFacade storeHouseSellFacade;
	@EJB
	private ShopSellFacade shopSellFacade;
	@EJB
	private OfficeRentFacade officeRentFacade;
	@EJB
	private OfficeSellFacade officeSellFacade;

	@EJB
	private UserFacade userFacade;

	@EJB
	private RealEstateFacade restateFacade;

	@EJB
	private AppSinglton appSinglton;

	@EJB
	private VillageFacade villageFacade;

	@EJB
	private DistrictFacade districtFacade;
	@EJB
	private GovernorateFacade governorateFacade;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	/*
	 * Manage Add post
	 */
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

			User user = checkUserConstraint(data.get(0).getBodyAsString());

			// check village Exist
			checkVillageExist(data.get(0).getBodyAsString());
			String jsonDataFromRequest = data.get(0).getBodyAsString();
			Object obj = savePost(jsonDataFromRequest, inputParts, user);

			if (obj == null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.POST_TYPE_NOT_SUPPORTED).build();

			} else {
				return Response.status(Status.OK).entity(Utils.objectToString(obj)).build();

			}

		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response updatePostImage(Long id, MultipartFormDataInput input) {

		try {
			RealEstate realEstate = restateFacade.find(id);
			if (realEstate == null) {
				throw new Exception("REALESTATE_DONT_EXISTS");

			}

			int imageCount = realEstate.getImages().size();
			// form contain image
			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			List<InputPart> inputParts = uploadForm.get("file");

			if (inputParts == null || inputParts.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.AT_LAST_ONE_IMAGE_REQUIRED).build();

			}

			if (inputParts.size() + imageCount > Constants.NB_IMAGE_IN_POST_ALLOWED) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED)
						.build();

			}

			List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
			realEstate.getImages().addAll(imagesUrl);
			realEstate.setPostStatus(PostStatus.PENDING);
			realEstate = restateFacade.save(realEstate);
			return Response.status(Status.OK).entity(Utils.objectToString(realEstate)).build();

		}

		catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response updateChaletImage(Long id, MultipartFormDataInput input) {

		try {
			Chalet chalet = chaletFacade.find(id);
			if (chalet == null) {
				throw new Exception("Chalet_DONT_EXISTS");

			}
			int imageCount = chalet.getImages().size();
			// form contain image
			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			List<InputPart> inputParts = uploadForm.get("file");

			if (inputParts == null || inputParts.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.AT_LAST_ONE_IMAGE_REQUIRED).build();

			}

			if (inputParts.size() + imageCount > Constants.NB_IMAGE_IN_POST_ALLOWED) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED)
						.build();

			}

			List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
			chalet.setImages(imagesUrl);

			chalet = chaletFacade.save(chalet);
			return Response.status(Status.OK).entity(Utils.objectToString(chalet)).build();

		}

		catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response removePostImage(Long id, List<String> imagesToDelete) {

		try {
			RealEstate realEstate = restateFacade.find(id);

			if (realEstate == null) {

				throw new Exception("REALESTATE_DONT_EXISTS");
			}

			List<String> listImage = realEstate.getImages();

			if (listImage.size() == 1) {
				throw new Exception("AT_LEAST_POST_SHOULD_HAVE_ONE_IMAGE");
			}

			for (String varDelete : imagesToDelete) {
				if (!listImage.contains(varDelete)) {
					throw new Exception("THIS_IMAGE_NOT_RELATED_TO_THIS_POST");

				}
				listImage.remove(varDelete);
			}

			uploadImagesMultiPart.deleteImagePost(imagesToDelete);
			realEstate = restateFacade.save(realEstate);

			return Response.status(Status.OK).entity(Utils.objectToString(realEstate)).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response removePostImageChalet(Long id, List<String> imagesToDelete) {

		try {
			Chalet chalet = chaletFacade.find(id);

			if (chalet == null) {

				throw new Exception("CHALET_DONT_EXISTS");
			}

			List<String> listImage = chalet.getImages();

			if (listImage.size() == 1) {
				throw new Exception("AT_LEAST_POST_SHOULD_HAVE_ONE_IMAGE");
			}

			for (String varDelete : imagesToDelete) {
				if (!listImage.contains(varDelete)) {
					throw new Exception("THIS_IMAGE_NOT_RELATED_TO_THIS_POST");

				}
				listImage.remove(varDelete);
			}

			uploadImagesMultiPart.deleteImagePost(imagesToDelete);
			chalet = chaletFacade.save(chalet);

			return Response.status(Status.OK).entity(Utils.objectToString(chalet)).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response mangmentUpdatePost(MultipartFormDataInput input) {

		try {

			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			// json in data to save
			List<InputPart> data = uploadForm.get("data");
			if (data == null) {

				return Response.status(Status.BAD_REQUEST).entity(Constants.EMPTY_REQUEST_DONT_CONTAIN_DATA).build();
			}

			User user = findUser(data.get(0).getBodyAsString());

			// check village Exist
			checkVillageExist(data.get(0).getBodyAsString());
			String jsonDataFromRequest = data.get(0).getBodyAsString();
			Object obj = updatePost(jsonDataFromRequest, user);

			if (obj == null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.POST_TYPE_NOT_SUPPORTED).build();

			} else {
				return Response.status(Status.OK).entity(Utils.objectToString(obj)).build();

			}

		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	private Object updatePost(String jsonString, User user) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		String postType = jsonNode.get("postType").asText();

		if (postType == null) {
			return null;
		}

		switch (postType) {
		case "APPRATMENT_RENT":
			AppratmentRent appratmentRent = Utils.getObjectFromString(jsonString, AppratmentRent.class);

			addCommonsFieldWithoutPostDate(appratmentRent);
			fielddDontChangeOnUpdate(appratmentRent);
			if (appratmentRent.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}

			if (appratmentRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}

			checkPostConstraintFields(appratmentRent);

			return appratmentRentFacade.save(appratmentRent);
		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
			addCommonsFieldWithoutPostDate(appratmentSell);
			fielddDontChangeOnUpdate(appratmentSell);
			if (appratmentSell.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}

			if (appratmentSell.getSpace() * 100 < appratmentSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}

			checkPostConstraintFields(appratmentSell);

			return appratmentSellFacade.save(appratmentSell);
		case "LAND":

			Land land = Utils.getObjectFromString(jsonString, Land.class);
			fielddDontChangeOnUpdate(land);
			if (land.getSpace() < 200) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_200");

			}

			if (land.getSpace() * 4 < land.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_4_DOLLARS");
			}
			addCommonsFieldWithoutPostDate(land);

			checkPostConstraintFields(land);
			return landFacade.save(land);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
			fielddDontChangeOnUpdate(chalet);
			addCommonsFieldChaletWithoutPostDate(chalet);
			checkChaletConstraintFields(chalet);

			return chaletFacade.save(chalet);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
			if (shopRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			if (shopRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			fielddDontChangeOnUpdate(shopRent);
			addCommonsFieldWithoutPostDate(shopRent);
			checkPostConstraintFields(shopRent);

			return shopRentFacade.save(shopRent);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);

			if (shopSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
		case "STORE_HOUSE_RENT":

			StoreHouseRent storeHouseRent = Utils.getObjectFromString(jsonString, StoreHouseRent.class);
			if (storeHouseRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			if (storeHouseRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			fielddDontChangeOnUpdate(storeHouseRent);
			addCommonsFieldWithoutPostDate(storeHouseRent);
			checkPostConstraintFields(storeHouseRent);

			return storeHouseRentFacade.save(storeHouseRent);
		case "STORE_HOUSE_SELL":

			StoreHouseSell storeHouseSell = Utils.getObjectFromString(jsonString, StoreHouseSell.class);

			if (storeHouseSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

			if (storeHouseSell.getSpace() * 100 < storeHouseSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			fielddDontChangeOnUpdate(storeHouseSell);
			addCommonsFieldWithoutPostDate(storeHouseSell);
			checkPostConstraintFields(storeHouseSell);

			return storeHouseSellFacade.save(storeHouseSell);
		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);

			if (officeRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

			if (officeRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			fielddDontChangeOnUpdate(officeRent);
			addCommonsFieldWithoutPostDate(officeRent);
			checkPostConstraintFields(officeRent);

			return officeRentFacade.save(officeRent);
		case "OFFICE_SELL":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);
			if (officeSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			if (officeSell.getSpace() * 100 < officeSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}

			fielddDontChangeOnUpdate(officeSell);
			addCommonsFieldWithoutPostDate(officeSell);
			checkPostConstraintFields(officeSell);

			return officeSellFacade.save(officeSell);
		default:

			return null;
		}

	}

	private void fielddDontChangeOnUpdate(RealEstate r) {
		// old RealtEstate
		RealEstate oldRealEstate = restateFacade.find(r.getId());
		r.setViews(oldRealEstate.getViews());
		r.setLiked(oldRealEstate.getLiked());
		r.setNumberOfCall(oldRealEstate.getNumberOfCall());
		r.setImages(oldRealEstate.getImages());

	}

	private void fielddDontChangeOnUpdate(Chalet chalet) {
		// old RealtEstate
		Chalet oldChalet = chaletFacade.find(chalet.getId());
		chalet.setViews(oldChalet.getViews());
		chalet.setLiked(oldChalet.getLiked());
		chalet.setNumberOfCall(oldChalet.getNumberOfCall());
		chalet.setImages(oldChalet.getImages());

	}

	private Object savePost(String jsonString, List<InputPart> inputParts, User user) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		String postType = jsonNode.get("postType").asText();

		if (postType == null) {
			return null;
		}

		switch (postType) {
		case "APPRATMENT_RENT":
			AppratmentRent appratmentRent = Utils.getObjectFromString(jsonString, AppratmentRent.class);

			if (appratmentRent.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}

			addCommonsField(appratmentRent);
			checkPostConstraintFields(appratmentRent);
			appratmentRent.setUser(user);
			return appratmentRentFacade.mangmentSavePost(appratmentRent, inputParts);
		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
			addCommonsField(appratmentSell);

			if (appratmentSell.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentSell.getSpace() * 100 < appratmentSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}

			checkPostConstraintFields(appratmentSell);
			appratmentSell.setUser(user);

			return appratmentSellFacade.mangmentSavePost(appratmentSell, inputParts);
		case "LAND":

			Land land = Utils.getObjectFromString(jsonString, Land.class);
			if (land.getSpace() < 200) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_200");

			}
			if (land.getSpace() * 4 > land.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_4_DOLLARS");
			}

			addCommonsField(land);
			land.setUser(user);

			checkPostConstraintFields(land);

			return landFacade.mangmentSavePost(land, inputParts);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
			addCommonsFieldChalet(chalet);
			checkChaletConstraintFields(chalet);
			chalet.setUser(user);
			user.getChales().add(chalet);
			return chaletFacade.mangmentSavePost(chalet, inputParts);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
			if (shopRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (shopRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(shopRent);
			checkPostConstraintFields(shopRent);
			shopRent.setUser(user);

			return shopRentFacade.mangmentSavePost(shopRent, inputParts);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);
			if (shopSell.getSpace() * 100 < shopSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (shopSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(shopSell);
			checkPostConstraintFields(shopSell);
			shopSell.setUser(user);

			return shopSellFacade.mangmentSavePost(shopSell, inputParts);

		case "STORE_HOUSE_RENT":

			StoreHouseRent storeHouseRent = Utils.getObjectFromString(jsonString, StoreHouseRent.class);
			if (storeHouseRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (storeHouseRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(storeHouseRent);
			checkPostConstraintFields(storeHouseRent);
			storeHouseRent.setUser(user);

			return storeHouseRentFacade.mangmentSavePost(storeHouseRent, inputParts);
		case "STORE_HOUSE_SELL":

			StoreHouseSell storeHouseSell = Utils.getObjectFromString(jsonString, StoreHouseSell.class);
			if (storeHouseSell.getSpace() * 100 > storeHouseSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (storeHouseSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(storeHouseSell);
			checkPostConstraintFields(storeHouseSell);
			storeHouseSell.setUser(user);

			return storeHouseSellFacade.mangmentSavePost(storeHouseSell, inputParts);

		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);
			if (officeRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (officeRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(officeRent);
			checkPostConstraintFields(officeRent);
			officeRent.setUser(user);

			return officeRentFacade.mangmentSavePost(officeRent, inputParts);
		case "OFFICE_SELL":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);
			if (officeSell.getSpace() * 100 < officeSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (officeSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}
			addCommonsField(officeSell);
			checkPostConstraintFields(officeSell);
			officeSell.setUser(user);

			return officeSellFacade.mangmentSavePost(officeSell, inputParts);
		default:

			return null;
		}

	}

	private User findUser(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		JsonNode idNode = jsonNode.get("userId");
		if (idNode == null) {
			throw new Exception("userId  Should Not BE Null");
		}
		Long id = jsonNode.get("userId").asLong();
		User user = userFacade.find(id);
		if (user == null) {
			throw new Exception(Constants.USER_NOT_EXISTS);
		} else {
			return user;
		}
	}

	private User checkUserConstraint(String jsonString) throws Exception {

		User user = findUser(jsonString);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		JsonNode postTypeNode = jsonNode.get("postType");
		if (postTypeNode == null) {
			throw new Exception("POST_TYPE_SHOULD_NOT_BE_NULL");

		}
		String postType = jsonNode.get("postType").asText();

		if (postType == null || StringUtils.isBlank(postType)) {
			throw new Exception("POST_TYPE_SHOULD_NOT_BE_NULL");
		}

		Long nbOfPost = restateFacade.findUserCountPostPendingOrActive(user.getId())
				+ chaletFacade.findUserCountPostPendingOrActive(user.getId());
		

		// nb of post allowed ;
		// krml is broker bytla3lo zyde
		int nbOfPostAllowed = 0;
		if (user.isBroker()) {
			nbOfPostAllowed = appSinglton.getBrokerNbOfPost();
		}

		if (user.getUserCategory() == UserCategory.REGULAR) {
			nbOfPostAllowed = nbOfPostAllowed + appSinglton.getFreeNbOfPost();

		} else if (user.getUserCategory() == UserCategory.MEDUIM) {

			nbOfPostAllowed = nbOfPostAllowed + appSinglton.getMeduimAccountNbOfPost();

		} else if (user.getUserCategory() == UserCategory.PREMIUM) {
			nbOfPostAllowed = nbOfPostAllowed + appSinglton.getPremuimAccountNbOfPost();
		}

		if (nbOfPost >= nbOfPostAllowed) {
			throw new Exception("EXCEEDED_POST_LIMIT");

		}

		return user;

	}

	private void checkVillageExist(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		JsonNode villageJson = jsonNode.get("village");
		Village village = Utils.getObjectFromString(villageJson.toString(), Village.class);
		villageFacade.findWithExcption(village.getId());

	}

	private void addCommonsField(RealEstate realEstate) {
		addCommonsFieldWithoutPostDate(realEstate);
		realEstate.setPostDate(new Date());
	}

	private void addCommonsFieldChalet(Chalet chalet) {
		addCommonsFieldChaletWithoutPostDate(chalet);
		chalet.setPostDate(new Date());

	}

	private void addCommonsFieldWithoutPostDate(RealEstate realEstate) {
		realEstate.setPostStatus(PostStatus.PENDING);

	}

	private void addCommonsFieldChaletWithoutPostDate(Chalet chalet) {
		chalet.setPostStatus(PostStatus.PENDING);

	}

	private void checkPostConstraintFields(RealEstate realEstate) throws Exception {

		// check tittle and sub tittle not null and empty
		if (StringUtils.isBlank(realEstate.getTittle()) || StringUtils.isBlank(realEstate.getSubTittle())) {
			throw new Exception("TITTLE_OR_SUBTTITLE_SHOULD_NOT_BE_EMPTY");

		}
		// check tittle sub tittle dont have numbet

		if (NumberPatternDetector.checkTextContainNumber(realEstate.getTittle())
				|| NumberPatternDetector.checkTextContainNumber(realEstate.getSubTittle())) {

			throw new Exception("TITTLE_OR_SUBTTITLE_CONTAIN_PHONE_NUMBER");
		}

		if (realEstate.getPrice() < 0) {
			throw new Exception("PRICE_SHOULD_BE_GREATER_THEN_ZERO");

		}

		if (realEstate.getSpace() < 0) {
			throw new Exception("SPACE_SHOULD_BE_GREATER_THEN_ZERO");

		}

	}

	private void checkChaletConstraintFields(Chalet chalet) {

		// TODO
	}
	/*
	 *
	 */

	/*
	 * Update Post Vieux
	 */

	// to use by kassem
	public Response updatePostVieux(Long id, String postType) {
		try {

			switch (postType) {
			case "APPRATMENT_RENT":
				AppratmentRent appratmentRent = appratmentRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentRent != null) {
					appratmentRent.setViews(appratmentRent.getViews() + 1);
					appratmentRentFacade.save(appratmentRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setViews(appratmentSell.getViews() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setViews(land.getViews() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setViews(chalet.getViews() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setViews(shopRent.getViews() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setViews(shopSell.getViews() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setViews(officeRent.getViews() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setViews(officeSell.getViews() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			default:

				return Response.status(Status.BAD_REQUEST).entity(Constants.POST_TYPE_NOT_SUPPORTED).build();

			}
		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	// to use by kassem
	public Response updateCallPost(Long id, String postType) {
		try {

			switch (postType) {
			case "APPRATMENT_RENT":
				AppratmentRent appratmentRent = appratmentRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentRent != null) {
					appratmentRent.setNumberOfCall(appratmentRent.getNumberOfCall() + 1);
					appratmentRentFacade.save(appratmentRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setNumberOfCall(appratmentSell.getNumberOfCall() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setNumberOfCall(land.getNumberOfCall() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setNumberOfCall(chalet.getNumberOfCall() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setNumberOfCall(shopRent.getNumberOfCall() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setNumberOfCall(shopSell.getNumberOfCall() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "STORE_HOUSE_RENT":

				StoreHouseRent storeHouseRent = storeHouseRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (storeHouseRent != null) {
					storeHouseRent.setNumberOfCall(storeHouseRent.getNumberOfCall() + 1);
					storeHouseRentFacade.save(storeHouseRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "STORE_HOUSE_SELL":

				StoreHouseSell storeHouseSell = storeHouseSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (storeHouseSell != null) {
					storeHouseSell.setNumberOfCall(storeHouseSell.getNumberOfCall() + 1);
					storeHouseSellFacade.save(storeHouseSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setNumberOfCall(officeRent.getNumberOfCall() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setNumberOfCall(officeSell.getNumberOfCall() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			default:

				return Response.status(Status.BAD_REQUEST).entity(Constants.POST_TYPE_NOT_SUPPORTED).build();

			}
		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response updatePostLike(Long id, String postType) {
		try {

			switch (postType) {
			case "APPRATMENT_RENT":
				AppratmentRent appratmentRent = appratmentRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentRent != null) {
					appratmentRent.setLiked(appratmentRent.getLiked() + 1);
					appratmentRentFacade.save(appratmentRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setLiked(appratmentSell.getLiked() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setLiked(land.getLiked() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setLiked(chalet.getLiked() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setLiked(shopRent.getLiked() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setLiked(shopSell.getLiked() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "STORE_HOUSE_RENT":

				StoreHouseRent storeHouseRent = storeHouseRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (storeHouseRent != null) {
					storeHouseRent.setLiked(storeHouseRent.getLiked() + 1);
					storeHouseRentFacade.save(storeHouseRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "STORE_HOUSE_SELL":

				StoreHouseSell storeHouseSell = storeHouseSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (storeHouseSell != null) {
					storeHouseSell.setLiked(storeHouseSell.getLiked() + 1);
					storeHouseSellFacade.save(storeHouseSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setLiked(officeRent.getLiked() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setLiked(officeSell.getLiked() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
				}

			default:

				return Response.status(Status.BAD_REQUEST).entity(Constants.POST_TYPE_NOT_SUPPORTED).build();

			}
		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response findPosts(Long userId, String postType, int minPrice, int maxPrice, Long villageId, int page,
			int size, int bedRoom, boolean bedRoomEq, int bathRoom, boolean bathRoomEq, Long districtId,
			Long governorateId , String exchangeRealEstateTypeString ) {

		try {
			Village village = null;
			User user = null;
			District district = null;
			Governorate governorate = null;
			ExchangeRealEstateType exchangeRealEstateType = null ;
			if (userId != null && userId > 0) {
				user = userFacade.findWithExcption(userId);
			}
			
			if(exchangeRealEstateTypeString!=null) {
				if(exchangeRealEstateTypeString.equals(ExchangeRealEstateType.BUY.toString())) {
					exchangeRealEstateType = ExchangeRealEstateType.BUY;
				}else {
					if(exchangeRealEstateTypeString.equals(ExchangeRealEstateType.RENT.toString())) {
						exchangeRealEstateType = ExchangeRealEstateType.RENT;
				}
			}

			// check priority
			// villageId
			// districtId
			// governorateId
			if (villageId != null && villageId > 0) {
				village = villageFacade.findWithExcption(villageId);

			} else if (districtId != null && villageId > 0) {
				district = districtFacade.findWithExcption(villageId);

			} else if (governorateId != null && governorateId > 0) {
				governorate = governorateFacade.findWithExcption(governorateId);

			}
			}

			AtomicLong totalResults = new AtomicLong();
			List<RealEstate> realEstate = restateFacade.findAllRealSatateWithFilter(user, postType, minPrice, maxPrice,
					village, page, size, totalResults, bedRoom, bedRoomEq, bathRoom, bathRoomEq, district, governorate , exchangeRealEstateType);

			PaginationResponse<RealEstate> response = new PaginationResponse<>();
			response.setPage(page);
			response.setSize(size);
			response.setTotalCount(totalResults.get());
			response.setData(realEstate);

			return Response.status(Status.OK).entity(Utils.objectToString(response)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	// HERE
	public Response findChalet(Long userId, int minPrice, int maxPrice, Long villageId, int size, int page,
			Boolean pool, Boolean chimney, Long governorateId, Long districtId) {

		try {
			Village village = null;
			User user = null;
			District district = null;
			Governorate governorate = null;
			if (userId != null && userId > 0) {
				user = userFacade.findWithExcption(userId);
			}

			// check priority
			// villageId
			// districtId
			// governorateId
			if (villageId != null && villageId > 0) {
				village = villageFacade.findWithExcption(villageId);

			} else if (districtId != null && villageId > 0) {
				district = districtFacade.findWithExcption(villageId);

			} else if (governorateId != null && governorateId > 0) {
				governorate = governorateFacade.findWithExcption(governorateId);

			}

			AtomicLong totalResults = new AtomicLong();
			List<Chalet> list = chaletFacade.findAllChaletWithFilter(user, village, page, size, totalResults, district,
					governorate, minPrice, maxPrice, pool, chimney);

			PaginationResponse<Chalet> response = new PaginationResponse<>();
			response.setPage(page);
			response.setSize(size);
			response.setTotalCount(totalResults.get());
			response.setData(list);

			return Response.status(Status.OK).entity(Utils.objectToString(response)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	public Response findPostById(Long id) {

		try {
			RealEstate realEstate = restateFacade.findWithQueryHint(id);
			return Response.status(Status.OK).entity(Utils.objectToString(realEstate)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	public Response updateChaletViews(long id) {
		try {
			Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

			if (chalet != null) {
				chalet.setViews(chalet.getViews() + 1);
				chaletFacade.save(chalet);
				return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
			} else {
				return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	public Response updateChaletCall(long id) {
		try {
			Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

			if (chalet != null) {
				chalet.setNumberOfCall(chalet.getNumberOfCall() + 1);
				chaletFacade.save(chalet);
				return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
			} else {
				return Response.status(Status.NO_CONTENT).entity("ID_NOT_EXISTS").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

}
