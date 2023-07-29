package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class ChaletCardController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";

	@Inject
	private ChaletFacade facade;

	private Chalet item;

	private String fullUrl = "";
	private String ipAddressWithPort;
	private int activeIndex = 0;

	private String fullUrlProfilePicture = "";



	
	
	@PostConstruct
	public void init() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (!facesContext.isPostback()) {
			String id = externalContext.getRequestParameterMap().get(REQUEST_PARAM);

			if (id != null && Long.parseLong(id) > 0) {
				// find item by id

				item = facade.find(Long.parseLong(id));

				if (item == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/")
							.concat(Constants.IMAGES).concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

					fullUrlProfilePicture = fullUrlProfilePicture.concat("http://").concat(getIpAddressWithPort())
							.concat("/").concat(Constants.IMAGES).concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME)
							.concat("/");
					if (item.getUser().getProfileImageUrl() != null) {
						fullUrlProfilePicture = fullUrlProfilePicture.concat(item.getUser().getProfileImageUrl());
					}

				}
			} else {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;
		System.out.println(ipAddressWithPort);
		return ipAddressWithPort;
	}

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		activeIndex = Integer.valueOf(params.get("index"));
	}

	public Chalet getItem() {
		return item;
	}

	public void setItem(Chalet item) {
		this.item = item;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public void setIpAddressWithPort(String ipAddressWithPort) {
		this.ipAddressWithPort = ipAddressWithPort;
	}

	public String getFullUrlProfilePicture() {
		return fullUrlProfilePicture;
	}

	public void setFullUrlProfilePicture(String fullUrlProfilePicture) {
		this.fullUrlProfilePicture = fullUrlProfilePicture;
	}
	
	
	
	

}
