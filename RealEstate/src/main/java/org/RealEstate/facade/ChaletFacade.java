package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.Chalet;


@Stateless

public class ChaletFacade extends AbstractFacade<Chalet> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChaletFacade() {
		super(Chalet.class);
	}
	
	public Chalet mangmentSavePost(Chalet obj) throws Exception {
		return this.save(obj);
	}
}
