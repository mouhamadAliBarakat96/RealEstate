package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.model.Governorate;

@ViewScoped
@Named
public class GovernorateListController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private GovernorateFacade governorateFacade;
	private List<Governorate> pageItems = new ArrayList<>();
	private List<Governorate> filteredList;

	@PostConstruct
	public void init() {
		pageItems = governorateFacade.findAll();
	}

	public List<Governorate> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Governorate> pageItems) {
		this.pageItems = pageItems;
	}

	public List<Governorate> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<Governorate> filteredList) {
		this.filteredList = filteredList;
	}

}
