package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.Ads;

@Stateless
public class AdsFacade extends AbstractFacade<Ads> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdsFacade() {
		super(Ads.class);
	}
}
