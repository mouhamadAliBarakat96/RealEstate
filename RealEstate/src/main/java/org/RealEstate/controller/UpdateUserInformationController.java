package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
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

	@Inject
	private AppSinglton appSinglton ;
	
	private User user;

	private String fullUrl = "";
	private String ipAddressWithPort;

	@PostConstruct
	public void init() {
		HttpSession session = request.getSession(true);

		user = (User) session.getAttribute(Constants.USER_SESSION);
		if (user == null) {

			try {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME).concat("/");
			if (user.getProfileImageUrl() != null) {
				fullUrl = fullUrl.concat(user.getProfileImageUrl());
			}
		}

	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		String ipAddress = request.getRemoteAddr();

		if (appSinglton.getMode().equals(Constants.DEVELOPMENT)) {
			ipAddressWithPort = "http://" + ipAddress +  ":" + request.getLocalPort() ;
		} else {
			ipAddressWithPort = "https://" + appSinglton.getRealDns() ;
		}

		return ipAddressWithPort;
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

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
