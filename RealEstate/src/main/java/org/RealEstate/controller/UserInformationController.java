package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.utils.CommonUtility;
import org.omnifaces.cdi.Param;

@ViewScoped
@Named
public class UserInformationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	@Inject
	private UserFacade userFacade;

	private User user;

	@PostConstruct
	public void init() {
		if (id > 0) {

			user = userFacade.find(id);
		}
	}

	public void save() {

		try {
			user = userFacade.save(user);
			CommonUtility.addMessageToFacesContext(" save_success ", "success");
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext(" error_on_save ", "error");

		}

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
