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

import org.RealEstate.dto.MenuItem;
import org.RealEstate.enumerator.LanguageEnum;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.utils.Utility;

@Named
@ViewScoped
public class MakanTemplateController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MenuItem> menu = new ArrayList<>();
	 
	@Inject
	private LanguageController languageController;
	
	@PostConstruct
	public void init() {
		fillMenuItems(menu);
	}

	public void changeLanguage() {

	}

	public void fillMenuItems(List<MenuItem> menu) {
		MenuItem m1 = null;

		m1 = new MenuItem(Utility.getMessage("real_estates", languageController.getBundle_name()), "", "index-page", true, "nav-item nav-link active", PropertyKindEnum.REALESTATE.toString());
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("chalets", languageController.getBundle_name()), "", "index-page", true, "nav-item nav-link", PropertyKindEnum.CHALET.toString());
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("add_new", languageController.getBundle_name()), "", "userpost-card", true, "nav-item nav-link", "post-card");
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("my_posts", languageController.getBundle_name()), "", "userpost-list", true, "nav-item nav-link", "post-list");
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("contact_us", languageController.getBundle_name()), "", "contact-us", true, "nav-item nav-link", "contact-us");
		menu.add(m1);

		m1 = new MenuItem(Utility.getMessage("login", languageController.getBundle_name()), "", "login-user", true, "nav-item nav-link", "login");
		menu.add(m1);

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

	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
	}
}
