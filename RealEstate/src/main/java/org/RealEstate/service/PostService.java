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
import org.RealEstate.enumerator.PostStatus;
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
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
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
			addCommonsField(appratmentRent);
			checkPostConstraintFields(appratmentRent);
			appratmentRent.setUser(user);
			return appratmentRentFacade.mangmentSavePost(appratmentRent, inputParts);
		case "APPRATMENT_SELL":

			AppratmentSell appratmentSell = Utils.getObjectFromString(jsonString, AppratmentSell.class);
			addCommonsField(appratmentSell);

			checkPostConstraintFields(appratmentSell);
			appratmentSell.setUser(user);

			return appratmentSellFacade.mangmentSavePost(appratmentSell, inputParts);
		case "LAND":

			Land land = Utils.getObjectFromString(jsonString, Land.class);
			addCommonsField(land);
			land.setUser(user);

			checkPostConstraintFields(land);
			return landFacade.mangmentSavePost(land, inputParts);
		case "CHALET":

			Chalet chalet = Utils.getObjectFromString(jsonString, Chalet.class);
			addCommonsFieldChalet(chalet);
			checkChaletConstraintFields(chalet);
			chalet.setUser(user);

			return chaletFacade.mangmentSavePost(chalet, inputParts);
		case "SHOP_RENT":

			ShopRent shopRent = Utils.getObjectFromString(jsonString, ShopRent.class);
			addCommonsField(shopRent);
			checkPostConstraintFields(shopRent);
			shopRent.setUser(user);

			return shopRentFacade.mangmentSavePost(shopRent, inputParts);
		case "SHOP_SELL":

			ShopSell shopSell = Utils.getObjectFromString(jsonString, ShopSell.class);
			addCommonsField(shopSell);
			checkPostConstraintFields(shopSell);
			shopSell.setUser(user);

			return shopSellFacade.mangmentSavePost(shopSell, inputParts);
		case "OFFICE_RENT":

			OfficeRent officeRent = Utils.getObjectFromString(jsonString, OfficeRent.class);
			addCommonsField(officeRent);
			checkPostConstraintFields(officeRent);
			officeRent.setUser(user);

			return officeRentFacade.mangmentSavePost(officeRent, inputParts);
		case "OFFICE_SELL":
			OfficeSell officeSell = Utils.getObjectFromString(jsonString, OfficeSell.class);
			addCommonsField(officeSell);
			checkPostConstraintFields(officeSell);
			officeSell.setUser(user);

			return officeSellFacade.mangmentSavePost(officeSell, inputParts);
		default:

			return null;
		}

	}

	private User checkUserConstraint(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		Long id = jsonNode.get("userId").asLong();
		User user = userFacade.find(id);
		if (user == null) {
			throw new Exception(Constants.USER_NOT_EXISTS);
		} else {

			Long nbOfPost = restateFacade.findUserCountPost(user.getId());

			if (nbOfPost >= appSinglton.getFreeNbOfPost()) {
				throw new Exception("EXCEEDED_POST_LIMIT");
			}

			return user;
		}

	}

	private void checkVillageExist(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonString);
		JsonNode villageJson = jsonNode.get("village");
		Village village = Utils.getObjectFromString(villageJson.toString(), Village.class);
		villageFacade.findWithExcption(village.getId());
	
		

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

		// TODO
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
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setViews(appratmentSell.getViews() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setViews(land.getViews() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setViews(chalet.getViews() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setViews(shopRent.getViews() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setViews(shopSell.getViews() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setViews(officeRent.getViews() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setViews(officeSell.getViews() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
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
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setNumberOfCall(appratmentSell.getNumberOfCall() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setNumberOfCall(land.getNumberOfCall() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setNumberOfCall(chalet.getNumberOfCall() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setNumberOfCall(shopRent.getNumberOfCall() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setNumberOfCall(shopSell.getNumberOfCall() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setNumberOfCall(officeRent.getNumberOfCall() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setNumberOfCall(officeSell.getNumberOfCall() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
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
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "APPRATMENT_SELL":

				AppratmentSell appratmentSell = appratmentSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (appratmentSell != null) {
					appratmentSell.setLiked(appratmentSell.getLiked() + 1);
					appratmentSellFacade.save(appratmentSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();

				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();

				}

			case "LAND":

				Land land = landFacade.findWithLockPessimisticWriteWithoutException(id);

				if (land != null) {
					land.setLiked(land.getLiked() + 1);
					landFacade.save(land);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "CHALET":

				Chalet chalet = chaletFacade.findWithLockPessimisticWriteWithoutException(id);

				if (chalet != null) {
					chalet.setLiked(chalet.getLiked() + 1);
					chaletFacade.save(chalet);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_RENT":

				ShopRent shopRent = shopRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopRent != null) {
					shopRent.setLiked(shopRent.getLiked() + 1);
					shopRentFacade.save(shopRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "SHOP_SELL":

				ShopSell shopSell = shopSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (shopSell != null) {
					shopSell.setLiked(shopSell.getLiked() + 1);
					shopSellFacade.save(shopSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_RENT":

				OfficeRent officeRent = officeRentFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeRent != null) {
					officeRent.setLiked(officeRent.getLiked() + 1);
					officeRentFacade.save(officeRent);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
				}

			case "OFFICE_SELL":

				OfficeSell officeSell = officeSellFacade.findWithLockPessimisticWriteWithoutException(id);

				if (officeSell != null) {
					officeSell.setLiked(officeSell.getLiked() + 1);
					officeSellFacade.save(officeSell);
					return Response.status(Status.ACCEPTED).entity("SUCCESS").build();
				} else {
					return Response.status(Status.NOT_FOUND).entity("ID_NOT_EXISTS").build();
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
			Long governorateId) {

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
			List<RealEstate> realEstate = restateFacade.findAllRealSatateWithFilter(user, postType, minPrice, maxPrice,
					village, page, size, totalResults, bedRoom, bedRoomEq, bathRoom, bathRoomEq, district, governorate);

			PaginationResponse<RealEstate> response = new PaginationResponse<>();
			response.setPage(page);
			response.setSize(size);
			response.setTotalCount(totalResults.get());
			response.setData(realEstate);
			// System.out.println(realEstate);
			// realEstate.get(0).getPostType()
			// AppratmentRent t = (AppratmentRent) realEstate.get(0)
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
}
