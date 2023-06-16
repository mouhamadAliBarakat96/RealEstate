package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.AppratmentRentFacade;
import org.RealEstate.model.RealEstate;
import org.omnifaces.util.Faces;
import org.primefaces.model.ResponsiveOption;

@Named
@ViewScoped
public class RealEstateCardController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String REQUEST_PARAM = "id";
	private RealEstate item;
	private List<String> imagesUrl = Arrays.asList("property-1.jpg", "property-2.jpg", "property-3.jpg",
			"property-4.jpg");
	private List<ResponsiveOption> responsiveOptions1;

	@PostConstruct
	public void init() {

		responsiveOptions1 = new ArrayList<>();
		responsiveOptions1.add(new ResponsiveOption("1024px", 5));
		responsiveOptions1.add(new ResponsiveOption("768px", 3));
		responsiveOptions1.add(new ResponsiveOption("560px", 1));

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

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", item.getId() + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RealEstate getItem() {
		return item;
	}

	public void setItem(RealEstate item) {
		this.item = item;
	}

	public List<String> getImagesUrl() {
		return imagesUrl;
	}

	public void setImagesUrl(List<String> imagesUrl) {
		this.imagesUrl = imagesUrl;
	}

	public List<ResponsiveOption> getResponsiveOptions1() {
		return responsiveOptions1;
	}

	public void setResponsiveOptions1(List<ResponsiveOption> responsiveOptions1) {
		this.responsiveOptions1 = responsiveOptions1;
	}

}
