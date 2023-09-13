package org.RealEstate.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.dto.PaginationResponse;
import org.RealEstate.enumerator.UserCategory;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Stateless
public class UserService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UserFacade userFacade;

	@EJB
	private RealEstateFacade restateFacade;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public Response updateUserInforamtion(User user) {

		try {
			// validite phone number

			if (user.getId() <= 0) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.USER_ID_SHOULD_BE_GREATER_THAN_ZERO)
						.build();

			}

			if (StringUtils.isBlank(user.getPhoneNumber()) || !Utils.validatePhoneNumber(user.getPhoneNumber())) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.PHONE_NUMBER_NOT_CORRECT).build();

			}

			if (StringUtils.isBlank(user.getLastName()) || StringUtils.isBlank(user.getFirstName())
					|| StringUtils.isBlank(user.getMiddleName()) || StringUtils.isBlank(user.getUserName())) {

				return Response.status(Status.BAD_REQUEST)
						.entity(Constants.USER_NAME_FIRST_NAME_MIDDLE_NAME_LAST_NAME_SHOULD_NOT_BE_EMPTY).build();

			}

			User orginUser = userFacade.find(user.getId());
			user.setPassowrd(orginUser.getPassowrd());
			user.setFreezed(orginUser.isFreezed());
			user.setFbId(orginUser.getFbId());
			user.setBroker(orginUser.isBroker());
			user.setUserCategory(orginUser.getUserCategory());
			if (!orginUser.getUserName().equals(user.getUserName())) {

				User userFinded = userFacade.findUserByUserName(user.getUserName());

				if (userFinded != null) {
					return Response.status(Status.BAD_REQUEST).entity(Constants.USER_NAME_SHOULD_BE_UNIQUE).build();
				}
			}

			userFacade.save(user);
			return Response.status(Status.ACCEPTED).entity(Utils.objectToString(user)).build();

		}

		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	public Response createUser(User user) {

		try {

			// check fb id nt null

			/**
			 * This Code is not used now TODO ACTIVE IT LATER
			 */
			/*
			 * if (user.getFbId() == null || StringUtils.isBlank(user.getFbId())) { return
			 * Response.status(Status.BAD_REQUEST).entity(Constants.
			 * FACEBOOK_ID_SHOUD_NOT_BE_NULL).build();
			 * 
			 * }
			 */

			user.setFbId(Utils.generateRandomStringBasedOnTime());

			// validite phone number

			if (StringUtils.isBlank(user.getPhoneNumber()) || !Utils.validatePhoneNumber(user.getPhoneNumber())) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.PHONE_NUMBER_NOT_CORRECT).build();

			}

			if (StringUtils.isBlank(user.getPassowrd()) || StringUtils.isBlank(user.getLastName())
					|| StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getMiddleName())
					|| StringUtils.isBlank(user.getUserName())) {

				return Response.status(Status.BAD_REQUEST)
						.entity(Constants.USER_NAME_FIRST_NAME_MIDDLE_NAME_LAST_NAME_SHOULD_NOT_BE_EMPTY).build();

			}

			// check if username is unique

			User userFinded = userFacade.findUserByUserName(user.getUserName());

			if (userFinded != null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.USER_NAME_SHOULD_BE_UNIQUE).build();
			}
			// check if fbId is unique
			userFinded = userFacade.findUserByFbId(user.getFbId());

			if (userFinded != null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.FACEBOOK_USER_HAVE_ACCOUNT).build();
			}

			user.setUserCategory(UserCategory.REGULAR);
			user.setBroker(false);
			user = userFacade.save(user);

			return Response.status(Status.CREATED).entity(Utils.objectToString(user)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	public Response login(String userName, String password) {

		try {
			User user = userFacade.findUserByUserNameAndPassword(userName, password);
			if (user == null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.USER_NAME_OR_PASSWORD_INVALID).build();

			}
			return Response.status(Status.OK).entity(Utils.objectToString(user)).build();

		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

	public Response loginFb(String fbId) {

		try {
			User user = userFacade.findUserByFbId(fbId);

			if (user == null) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.FACEBOOK_ID_INVALID).build();

			}

			return Response.status(Status.OK).entity(Utils.objectToString(user)).build();

		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

	public Response find(Long userId) {

		try {
			User user = userFacade.find(userId);
			userFacade.getEm().detach(user);
			if (!user.isShowProfilePicture()) {
				user.setProfileImageUrl("");
			}
			return Response.status(Status.OK).entity(Utils.objectToString(user)).build();

		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

	public Response changeProfilePictureApi(Long userId, MultipartFormDataInput input) {

		try {
			// form contain image
			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

			List<InputPart> inputParts = uploadForm.get("file");

			if (inputParts == null || inputParts.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.AT_LAST_ONE_IMAGE_REQUIRED).build();

			}

			if (inputParts.size() > 1) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.ONLY_ONE_IMAGE_ALLOWED_FOR_USER).build();

			}

			if (changeProfilePicture(userId, inputParts.get(0))) {
				return Response.status(Status.OK).entity("DONE").build();

			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("FAILER").build();

			}

		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

	public boolean changeProfilePicture(Long userId, InputPart inputPart) throws Exception {

		// find user
		User user = userFacade.findWithExcption(userId);
// yaane hayde mana awal mara  w 3am yaaml update

		if (user.getProfileImageUrl() != null) {
			user.setShowProfilePicture(false);
		} else {
			// hayde yaane awal mara
			user.setShowProfilePicture(true);

		}

		String imageUrl = uploadImagesMultiPart.uploadImageUserProfile(inputPart);

		user.setProfileImageUrl(imageUrl);
		userFacade.save(user);
		return true;
	}

	public Response findAllPagination(int page, int size) {
		try {
			int offset = (page - 1) * size;
			int totalCount = userFacade.count();
			List<User> users = userFacade.findPagintion(size, offset);
			PaginationResponse<User> response = new PaginationResponse<>();
			response.setPage(page);
			response.setSize(size);
			response.setTotalCount(totalCount);
			response.setData(users);

			return Response.status(Status.OK).entity(Utils.objectToString(response)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
	}

}
