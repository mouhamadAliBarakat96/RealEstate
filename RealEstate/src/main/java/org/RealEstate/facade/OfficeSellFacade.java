package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.OfficeSell;



@Stateless

public class OfficeSellFacade extends AbstractFacade<OfficeSell> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfficeSellFacade() {
		super(OfficeSell.class);
	}
	
	public OfficeSell preSave(OfficeSell obj) {
		return null ;
	}
}

