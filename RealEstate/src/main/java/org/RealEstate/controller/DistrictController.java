package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.model.District;

@ViewScoped
@Named
public class DistrictController  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@EJB
	private DistrictFacade  districtFacade  ;
	
	private District district = new District()  ;
	
	@PostConstruct
	public void init() {
		
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}
	
	

}
