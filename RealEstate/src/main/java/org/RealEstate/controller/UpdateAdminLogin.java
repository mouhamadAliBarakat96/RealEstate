package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.utils.Utility;
import org.apache.commons.lang3.StringUtils;

@ViewScoped
@Named
public class UpdateAdminLogin implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private UserFacade userFacade;

	private String username = "mojtaba";

	private String password;

	private String confirmPassword;

	@PostConstruct
	public void init() {

	}

	public void save() {
		try {
			if (StringUtils.isNoneBlank(password) && StringUtils.isNoneBlank(confirmPassword)) {
				if (password.equals(confirmPassword)) {
					userFacade.updateUserPassword(password);
					Utility.addMessage("password changed!");
				} else {
					Utility.addMessage("please check the confirmation password");
				}
			} else {
				Utility.addMessage("please check the fields to not be empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
