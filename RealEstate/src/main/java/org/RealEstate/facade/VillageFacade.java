package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Village;
import org.RealEstate.utils.Utils;

@Stateless

public class VillageFacade extends AbstractFacade<Village> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VillageFacade() {
		super(Village.class);
	}

	public Response findByDisctrictForApi(Long districtId) {
		try {
			List<Village> list = getEntityManager().createNamedQuery(Village.FING_BY_DISTRICT, Village.class)
					.setParameter("districtId", districtId).getResultList();
			return Response.status(Status.OK).entity(Utils.listToString(list)).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

}
