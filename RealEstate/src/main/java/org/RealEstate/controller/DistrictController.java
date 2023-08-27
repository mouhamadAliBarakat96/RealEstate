package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Utils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class DistrictController extends AbstractController<District> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private DistrictFacade districtFacade;
	@EJB
	private GovernorateFacade governorateFacade;

	private List<Governorate> governorateList = new ArrayList<>();

	private District district = new District();

	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	@Inject
	private AppSinglton appSinglton;

	@PostConstruct
	public void init() {
		governorateList = governorateFacade.findAll();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (!facesContext.isPostback()) {
			if (id > 0) {
				district = find((id));
				if (district == null) {
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
				district = new District();
			}
		}
	}

	public void save(boolean isSaveAndNew) {
		try {
			if (getItem().getId() <= 0) {
				super.save();
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("new-card", "true");
				changeUrl(isSaveAndNew);
			} else {
				district = getAbstractFacade().save(district);
				if (isSaveAndNew) {
					changeUrl(isSaveAndNew);

				}
				CommonUtility.addMessageToFacesContext(" save_success ", "success");

			}
		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getMessage(), "error");
			e.printStackTrace();
		}

	}

	private void changeUrl(boolean isSaveAndNew) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns());

		try {
			if (!isSaveAndNew) {
				Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", getItem().getId() + "");

			} else {
				Faces.redirect(url);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	@Override
	protected District getItem() {
		return district;
	}

	@Override
	protected void setItem(District item) {
		this.district = item;
	}

	@Override
	protected long getId() {
		return this.district.getId();
	}

	@Override
	protected ICRUDOperations<District> getAbstractFacade() {
		return districtFacade;
	}

	public List<Governorate> getGovernorateList() {
		return governorateList;
	}

	public void setGovernorateList(List<Governorate> governorateList) {
		this.governorateList = governorateList;
	}

}
