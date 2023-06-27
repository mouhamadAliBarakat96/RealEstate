package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.omnifaces.cdi.Param;

@ViewScoped
@Named
public class UserProfilePicture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	@Inject
	private UserFacade userFacade;

	private User user;

	private String fullUrl = "";

	private String ipAddressWithPort;

	@PostConstruct
	public void init() {
		if (id > 0) {
			user = userFacade.find(id);
			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME).concat("/").concat(user.getProfileImageUrl());

		}

	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;

		return ipAddressWithPort;
	}

	public void changeProfilePictureStatus() {
		try {
			this.user.setShowProfilePicture(!user.isShowProfilePicture());

			user = 	userFacade.save(user);
			CommonUtility.addMessageToFacesContext(" save_success ", "success");

		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext(" error_on_save ", "error");

			// TODO Auto-generated catch block
			e.printStackTrace();
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
