package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.RealEstate.dto.MenuItem;

@Named
@SessionScoped
public class TemplateController implements Serializable {
	private static final long serialVersionUID = -1L;
	private List<MenuItem> menu = new ArrayList<MenuItem>();

	@PostConstruct
	public void init() {

		menu = fillMenuItems();
	}

	public List<MenuItem> fillMenuItems() {
		List<MenuItem> menu = new ArrayList<>();
		MenuItem m1 = null;
		MenuItem m2 = null;
		MenuItem m3 = null;
		MenuItem m4 = null;
		MenuItem m5 = null;

		m1 = new MenuItem("1", getValue("home"), " fas fa-home ", "", getValue("home"), true);

		m2 = new MenuItem(getValue("home"), "  dripicons-home  ", "index-admin", true, "addButton");
		m1.addChild(m2);

		menu.add(m1);

		// --- configuration ---//
		m1 = new MenuItem("2", getValue("card"), " fa fa-wrench ", "", getValue("card"), true);

		m2 = new MenuItem("Advertisement List", "   mdi mdi-note-plus-outline     ", "governorate-list", true,
				"addButton");
		m1.addChild(m2);

		
		menu.add(m1);

		return menu;

	}

	
	
	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
	}

	private String getValue(java.lang.String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("resources.Bundle");
		return bundle.getString(key);
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

}
