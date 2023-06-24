package org.RealEstate.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.LazyDataModel.LazyChalelModel;
import org.RealEstate.LazyDataModel.LazyPostModel;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.Village;
import org.RealEstate.utils.CommonUtility;
import org.omnifaces.util.Ajax;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named
public class ChaletListController implements Serializable {
	private static final long serialVersionUID = 1L;

	private LazyDataModel<Chalet> lazyModel;

	@Inject
	private ChaletFacade chaletFacade;

	@Inject
	private GovernorateFacade governorateFacade;
	@Inject
	private DistrictFacade districtFacade;
	@Inject
	private VillageFacade villageFacade;

	// List
	private List<Governorate> governorates;

	private List<District> districts;

	private List<Village> villages;

	private Date fromDate;
	private Date toDate;
	private Governorate governorate;
	private District district;
	private Village village;

	@PostConstruct
	public void init() {
		lazyModel = new LazyChalelModel(chaletFacade, fromDate, toDate, governorate, district, village);
		fillData();
	}

	private void fillData() {

		governorates = governorateFacade.findAll();
	}

	public void search() {

		if (toDate != null && fromDate != null && toDate.compareTo(fromDate) < 0) {
			CommonUtility.addMessageToFacesContext("toDate its before fromDate ", "error");

		} else {
			lazyModel = new LazyChalelModel(chaletFacade, fromDate, toDate, governorate, district, village);
			Ajax.oncomplete("PF('pageItemsTableWgVar').filter(); ");
		}

	}

	public void fillDistrict() {
		district = null ;
		village = null;
		
		if (governorate != null) {
			districts = districtFacade.findByGovernorate(governorate.getId());
		}
	
	}

	public void fillVillage() {
		village = null;
		if (district != null) {
			villages = villageFacade.findByDisctrict(district.getId());
		}
	}

	public List<Governorate> getGovernorates() {
		return governorates;
	}

	public void setGovernorates(List<Governorate> governorates) {
		this.governorates = governorates;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	public List<Village> getVillages() {
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public ChaletFacade getChaletFacade() {
		return chaletFacade;
	}

	public void setChaletFacade(ChaletFacade chaletFacade) {
		this.chaletFacade = chaletFacade;
	}

	public LazyDataModel<Chalet> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<Chalet> lazyModel) {
		this.lazyModel = lazyModel;
	}

}
