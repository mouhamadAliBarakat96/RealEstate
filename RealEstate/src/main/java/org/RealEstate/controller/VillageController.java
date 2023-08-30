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
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.District;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Utils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class VillageController extends AbstractController<Village> implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private VillageFacade villageFacade;

	@EJB
	private DistrictFacade districtFacade;

	private Village village = new Village();
	private List<District> listDistrict = new ArrayList<>();

	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	private AppSinglton appSinglton;

	@PostConstruct
	public void init() {
		listDistrict = districtFacade.findAll();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (!facesContext.isPostback()) {
			if (id > 0) {
				village = find((id));
				if (village == null) {
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
				village = new Village();
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
				village = getAbstractFacade().save(village);
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
		url = Utils.replaceHost(url, appSinglton.getRealDns() ,  appSinglton.getMode());
		try {
			if (!isSaveAndNew) {
				Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", getItem().getId() + "");

			} else {
				Faces.redirect(url + "?");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	@Override
	protected Village getItem() {
		return village;
	}

	@Override
	protected void setItem(Village item) {
		this.village = item;
	}

	@Override
	protected long getId() {
		return this.village.getId();
	}

	@Override
	protected ICRUDOperations<Village> getAbstractFacade() {
		return villageFacade;
	}

	public List<District> getListDistrict() {
		return listDistrict;
	}

	public void setListDistrict(List<District> listDistrict) {
		this.listDistrict = listDistrict;
	}

}
