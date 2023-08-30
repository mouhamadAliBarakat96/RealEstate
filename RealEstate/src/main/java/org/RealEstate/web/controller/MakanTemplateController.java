package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.dto.MenuItem;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.facade.AdsFacade;
import org.RealEstate.model.Ads;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class MakanTemplateController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LanguageController languageController;
	@Inject
	private HttpServletRequest request;

	private User user;

	/**
	 * Ads
	 */

	@EJB
	private AdsFacade adsFacade;

	private List<Ads> adsList;

	private String fullUrlAdsImage = "";
<<<<<<< Upstream, based on origin/development

	@Inject
	private AppSinglton appSinglton ;
=======
	private Locale locale;
>>>>>>> 7e4ec34 fix languages
	
	@PostConstruct
	public void init() {
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute(Constants.USER_SESSION);
		locale=languageController.getLocale();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		adsList = adsFacade.findAll();
		fullUrlAdsImage = fullUrlAdsImage.concat(getIpAddressWithPort()).concat("/")
				.concat(Constants.IMAGES).concat("/").concat(Constants.ADS_IMAGE_DIR_NAME).concat("/");

	}

	private String ipAddressWithPort ;

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		String ipAddress = request.getRemoteAddr();

		if (appSinglton.getMode().equals(Constants.DEVELOPMENT)) {
			ipAddressWithPort = "http://" + ipAddress +  ":" + request.getLocalPort() ;
		} else {
			ipAddressWithPort = "https://" + ipAddress ;
		}

		return ipAddressWithPort;
	}
	
	

	public List<MenuItem> fillMenuItems() {
		List<MenuItem> menu = new ArrayList<>();
		MenuItem m1 = null;

		m1 = new MenuItem(languageController.getMessage("real_estates"), "", "index-page", true, "nav-item nav-link active",
				PropertyKindEnum.REALESTATE.toString());
		menu.add(m1);

		m1 = new MenuItem(languageController.getMessage("chalets"), "", "index-page", true, "nav-item nav-link",
				PropertyKindEnum.CHALET.toString());
		menu.add(m1);

		m1 = new MenuItem(languageController.getMessage("add_new"), "", "userpost-card", true, "nav-item nav-link", "post-card");
		menu.add(m1);

		m1 = new MenuItem(languageController.getMessage("my_posts"), "", "userpost-list", true, "nav-item nav-link", "post-list");
		menu.add(m1);

		m1 = new MenuItem(languageController.getMessage("contact_us"), "", "contact-us", true, "nav-item nav-link", "contact-us");
		menu.add(m1);

		if (user == null) {
			m1 = new MenuItem(languageController.getMessage("login"), "", "login-user", true, "nav-item nav-link", "login");
			menu.add(m1);

			m1 = new MenuItem(languageController.getMessage("register"), "", "signup", true, "nav-item nav-link", "register");
			menu.add(m1);

		} else {
			m1 = new MenuItem(languageController.getMessage("user-information"), "", "user-information-front-end", true,
					"nav-item nav-link", "user-information-front-end");
			menu.add(m1);

		}

		return menu;
	}

	public List<FacesMessage> getMessages() {

		List<FacesMessage> uniqueMessages = new ArrayList<>();

		for (FacesMessage message : FacesContext.getCurrentInstance().getMessageList()) {

			if (uniqueMessages.isEmpty()) {

				uniqueMessages.add(message);

			} else {

				boolean addMessage = true;

				for (FacesMessage currentMessage : uniqueMessages) {

					if (currentMessage.getDetail().equals(message.getDetail())) {
						addMessage = false;
						break;
					}
				}

				if (addMessage) {
					uniqueMessages.add(message);
				}

			}

		}

		return uniqueMessages;
	}

	public String showRealEstateTitle() {
		return languageController.getMessage("house_and_land");
	}

	public void logout() {

		try {
			HttpSession session = request.getSession(true);
			session.removeAttribute(Constants.USER_SESSION);
			Faces.redirect("index.xhtml");
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

	public List<Ads> getAdsList() {
		return adsList;
	}

	public void setAdsList(List<Ads> adsList) {
		this.adsList = adsList;
	}

	public String getFullUrlAdsImage() {
		return fullUrlAdsImage;
	}

	public void setFullUrlAdsImage(String fullUrlAdsImage) {
		this.fullUrlAdsImage = fullUrlAdsImage;
	}

}
