package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.model.Governorate;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;

@Stateless

public class GovernorateFacade extends AbstractFacade<Governorate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GovernorateFacade() {
		super(Governorate.class);
	}



}
