package org.RealEstate.controller;


import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class LoginAdmin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	String originalURI;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		originalURI = (String) context.getExternalContext().getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		HttpSession session = request.getSession(true);

		if (originalURI == null) {

			originalURI = (String) session.getAttribute("url");
			if (originalURI == null) {
				originalURI = "views/template/homeIndex.xhtml";
			}
		}

		else {
			try {
				session.setAttribute("url", originalURI);

				Faces.redirect("admin-login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void login() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		try {

			request.login(userName, password);
			Faces.redirect(originalURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
