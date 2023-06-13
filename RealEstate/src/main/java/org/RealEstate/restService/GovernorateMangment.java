package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.RealEstate.facade.GovernorateFacade;

@Path("/governorate")
public class GovernorateMangment {

	@EJB
	private GovernorateFacade governorateFacade;

	@GET
	@Path("/v1")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllGovernorate() {

		return governorateFacade.findAllForApi();

	}
}
