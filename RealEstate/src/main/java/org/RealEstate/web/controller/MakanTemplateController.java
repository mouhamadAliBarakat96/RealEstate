package org.RealEstate.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private List<MenuItem> menu = new ArrayList<MenuItem>();

	@PostConstruct
	public void init() {
		fillMenuItems();
	}

	public List<MenuItem> fillMenuItems() {
		List<MenuItem> menu = new ArrayList<>();
		MenuItem m1 = null;

		m1 =  new MenuItem("Home", "", "home-page", true, "nav-item nav-link active");
		menu.add(m1);
		
		m1 =  new MenuItem("Contact us", "", "contact-page", true, "nav-item nav-link");
		menu.add(m1);

		return menu;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
	}

}
