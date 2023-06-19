package org.RealEstate.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.dto.MenuItem;

@Named
@ViewScoped
public class MakanTemplateController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MenuItem> menu = new ArrayList<>();

	@PostConstruct
	public void init() {
		fillMenuItems(menu);
	}

	public void fillMenuItems(List<MenuItem> menu) {
		MenuItem m1 = null;

		m1 = new MenuItem("Home", "", "index-page", true, "nav-item nav-link active");
		menu.add(m1);

		m1 = new MenuItem("Contact us", "", "contact-us", true, "nav-item nav-link");
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
