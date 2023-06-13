package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.model.District;
import org.RealEstate.utils.Utils;

@Stateless

public class DistrictFacade extends AbstractFacade<District> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistrictFacade() {
		super(District.class);
	}

	public Response findByGovernorateForApi (Long governorateId){
		try {
			List<District> list = getEntityManager().createNamedQuery(District.FING_BY_GOVERNORATE, District.class)
					.setParameter("governorateId", governorateId).getResultList();
			return Response.status(Status.OK).entity(Utils.listToString(list)).build();

			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
		
	}

	
}