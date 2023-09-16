package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.facade.ContactUsFacade;
import org.RealEstate.model.ContactUs;
import org.RealEstate.model.Governorate;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class ContactUsListController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private ContactUsFacade contactUsFacade;

	private List<ContactUs> pageItems = new ArrayList<>();
	private List<ContactUs> filteredList;

	@PostConstruct
	public void init() {
		pageItems = contactUsFacade.findAll();
	}

	public void remove(ContactUs item) {
		try {
			contactUsFacade.remove(item);
			pageItems = contactUsFacade.findAll();
			CommonUtility.addMessageToFacesContext("Delete Success ", "success");

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext("Error on delete caused by childs ", "error");

		}
	}

	public List<ContactUs> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<ContactUs> pageItems) {
		this.pageItems = pageItems;
	}

	public List<ContactUs> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<ContactUs> filteredList) {
		this.filteredList = filteredList;
	}

}
