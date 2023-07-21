package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;

@ViewScoped
@Named
public class UpdateUserInformationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UserFacade userFacade;

	@EJB
	private UserService userService;

	@Inject
	private HttpServletRequest request;

	private User user;

	@PostConstruct
	public void init() {
		HttpSession session = request.getSession(true);

		user = (User) session.getAttribute(Constants.USER_SESSION);

		if (user == null) {

		}
	}

	public void updateUserInformation() {

		Response r = userService.updateUserInforamtion(user);
		if (r.getStatus() == Status.ACCEPTED.getStatusCode()) {
			CommonUtility.addMessageToFacesContext(Utility.getMessage("update_sucess"), "success");
		} else {
			CommonUtility.addMessageToFacesContext(r.getEntity().toString(), "error");

		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
