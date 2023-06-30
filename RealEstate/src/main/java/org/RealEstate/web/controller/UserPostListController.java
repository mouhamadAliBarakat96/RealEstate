package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class UserPostListController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	private User user;

	private List<RealEstate> realEstates = new ArrayList<RealEstate>();

	private List<Chalet> chalets = new ArrayList<>();

	private String fullUrl = "";
	private String ipAddressWithPort;

	@Inject
	private UserFacade userFacade;

	@PostConstruct
	public void init() {
		user = getUser();
		// go to login page
		if (user == null) {
			try {
				Faces.redirect("/user-login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");
			realEstates = user.getReadStateList();
			chalets = user.getChales();
		}
	}

	public User getUser() {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute(Constants.USER_SESSION);
		return user;
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

	public boolean hasRoomsAndBathRooms(PostType type) {
		if (type == null) {
			return true;
		} else
			return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
					|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL));
	}

	public void navigateToUserReal(RealEstate item) {
		try {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("edit-realestate", "true");
			String url = "userPost-card.xhtml?id=" + item.getId();
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void navigateToUserChalet(Chalet item) {
		try {
			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("edit-chalet", "true");
			String url = "userPost-card.xhtml?id=" + item.getId();
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<RealEstate> getRealEstates() {
		return realEstates;
	}

	public void setRealEstates(List<RealEstate> realEstates) {
		this.realEstates = realEstates;
	}

	public List<Chalet> getChalets() {
		return chalets;
	}

	public void setChalets(List<Chalet> chalets) {
		this.chalets = chalets;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
