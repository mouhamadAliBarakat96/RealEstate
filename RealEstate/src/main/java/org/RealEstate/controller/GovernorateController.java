package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.Governorate;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Utility;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class GovernorateController extends AbstractController<Governorate> implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private GovernorateFacade governorateFacade;
	private Governorate governorate = new Governorate();
	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	@PostConstruct
	public void init() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (!facesContext.isPostback()) {
			if (id > 0) {
				governorate = find((id));
				if (governorate == null) {
					try {
						Faces.redirect("exception");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				if (flash.containsKey("new-card")) {

					flash.setKeepMessages(true);
					CommonUtility.addMessageToFacesContext(" save_success ", "success");
				}
			} else {
				governorate = new Governorate();
			}
		}
	}

	public void save() {
		try {
			if (getItem().getId() <= 0) {
				super.save();
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("new-card", "true");
				changeUrl();
			} else {
				governorate = getAbstractFacade().save(governorate);

				CommonUtility.addMessageToFacesContext(" save_success ", "success");

			}
		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getMessage(), "error");
			e.printStackTrace();
		}

	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", getItem().getId() + "");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Governorate getItem() {
		return governorate;
	}

	@Override
	protected void setItem(Governorate item) {
		this.governorate = item;
	}

	@Override
	protected long getId() {
		return this.governorate.getId();
	}

	@Override
	protected ICRUDOperations<Governorate> getAbstractFacade() {
		return governorateFacade;
	}

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
	}

}
