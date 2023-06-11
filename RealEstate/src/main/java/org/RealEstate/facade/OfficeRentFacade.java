package org.RealEstate.facade;



import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.OfficeRent;

@Stateless

public class OfficeRentFacade extends AbstractFacade<OfficeRent> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfficeRentFacade() {
		super(OfficeRent.class);
	}
	
	public OfficeRent preSave(OfficeRent obj) {
		return null ;
	}
}

