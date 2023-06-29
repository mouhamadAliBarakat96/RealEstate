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
			User user = userFacade.findUserByUserNameAndPassword(userName, passowrd);

			if (user == null) {
				// user wrong
			}

			// else true
			HttpSession session = request.getSession(true);
			session.setAttribute(Constants.USER_SESSION, user);
			Faces.redirect("URL LAWEN BADO YROUH");

		} catch (Exception e) {

		}

	}

	// CODE TO KASSSEM
	// hayda btst3mlo b safhat el b2ye
	public void getUser() {
		HttpSession session = request.getSession(true);

		User user = (User) session.getAttribute(Constants.USER_SESSION);

		// bt3ml hon check iza user == null aw 3ade

		// iza null yaane mano mswe login
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
