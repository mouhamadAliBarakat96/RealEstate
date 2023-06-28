package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;

@ViewScoped
@Named
public class UserProfilePictureList implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private UserFacade userFacade;

	private List<User> pageItems = new ArrayList<>();
	private List<User> filteredList;

	private boolean param;

	@PostConstruct
	public void init() {
		pageItems = userFacade.findUserProfilePicture(param);

	}

	public void search() {
		pageItems = userFacade.findUserProfilePicture(param);

	}

	public List<User> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<User> pageItems) {
		this.pageItems = pageItems;
	}

	public List<User> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<User> filteredList) {
		this.filteredList = filteredList;
	}

	public boolean isParam() {
		return param;
	}

	public void setParam(boolean param) {
		this.param = param;
	}

}
