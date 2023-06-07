package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.Governorate;

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
