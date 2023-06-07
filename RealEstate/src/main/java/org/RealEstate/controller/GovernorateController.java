package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.model.Governorate;

@ViewScoped
@Named
public class GovernorateController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private GovernorateFacade governorateFacade;
	private Governorate governorate = new Governorate();

	@PostConstruct
	public void init() {

	}

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
	}
	
	

}
