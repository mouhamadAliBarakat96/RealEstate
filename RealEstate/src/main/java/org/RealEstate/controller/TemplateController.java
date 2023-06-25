package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.dto.MenuItem;
import org.omnifaces.util.Faces;

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

		m1 = new MenuItem("2", getValue("card"), " fa fa-cogs ", "", getValue("card"), true);

		m2 = new MenuItem("Governorate List", "    mdi mdi-google-nearby      ", "governorate-list", true, "addButton");
		m1.addChild(m2);

		m2 = new MenuItem("", "", "governorate", true, "display: none;");
		m1.addChild(m2);
		// ----//
		m2 = new MenuItem("District List", "     mdi mdi-image-area-close       ", "district-list", true, "addButton");
		m1.addChild(m2);

		m2 = new MenuItem("", "", "district", true, "display: none;");
		m1.addChild(m2);

		// ----//
		m2 = new MenuItem("Village List", "      mdi mdi-city-variant     ", "village-list", true, "addButton");
		m1.addChild(m2);
		m2 = new MenuItem("", "", "village", true, "display: none;");
		m1.addChild(m2);

		// --//

		m2 = new MenuItem("", "", "village", true, "display: none;");
		m1.addChild(m2);
		m2 = new MenuItem("Configuration", "      mdi mdi-cogs     ", "configuration", true, "addButton");
		m1.addChild(m2);

		menu.add(m1);

		// mdi mdi-cogs
		m1 = new MenuItem("3", "Management", "  fa fa-list-ol  ", "", getValue("card"), true);
		m2 = new MenuItem("Post List", "      mdi mdi-hospital-building     ", "post-list", true, "addButton");
		m1.addChild(m2);
		m2 = new MenuItem("", "", "post", true, "display: none;");
		m1.addChild(m2);

		m2 = new MenuItem("Chalet List", "        mdi mdi-hospital-building      ", "chalet-list", true, "addButton");
		m1.addChild(m2);
		
		m2 = new MenuItem("", "", "chalet", true, "display: none;");		m1.addChild(m2);
		m1.addChild(m2);
		
		
		m2 = new MenuItem("User List", "       dripicons-user-group     ", "user-list", true, "addButton");
		m1.addChild(m2);
		
		m2 = new MenuItem("", "", "user-post", true, "display: none;");
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

	public void logout() {

		try {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			request.logout();
			externalContext.invalidateSession();
			Faces.redirect("admin-login.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
