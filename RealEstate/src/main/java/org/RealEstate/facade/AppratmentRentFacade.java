package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentRent;

@Stateless

public class AppratmentRentFacade extends AbstractFacade<AppratmentRent> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppratmentRentFacade() {
		super(AppratmentRent.class);
	}

	public AppratmentRent mangmentSavePost(AppratmentRent obj) throws Exception {
		return this.save(obj);

	}
}
