package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class DistrictListController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private DistrictFacade districtFacade;
	private List<District> pageItems = new ArrayList<>();
	private List<District> filteredList;

	@EJB
	private GovernorateFacade governorateFacade;

	private List<Governorate> governorateList = new ArrayList<>();

	@PostConstruct
	public void init() {
		pageItems = districtFacade.findAll();
		governorateList = governorateFacade.findAll();
	}

	public void remove(District item) {
		try {
			districtFacade.remove(item);
			pageItems = districtFacade.findAll();
			CommonUtility.addMessageToFacesContext("Delete Success ", "success");

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext("Error on delete caused by childs ", "error");

		}
	}

	public List<District> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<District> pageItems) {
		this.pageItems = pageItems;
	}

	public List<District> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<District> filteredList) {
		this.filteredList = filteredList;
	}

	public List<Governorate> getGovernorateList() {
		return governorateList;
	}

	public void setGovernorateList(List<Governorate> governorateList) {
		this.governorateList = governorateList;
	}

}
