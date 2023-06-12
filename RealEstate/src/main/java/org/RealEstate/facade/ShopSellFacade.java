package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.ShopSell;



@Stateless

public class ShopSellFacade extends AbstractFacade<ShopSell> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShopSellFacade() {
		super(ShopSell.class);
	}
	
	public ShopSell mangmentSavePost(ShopSell obj) throws Exception {
		return this.save(obj);
	}
}
