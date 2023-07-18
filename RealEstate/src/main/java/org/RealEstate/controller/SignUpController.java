package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class SignUpController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user = new User();;
	@EJB
	private UserService userService;

	@PostConstruct
	public void init() {

	}

	public void save() {
		try {
			Response r = userService.createUser(user);

			if (r.getStatus() == Status.CREATED.getStatusCode()) {

			} else {

				CommonUtility.addMessageToFacesContext(r.getEntity().toString(), "error");

			}

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext(e.getCause().getMessage(), "error");

		}

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
