package org.RealEstate.restService;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.facade.ContactUsFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.ContactUs;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;

@Path("/contact-us")

public class ContactUsMangment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private ContactUsFacade contactUsFacade;

	@EJB
	private UserFacade userFacade;

	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response saveContactUs(ContactUs contactUs) {
		try {
			return saveContactUsMangment(contactUs);
		} catch (Exception e) {
			return analyzeException(e);

		}

	}

	private Response saveContactUsMangment(ContactUs contactUs) throws Exception {

		if(contactUs.getUser() !=null) {
			User user = userFacade.findWithExcption(contactUs.getUser().getId());
			contactUs.setUser(user);
			
		}

		
		contactUs = contactUsFacade.save(contactUs);

		return Response.status(Status.CREATED).entity(Utils.objectToString(contactUs)).build();

	}

	private Response analyzeException(Exception e) {
		e.printStackTrace();
		if (e instanceof EJBException)

		{
			if (e.getCause() != null) {
				if (e.getCause() instanceof ConstraintViolationException) {
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

				} else {
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity(Constants.USER_NAME_SHOULD_BE_UNIQUE)
							.build();

				}

			}

		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

	}

}
