package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.facade.AdsFacade;
import org.RealEstate.utils.Utils;

@Path("/ads")
public class AdsMangment {

	@EJB
	private AdsFacade adsFacade;

	@GET
	@Path("/v1")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllGovernorate() {

		try {

			return Response.status(Status.OK).entity(Utils.listToString(adsFacade.findAll())).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

}
