package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.dto.RealEstateLazyDataModel;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.PostService;
import org.RealEstate.utils.Utility;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class IndexController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";

	@Inject
	private GovernorateFacade governorateFacade;
	@Inject
	private DistrictFacade districtFacade;
	@Inject
	private VillageFacade villageFacade;
	@Inject
	private RealEstateFacade realEstateFacade;
	@Inject
	private PostService postService;

	private List<RealEstate> realEstates = new ArrayList<>();

	private List<Chalet> chaletList = new ArrayList<>();

	private PostType selectPostType;

	private List<Governorate> governorates = new ArrayList<>();

	private List<Village> villages = new ArrayList<>();

	private List<District> districts = new ArrayList<>();

	// Search Bar Filters

	private User user;
	private String postType;
	private int minPrice;
	private int maxPrice;
	private AtomicLong totalCount = new AtomicLong();
	private int bedRoom;
	private boolean bedRoomEq;
	private int bathRoom;
	private boolean bathRoomEq;
	private Governorate selecteGovernorate = new Governorate();
	private District selecteDistrict = new District();
	private Village selecteVillage = new Village();

	// lazy model
	private RealEstateLazyDataModel lazyModel;

	@PostConstruct
	public void init() {
		governorates = governorateFacade.findAll();

		// realEstates = realEstateFacade.findAll();// GET RECOMMEND PROPERTIES LATER
		// totalCount.set(realEstates.size());

		lazyModel = new RealEstateLazyDataModel(realEstateFacade);
	}

	public void addViewsAfterClick(RealEstate item) {
		try {
			Response response = postService.updatePostVieux(item.getId(), item.getPostType().toString());

			if (response.getStatus() == Status.ACCEPTED.getStatusCode()) {
				System.out.println("Post Viewed ACCEPTED");

			} else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				System.out.println("Post Viewed NOT_FOUND");

			} else {
				System.out.println("Status NO CONTENT" + (String) response.getEntity());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void navigate(RealEstate item) {
		// Build the URL with the parameter values
		String url = "realEstate-card.xhtml?id=" + item.getId();
		// Use the ExternalContext object to redirect to the new page
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (IOException e) {
			// Handle the exception appropriately
		}
	}

	public void listenerSelectGovernate() {
		if (selecteGovernorate != null) {
			villages = new ArrayList<>();
			districts = districtFacade.findByGovernorate(selecteGovernorate.getId());
		}

	}

	public void listenerSelectDistrict() {
		if (selecteDistrict != null) {
			villages = villageFacade.findByDisctrict(selecteDistrict.getId());
		}
	}

	public void search() {

		// boolean searchFeildsError=false;

		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max");
			return;
		}

		lazyModel.setBathRoom(bathRoom);
		lazyModel.setUser(user != null ? user : null);
		lazyModel.setBathRoomEq(bathRoomEq);
		lazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		lazyModel.setGovernorate(
				selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		lazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);
		lazyModel.setBedRoom(bedRoom);
		lazyModel.setBedRoomEq(false);
		lazyModel.setMaxPrice(maxPrice);
		lazyModel.setMinPrice(minPrice);
		lazyModel.setPostType(selectPostType == null ? null : selectPostType.toString());
		// lazyModel.setTotalCount(totalCount);

		Utility.addSuccessMessage("search_complete");
	}

	public void listenerSelect(RealEstateTypeEnum type) {

		if (type == RealEstateTypeEnum.FORRENT) {
			realEstates = realEstates.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_RENT)
							|| x.getPostType().equals(PostType.OFFICE_RENT)
							|| x.getPostType().equals(PostType.SHOP_RENT))
					.collect(Collectors.toList());

		} else if (type == RealEstateTypeEnum.FORSELL) {
			realEstates = realEstates.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_SELL)
							|| x.getPostType().equals(PostType.OFFICE_SELL)
							|| x.getPostType().equals(PostType.SHOP_SELL) || x.getPostType().equals(PostType.LAND))
					.collect(Collectors.toList());

		}
	}

	public List<RealEstate> getRealEstates() {
		return realEstates;
	}

	public void setRealEstates(List<RealEstate> realEstates) {
		this.realEstates = realEstates;
	}

	public List<Chalet> getChaletList() {
		return chaletList;
	}

	public void setChaletList(List<Chalet> chaletList) {
		this.chaletList = chaletList;
	}

	public boolean hasRoomsAndBathRooms(PostType type) {
		if (type == null) {
			return true;
		} else
			return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
					|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL));
	}

	public int numberOfSellAppartments() {
		return realEstates.stream().filter(x -> x instanceof AppratmentRent).collect(Collectors.toList()).size();
	}

	public int numberOfRentAppartments() {
		return realEstates.stream().filter(x -> x instanceof AppratmentSell).collect(Collectors.toList()).size();
	}

	public int numberOfLands() {
		return realEstates.stream().filter(x -> x instanceof Land).collect(Collectors.toList()).size();
	}

	public int numberOfSellOffices() {
		return realEstates.stream().filter(x -> x instanceof OfficeSell).collect(Collectors.toList()).size();
	}

	public int numberOfRentOffices() {
		return realEstates.stream().filter(x -> x instanceof OfficeRent).collect(Collectors.toList()).size();
	}

	public void addToRealEstate(RealEstate item) {
		realEstates.add(item);
	}

	public void addToChalet(Chalet item) {
		this.chaletList.add(item);
	}

	public int random() {
		Random random = new Random();
		int randomValue = random.nextInt(3) + 1;
		return randomValue;
	}

	public PostType getSelectPostType() {
		return selectPostType;
	}

	public void setSelectPostType(PostType selectPostType) {
		this.selectPostType = selectPostType;
	}

	public List<Village> getVillages() {
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public Village getSelecteVillage() {
		return selecteVillage;
	}

	public void setSelecteVillage(Village selecteVillage) {
		this.selecteVillage = selecteVillage;
	}

	public List<Governorate> getGovernorates() {
		return governorates;
	}

	public void setGovernorates(List<Governorate> governorates) {
		this.governorates = governorates;
	}

	public Governorate getSelecteGovernorate() {
		return selecteGovernorate;
	}

	public void setSelecteGovernorate(Governorate selecteGovernorate) {
		this.selecteGovernorate = selecteGovernorate;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	public District getSelecteDistrict() {
		return selecteDistrict;
	}

	public void setSelecteDistrict(District selecteDistrict) {
		this.selecteDistrict = selecteDistrict;
	}

	public RealEstateLazyDataModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(RealEstateLazyDataModel lazyModel) {
		this.lazyModel = lazyModel;
	}

	public GovernorateFacade getGovernorateFacade() {
		return governorateFacade;
	}

	public void setGovernorateFacade(GovernorateFacade governorateFacade) {
		this.governorateFacade = governorateFacade;
	}

	public DistrictFacade getDistrictFacade() {
		return districtFacade;
	}

	public void setDistrictFacade(DistrictFacade districtFacade) {
		this.districtFacade = districtFacade;
	}

	public VillageFacade getVillageFacade() {
		return villageFacade;
	}

	public void setVillageFacade(VillageFacade villageFacade) {
		this.villageFacade = villageFacade;
	}

	public RealEstateFacade getRealEstateFacade() {
		return realEstateFacade;
	}

	public void setRealEstateFacade(RealEstateFacade realEstateFacade) {
		this.realEstateFacade = realEstateFacade;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
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

	public AtomicLong getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(AtomicLong totalCount) {
		this.totalCount = totalCount;
	}

	public int getBedRoom() {
		return bedRoom;
	}

	public void setBedRoom(int bedRoom) {
		this.bedRoom = bedRoom;
	}

	public boolean isBedRoomEq() {
		return bedRoomEq;
	}

	public void setBedRoomEq(boolean bedRoomEq) {
		this.bedRoomEq = bedRoomEq;
	}

	public int getBathRoom() {
		return bathRoom;
	}

	public void setBathRoom(int bathRoom) {
		this.bathRoom = bathRoom;
	}

	public boolean isBathRoomEq() {
		return bathRoomEq;
	}

	public void setBathRoomEq(boolean bathRoomEq) {
		this.bathRoomEq = bathRoomEq;
	}

}
