package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.enumerator.Country;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.RealEstate.utils.Utils;
import org.RealEstate.web.controller.LanguageController;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class SignUpController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String RQUEST_FROM = "from";
	
	private User user = new User();
	@EJB
	private UserService userService;

	@Inject
	private HttpServletRequest request;

	@Inject
	private  AppSinglton appSinglton ;
	
	@Inject
	private LanguageController sessionLanguage;
	
	private String from_url = "";
	
	@PostConstruct
	public void init() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (!facesContext.isPostback()) {
			from_url = externalContext.getRequestParameterMap().get(RQUEST_FROM);
		}
		
		
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		if (flash.containsKey("type")) {
			String type = (String) flash.get("type");

			// try get message on arabic

			String message = (String) flash.get("message");

			try {
				message = Utility.getMessage(message);
			} catch (Exception e) {

			}

			flash.setKeepMessages(true);
			CommonUtility.addMessageToFacesContext(message, type);
		}
	}
	
	
	
	private void requestFromUrl() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String url = request.getRequestURL().toString();
			String requestUri = request.getRequestURI();
			
			if (!StringUtils.isEmpty(from_url)) {
				url = url.replace(requestUri, from_url);
			} else {
				url = url.replace(requestUri, "/index.xhtml");
			}
			url = Utils.replaceHost(url, appSinglton.getRealDns()   , appSinglton.getMode());
			Faces.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	public boolean validate() {
		boolean isValid = true;

		if (StringUtils.isBlank(user.getFirstName())) {
			Utility.addErrorMessage("first_name_equried", sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getLastName())) {
			Utility.addErrorMessage("last_name_equried", sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getPhoneNumber())) {
			Utility.addErrorMessage("phone_no_required", sessionLanguage.getLocale());
			isValid = false;

		} else if (!Utils.validatePhoneNumber(user.getPhoneNumber())) {
			Utility.addErrorMessage(Constants.PHONE_NUMBER_NOT_CORRECT, sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getUserName())) {
			Utility.addErrorMessage("user_name_required", sessionLanguage.getLocale());
			isValid = false;
		}

		if (!Utils.validatePassword(user.getPassowrd())) {
			Utility.addErrorMessage("INVALID_PASSWORD", sessionLanguage.getLocale());
			isValid = false;
		}
		
		
		return isValid;
	}
	

	public void save() {
		try {

			/**
			 * TODO ACTIVE THIS CODE LATER
			 */
			
			/*
			 * String fbId = null; Map<String, String> requestParamMap =
			 * FacesContext.getCurrentInstance().getExternalContext()
			 * .getRequestParameterMap(); if (requestParamMap.containsKey("fbId")) { fbId =
			 * requestParamMap.get("fbId");
			 * 
			 * } else { Flash flash =
			 * FacesContext.getCurrentInstance().getExternalContext().getFlash();
			 * flash.put("type", "error"); flash.put("message",
			 * "authntication errror refresh page and try again");
			 * 
			 * changeUrl(); return; }
			 */
			
			if (!validate()) {
				return;
			}
 
			user.setPassowrd(Utils.sha256(user.getPassowrd()));
			user.setPhoneNumber(Utility.checkPhoneNumber(user.getPhoneNumber().replaceAll("\\s+", ""), Country.LEBANON));
			
			Response r = userService.createUser(user);
			if (r.getStatus() == Status.CREATED.getStatusCode()) {

				User user = Utils.getObjectFromString(r.getEntity().toString(), User.class);

				HttpSession session = request.getSession(true);
				session.setAttribute(Constants.USER_SESSION, user);
			
				/*FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");*/
				
				requestFromUrl();
			} else {
				Utility.addErrorMessage(r.getEntity().toString(), sessionLanguage.getLocale());
			}

		} catch (Exception e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("type", "error");
			flash.put("message", e);
			e.printStackTrace();
			changeUrl();
		}

	}

	public void validationEmtpyFields(User user) {
		if (StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getLastName())
				|| StringUtils.isBlank(user.getUserName()) || StringUtils.isBlank(user.getPassowrd())
				|| StringUtils.isBlank(user.getPhoneNumber())) {
			
			Utility.addWarningMessage("user_info_requried", sessionLanguage.getLocale());
		}

	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			url = Utils.replaceHost(url, appSinglton.getRealDns() ,   appSinglton.getMode());

			Faces.redirect(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
