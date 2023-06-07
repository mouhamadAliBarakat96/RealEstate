package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.Village;

@ViewScoped
@Named
public class VillageController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private VillageFacade villageFacade;

	private Village village = new Village();

	@PostConstruct
	public void init() {

	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

}
