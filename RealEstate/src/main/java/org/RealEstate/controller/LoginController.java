package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class LoginController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName = "";
	// passowrd as sha256
	private String passowrd = "";

	@EJB
	private UserFacade userFacade;
	@Inject
	private HttpServletRequest request;

	@PostConstruct
	public void init() {

	}

	public void login() {
		try {
			String hashPass = Utility.hashPassword(passowrd);
			User user = userFacade.findUserByUserNameAndPassword(userName, hashPass);

			if (user == null) {
				Utility.addErrorMessage("name_or_pass_wrong");
				return;
			}
			HttpSession session = request.getSession(true);
			session.setAttribute(Constants.USER_SESSION, user);
			Faces.redirect("/index.xhtml");
		} catch (Exception e) {

		}

	}

	public void logout() {

		try {
			HttpSession session = request.getSession(true);
			session.removeAttribute(Constants.USER_SESSION);
			Faces.redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassowrd() {
		return passowrd;
	}

	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}

}
