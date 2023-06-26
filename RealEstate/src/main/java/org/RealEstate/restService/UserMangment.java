package org.RealEstate.restService;

import java.io.InputStream;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.model.User;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.Constants;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("/user")
public class UserMangment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UserService userService;

	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response savePost(User user) {
		try {
			return userService.createUser(user);
		} catch (Exception e) {
			return analyzeException(e);

		}

	}

//MultipartFormDataInput
	@PUT
	@Path("/v1/change-profile-picture/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response changeProfilePicture(@PathParam("id") Long id, @MultipartForm MultipartFormDataInput in) {

		return userService.changeProfilePictureApi(id, in);
 
	}

	@GET
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllUser(@QueryParam("page") int page, @QueryParam("size") int size) {
		try {
			return userService.findAllPagination(page, size);
		} catch (Exception e) {
			return analyzeException(e);

		}

	}

	@GET
	@Path("/v1/login")

	public Response login(@QueryParam("userName") String userName, @QueryParam("password") String password) {
		try {
			return userService.login(userName, password);
		} catch (Exception e) {
			return analyzeException(e);

		}

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
