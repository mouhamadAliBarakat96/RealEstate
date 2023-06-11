package org.RealEstate.facade;


import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Land;
import org.RealEstate.model.ShopRent;

@Stateless

public class LandFacade extends AbstractFacade<Land> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LandFacade() {
		super(Land.class);
	}
	
	
	

	public AppratmentSell mangmentSavePost(Land obj) {
		return null ;
	}
}
