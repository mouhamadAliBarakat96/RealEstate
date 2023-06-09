package org.RealEstate.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.dto.MenuItem;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;

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
	
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(languageController.getLocale());
	}

	public List<MenuItem> fillMenuItems() {
		List<MenuItem> menu=new ArrayList<>();
		MenuItem m1 = null;
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute(Constants.USER_SESSION);
		
		
		m1 = new MenuItem(Utility.getMessage("real_estates"), "", "index-page", true, "nav-item nav-link active",
				PropertyKindEnum.REALESTATE.toString());
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("chalets"), "", "index-page", true, "nav-item nav-link",
				PropertyKindEnum.CHALET.toString());
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("add_new"), "", "userpost-card", true, "nav-item nav-link", "post-card");
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("my_posts"), "", "userpost-list", true, "nav-item nav-link", "post-list");
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("contact_us"), "", "contact-us", true, "nav-item nav-link", "contact-us");
		menu.add(m1);

		
		if(user==null)
		  m1 = new MenuItem(Utility.getMessage("login"), "", "login-user", true, "nav-item nav-link", "login");
		else  
		  m1 = new MenuItem(Utility.getMessage("logout"), "", "logout-user", true, "nav-item nav-link", "logout");
		
		menu.add(m1);

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
		return Utility.getMessage("house_and_land");
	}
}
