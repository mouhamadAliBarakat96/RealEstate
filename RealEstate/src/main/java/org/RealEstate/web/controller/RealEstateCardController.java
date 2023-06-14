package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.model.RealEstate;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class RealEstateCardController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final String REQUEST_PARAM = "id";
	private RealEstate item;

	@PostConstruct
	public void init() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		if (!facesContext.isPostback()) {
			String id = externalContext.getRequestParameterMap().get(REQUEST_PARAM);
			if (id != null && Double.parseDouble(id) > 0) {
				// find item by id
				if (item == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				// initiate by type
			}

		}
	}

	public RealEstate getItem() {
		return item;
	}

	public void setItem(RealEstate item) {
		this.item = item;
	}

}
