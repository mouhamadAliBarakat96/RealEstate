package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.apache.commons.lang3.StringUtils;
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

	public void loginFb() {

		try {
			String fbId = null;
			Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			User user;
			if (requestParamMap.containsKey("fbId")) {
				fbId = requestParamMap.get("fbId");

				user = userFacade.findUserByFbId(fbId);

				if (user == null) {
					Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
					flash.put("type", "error");
					flash.put("message", "Facebook dosent have Account here ");
				} else {
					HttpSession session = request.getSession(true);
					session.setAttribute(Constants.USER_SESSION, user);

					FacesContext facesContext = FacesContext.getCurrentInstance();
					ExternalContext externalContext = facesContext.getExternalContext();

					Map<String, Object> flash = externalContext.getFlash();
					String currentUrl = (String) flash.get(Constants.CURRENT_URL);

					if (StringUtils.isBlank(currentUrl)) {
						// Redirect to default page after successful login
						externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
					} else {
						externalContext.redirect(currentUrl);
					}
				}

			} else {
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("type", "error");
				flash.put("message", "authntication errror refresh page and try again");

				changeUrl();
				return;
			}

		}

		catch (Exception e) {

		}

	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {

			Faces.redirect(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login() {
		try {
			String hashPass = Utility.hashPassword(passowrd);
			User user = userFacade.findUserByUserNameAndPassword(userName, hashPass);

			if (user == null) {
				Utility.addErrorMessage("name_or_pass_wrong");
			} else {
				HttpSession session = request.getSession(true);
				session.setAttribute(Constants.USER_SESSION, user);

				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();

				Map<String, Object> flash = externalContext.getFlash();
				String currentUrl = (String) flash.get(Constants.CURRENT_URL);

				if (StringUtils.isBlank(currentUrl)) {
					// Redirect to default page after successful login
					externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
				} else {
					externalContext.redirect(currentUrl);
				}
			}

		} catch (Exception e) {

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
