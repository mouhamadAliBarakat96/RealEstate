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
import org.RealEstate.utils.CommonUtility;

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

	public void remove(Governorate item) {
		try {
			governorateFacade.remove(item);
			pageItems = governorateFacade.findAll();
			CommonUtility.addMessageToFacesContext("Delete Success ", "success");

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext("Error on delete caused by childs ", "error");

		}
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
