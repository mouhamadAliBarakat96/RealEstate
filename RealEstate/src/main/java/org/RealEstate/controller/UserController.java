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
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Utils;

@ViewScoped
@Named
public class UserController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private UserFacade userFacade;

	private List<User> pageItems = new ArrayList<>();
	private List<User> filteredList;

	@PostConstruct
	public void init() {
		pageItems = userFacade.findAll();

	}

	public void freezUserMangment(User user) {

		try {
			user.setFreezed(!user.isFreezed());
			userFacade.save(user);
			pageItems = userFacade.findAll();
			CommonUtility.addMessageToFacesContext("Update successfully   ", "success");
		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext("error on Update   ", "error");
			e.printStackTrace();
		}

	}

	public void resetPassword(User user) {

		try {
			String newPasswrod = "123456";

			String passwordEncrypted = Utils.sha256(newPasswrod);

			user.setPassowrd(passwordEncrypted);
			userFacade.save(user);

			CommonUtility.addMessageToFacesContext("Update successfully   ", "success");
		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext("error on Update   ", "error");
			e.printStackTrace();
		}

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

}
