package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentSell;



@Stateless

public class AppratmentSellFacade extends AbstractFacade<AppratmentSell> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppratmentSellFacade() {
		super(AppratmentSell.class);
	}
	
	public AppratmentSell mangmentSavePost(AppratmentSell obj) {
		return null ;
	}
}
