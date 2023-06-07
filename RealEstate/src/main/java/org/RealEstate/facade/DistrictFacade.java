package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.RealEstate.model.District;

@Stateless

public class DistrictFacade extends AbstractFacade<District> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistrictFacade() {
		super(District.class);
	}

}