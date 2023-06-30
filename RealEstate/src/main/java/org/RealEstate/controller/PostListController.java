package org.RealEstate.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.LazyDataModel.LazyPostModel;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.Village;
import org.RealEstate.utils.CommonUtility;
import org.omnifaces.util.Ajax;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named
public class PostListController implements Serializable {
	private static final long serialVersionUID = 1L;

	private LazyDataModel<RealEstate> lazyModel;

	// EJB
	@Inject
	private RealEstateFacade realEstateFacade;
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

	// post status
	private Long nmOfPostAccepted;
	private Long nbOfPostPending;
	private Long nbOfPostRevieuxByUser;
	private Long nbOfPostReffused;
	private Long nbOfPostExpired;

	// post type
	private Long numOfPostAppartmentRent;
	private Long numOfPostAppartmentSell;
	private Long numOfPostLand;
	private Long numOfPostShopRent;
	private Long numOfPostShopSell;
	private Long numOfPostOfficeRent;
	private Long numOfPostOfficeSell;
	private Long numOfStoreHouseRent;
	private Long numOfStoreHouseSell;

	public Long getNumOfStoreHouseRent() {
		return numOfStoreHouseRent;
	}

	public void setNumOfStoreHouseRent(Long numOfStoreHouseRent) {
		this.numOfStoreHouseRent = numOfStoreHouseRent;
	}

	public Long getNumOfStoreHouseSell() {
		return numOfStoreHouseSell;
	}

	public void setNumOfStoreHouseSell(Long numOfStoreHouseSell) {
		this.numOfStoreHouseSell = numOfStoreHouseSell;
	}

	private Long numAllPost;

	// Filters ;
	private Date fromDate;
	private Date toDate;
	private Governorate governorate;
	private District district;
	private Village village;
	private int minPrice;
	private int maxPrice;

	@PostConstruct
	public void init() {
		lazyModel = new LazyPostModel(realEstateFacade, fromDate, toDate, governorate, district, village, minPrice,
				maxPrice);

		fillData();
	}

	private void fillData() {
		// post status
		nmOfPostAccepted = realEstateFacade.findUserCountPostByStatus(PostStatus.ACCEPTED);
		nbOfPostPending = realEstateFacade.findUserCountPostByStatus(PostStatus.PENDING);
		nbOfPostRevieuxByUser = realEstateFacade.findUserCountPostByStatus(PostStatus.TO_REVIEUX_BY_USER);
		nbOfPostReffused = realEstateFacade.findUserCountPostByStatus(PostStatus.REFFUSED);
		nbOfPostExpired = realEstateFacade.findUserCountPostByStatus(PostStatus.EXPIRED);
		// post type
		numOfPostAppartmentRent = realEstateFacade.findUserCountPostByType(PostType.APPRATMENT_RENT);
		numOfPostAppartmentSell = realEstateFacade.findUserCountPostByType(PostType.APPRATMENT_SELL);
		numOfPostLand = realEstateFacade.findUserCountPostByType(PostType.LAND);
		numOfPostShopRent = realEstateFacade.findUserCountPostByType(PostType.SHOP_RENT);
		numOfPostShopSell = realEstateFacade.findUserCountPostByType(PostType.SHOP_SELL);
		numOfPostOfficeRent = realEstateFacade.findUserCountPostByType(PostType.OFFICE_RENT);
		numOfPostOfficeSell = realEstateFacade.findUserCountPostByType(PostType.OFFICE_SELL);
		numOfStoreHouseRent = realEstateFacade.findUserCountPostByType(PostType.STORE_HOUSE_RENT);
		numOfStoreHouseSell = realEstateFacade.findUserCountPostByType(PostType.STORE_HOUSE_SELL);
		numAllPost = (long) realEstateFacade.count();
		// List
		governorates = governorateFacade.findAll();

	}

	public void search() {

		if (toDate != null && fromDate != null && toDate.compareTo(fromDate) < 0) {
			CommonUtility.addMessageToFacesContext("toDate its before fromDate ", "error");

		} else if (minPrice > maxPrice) {
			CommonUtility.addMessageToFacesContext("minPrice greater than maxPrice ", "error");

		} else {
			lazyModel = new LazyPostModel(realEstateFacade, fromDate, toDate, governorate, district, village, minPrice,
					maxPrice);
			Ajax.oncomplete("PF('pageItemsTableWgVar').filter(); ");
		}

	}

	public void fillDistrict() {
		district = null;
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

	public LazyDataModel<RealEstate> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<RealEstate> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public RealEstateFacade getRealEstateFacade() {
		return realEstateFacade;
	}

	public void setRealEstateFacade(RealEstateFacade realEstateFacade) {
		this.realEstateFacade = realEstateFacade;
	}

	public Long getNmOfPostAccepted() {
		return nmOfPostAccepted;
	}

	public void setNmOfPostAccepted(Long nmOfPostAccepted) {
		this.nmOfPostAccepted = nmOfPostAccepted;
	}

	public Long getNbOfPostPending() {
		return nbOfPostPending;
	}

	public void setNbOfPostPending(Long nbOfPostPending) {
		this.nbOfPostPending = nbOfPostPending;
	}

	public Long getNbOfPostRevieuxByUser() {
		return nbOfPostRevieuxByUser;
	}

	public void setNbOfPostRevieuxByUser(Long nbOfPostRevieuxByUser) {
		this.nbOfPostRevieuxByUser = nbOfPostRevieuxByUser;
	}

	public Long getNbOfPostReffused() {
		return nbOfPostReffused;
	}

	public void setNbOfPostReffused(Long nbOfPostReffused) {
		this.nbOfPostReffused = nbOfPostReffused;
	}

	public Long getNbOfPostExpired() {
		return nbOfPostExpired;
	}

	public void setNbOfPostExpired(Long nbOfPostExpired) {
		this.nbOfPostExpired = nbOfPostExpired;
	}

	public Long getNumOfPostAppartmentRent() {
		return numOfPostAppartmentRent;
	}

	public void setNumOfPostAppartmentRent(Long numOfPostAppartmentRent) {
		this.numOfPostAppartmentRent = numOfPostAppartmentRent;
	}

	public Long getNumOfPostAppartmentSell() {
		return numOfPostAppartmentSell;
	}

	public void setNumOfPostAppartmentSell(Long numOfPostAppartmentSell) {
		this.numOfPostAppartmentSell = numOfPostAppartmentSell;
	}

	public Long getNumOfPostLand() {
		return numOfPostLand;
	}

	public void setNumOfPostLand(Long numOfPostLand) {
		this.numOfPostLand = numOfPostLand;
	}

	public Long getNumOfPostShopRent() {
		return numOfPostShopRent;
	}

	public void setNumOfPostShopRent(Long numOfPostShopRent) {
		this.numOfPostShopRent = numOfPostShopRent;
	}

	public Long getNumOfPostShopSell() {
		return numOfPostShopSell;
	}

	public void setNumOfPostShopSell(Long numOfPostShopSell) {
		this.numOfPostShopSell = numOfPostShopSell;
	}

	public Long getNumOfPostOfficeRent() {
		return numOfPostOfficeRent;
	}

	public void setNumOfPostOfficeRent(Long numOfPostOfficeRent) {
		this.numOfPostOfficeRent = numOfPostOfficeRent;
	}

	public Long getNumOfPostOfficeSell() {
		return numOfPostOfficeSell;
	}

	public void setNumOfPostOfficeSell(Long numOfPostOfficeSell) {
		this.numOfPostOfficeSell = numOfPostOfficeSell;
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

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getNumAllPost() {
		return numAllPost;
	}

	public void setNumAllPost(Long numAllPost) {
		this.numAllPost = numAllPost;
	}

}
