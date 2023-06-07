package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.RealEstate.model.Governorate;
import org.RealEstate.model.Village;

@Stateless

public class VillageFacade  extends AbstractFacade<Village> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VillageFacade() {
		super(Village.class);
	}



}


