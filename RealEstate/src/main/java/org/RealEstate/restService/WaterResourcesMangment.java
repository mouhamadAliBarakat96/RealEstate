package org.RealEstate.restService;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.enumerator.WaterResources;
import org.RealEstate.utils.Utils;

@Path("/water-resources")
public class WaterResourcesMangment {
	@GET
	@Path("/v1/")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAll() {

		try {
			return Response.status(Status.OK).entity(Utils.listToString(Arrays.asList(WaterResources.values())))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}
}
