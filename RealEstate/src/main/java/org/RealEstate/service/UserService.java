package org.RealEstate.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.dto.PaginationResponse;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.facade.VillageFacade;
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

	public Response createUser(User user) {

		try {
			// validite phone number

			if (!Utils.validatePhoneNumber(user.getPhoneNumber())) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.PHONE_NUMBER_NOT_CORRECT).build();

			}

			if (StringUtils.isBlank(user.getLastName()) || StringUtils.isBlank(user.getFirstName())
					|| StringUtils.isBlank(user.getMiddleName()) || StringUtils.isBlank(user.getUserName())) {

				return Response.status(Status.BAD_REQUEST)
						.entity(Constants.USER_NAME_FIRST_NAME_MIDDLE_NAME_LAST_NAME_SHOULD_NOT_BE_EMPTY).build();

			}

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

		String imageUrl = uploadImagesMultiPart.uploadImagePost(inputPart);
		user.setShowProfilePicture(false);
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
