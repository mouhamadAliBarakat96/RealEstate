package org.RealEstate.facade;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.utils.Constants;

@Stateless

public class GovernorateFacade extends AbstractFacade<Governorate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GovernorateFacade() {
		super(Governorate.class);
	}
	public Governorate findWithExcption(Long id) throws Exception {
		Governorate Governorate = this.find(id);
		if(Governorate !=null) {
			return Governorate;
		}
		else {
			throw new Exception(Constants.GOVERNORTE_NOT_EXISTS);
		}
	}



}
