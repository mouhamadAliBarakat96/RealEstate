package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
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

import org.RealEstate.model.User;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class SignUpController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user = new User();
	@EJB
	private UserService userService;

	@Inject
	private HttpServletRequest request;

	@PostConstruct
	public void init() {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		if (flash.containsKey("type")) {
			String type = (String) flash.get("type");
			String message = (String) flash.get("message");
			flash.setKeepMessages(true);
			CommonUtility.addMessageToFacesContext(message, type);
		}
	}

	public void save() {
		try {

			String fbId = null;
			Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			if (requestParamMap.containsKey("fbId")) {
				fbId = requestParamMap.get("fbId");

			} else {
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("type", "error");
				flash.put("message", "authntication errror refresh page and try again");

				changeUrl();
				return;
			}
			user.setFbId(fbId);
			user.setPassowrd(Utils.sha256(user.getPassowrd()));

			Response r = userService.createUser(user);

			if (r.getStatus() == Status.CREATED.getStatusCode()) {

				User user = Utils.getObjectFromString(r.getEntity().toString(), User.class);

				HttpSession session = request.getSession(true);
				session.setAttribute(Constants.USER_SESSION, user);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
			} else {
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("type", "error");
				flash.put("message", r.getEntity().toString());

				changeUrl();
			}

		} catch (Exception e) {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("type", "error");
			flash.put("message", e);

			e.printStackTrace();

			changeUrl();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
