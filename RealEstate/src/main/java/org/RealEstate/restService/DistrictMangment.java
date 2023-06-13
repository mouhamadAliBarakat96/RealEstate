package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.RealEstate.facade.DistrictFacade;

@Path("/district")

public class DistrictMangment {

	@EJB
	private DistrictFacade districtFacade;

	@GET
	@Path("/v1/{governorateId}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllGovernorate(@PathParam("governorateId") Long governorateId) {

		return districtFacade.findByGovernorateForApi(governorateId);

	}

}
