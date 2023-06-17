package org.RealEstate.service;

import java.io.Serializable;
import java.util.List;

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
