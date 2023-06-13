package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.RealEstate.facade.VillageFacade;

@Path("/village")
public class VillageMangment {
	@EJB
	private VillageFacade villageFacade;

	@GET
	@Path("/v1/{districtId}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllGovernorate(@PathParam("districtId") Long districtId) {

		return villageFacade.findByDisctrictForApi(districtId);

	}
}
