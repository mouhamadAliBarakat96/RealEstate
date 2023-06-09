package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Village;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class VillageListController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private DistrictFacade districtFacade;
	@EJB
	private VillageFacade villageFacade;

	private List<Village> pageItems = new ArrayList<>();
	private List<Village> filteredList;
	private List<District> listDistrict = new ArrayList<>();

	@PostConstruct
	public void init() {
		pageItems = villageFacade.findAll();

		listDistrict = districtFacade.findAll();

	}

	public void remove(Village item) {
		try {
			villageFacade.remove(item);
			pageItems = villageFacade.findAll();
			CommonUtility.addMessageToFacesContext("Delete Success ", "success");

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext("Error on delete caused by childs ", "error");

		}
	}

	public List<Village> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Village> pageItems) {
		this.pageItems = pageItems;
	}

	public List<Village> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<Village> filteredList) {
		this.filteredList = filteredList;
	}

	public List<District> getListDistrict() {
		return listDistrict;
	}

	public void setListDistrict(List<District> listDistrict) {
		this.listDistrict = listDistrict;
	}

}
