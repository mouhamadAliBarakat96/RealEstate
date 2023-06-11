package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;

@Stateless

public class ShopRentFacade extends AbstractFacade<ShopRent> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShopRentFacade() {
		super(ShopRent.class);
	}
	
	public ShopRent preSave(ShopRent obj) {
		return null ;
	}
}
