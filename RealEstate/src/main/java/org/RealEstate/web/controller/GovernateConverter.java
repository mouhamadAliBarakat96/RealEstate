package org.RealEstate.web.controller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.model.Governorate;

@Named
@FacesConverter("governateConverter")
public class GovernateConverter implements Converter {

	@Inject
	private GovernorateFacade facade;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub

		long id = Long.valueOf(value);
		return facade.find(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		long id = ((Governorate) value).getId();
		return String.valueOf(id);
	}

}
