package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.RealEstate.dto.ChaletLazyDataModel;
import org.RealEstate.dto.RealEstateLazyDataModel;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.facade.ChaletFacade;
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
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.apache.commons.lang3.StringUtils;

@Named
@ViewScoped
public class IndexController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	@Inject
	private ChaletFacade chaletFacade;

	private List<RealEstate> realEstates = new ArrayList<>();

	private List<Chalet> chaletList = new ArrayList<>();

	private PostType selectPostType;

	private PropertyKindEnum propertyKind;

	private List<Governorate> governorates = new ArrayList<>();

	private List<Village> villages = new ArrayList<>();

	private List<District> districts = new ArrayList<>();

	// lazy model
	private RealEstateLazyDataModel realLazyModel;
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


	// chalet lazyModel
	private ChaletLazyDataModel chaletLazyModel;
	// Search Bar Filters
	private Boolean hasChimney;
	private Boolean hasPool;
	
	private String fullUrl = "";
	private String ipAddressWithPort;

	@PostConstruct
	public void init() {
		governorates = governorateFacade.findAll();

		// realEstates = realEstateFacade.findAll();// GET RECOMMEND PROPERTIES LATER
		// totalCount.set(realEstates.size());

		realLazyModel = new RealEstateLazyDataModel(realEstateFacade);
		chaletLazyModel = new ChaletLazyDataModel(chaletFacade);

		fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
				.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

		propertyKind = parameterPropertyKind();
	}

	private PropertyKindEnum parameterPropertyKind() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (!facesContext.isPostback()) {
			String kind = externalContext.getRequestParameterMap().get("property");
			if (!StringUtils.isBlank(kind) && kind.equals(PropertyKindEnum.REALESTATE.toString())) {
				return PropertyKindEnum.REALESTATE;
			} else if (!StringUtils.isBlank(kind) && kind.equals(PropertyKindEnum.CHALET.toString())) {
				return PropertyKindEnum.CHALET;
			}
		}

		return PropertyKindEnum.REALESTATE;
	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;
		System.out.println(ipAddressWithPort);
		return ipAddressWithPort;
	}

	public void addViewsAfterClick(RealEstate item) {
		try {
			Response response = postService.updatePostVieux(item.getId(), item.getPostType().toString());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addViewsAfterClick(Chalet item) {
		try {
			Response response = postService.updateChaletViews(item.getId());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCallNumber(RealEstate item) {
		try {
			Response response = postService.updateCallPost(item.getId(), item.getPostType().toString());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addCallNumber(Chalet item) {
		try {
			Response response = postService.updateChaletCall(item.getId());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void navigateToWhatsApp(Chalet item) throws IOException {
		// Get the phone number parameter from the request
		if (item.getUser() != null && item.getUser().getPhoneNumber() != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			String phoneNumber = item.getUser().getPhoneNumber();
			// Construct the WhatsApp URL
			String url = "https://api.whatsapp.com/send?phone=" + phoneNumber.replaceAll("\\D+", "");
			// Navigate to the URL
			externalContext.redirect(url);
		}
	}

	public void navigateToWhatsApp(RealEstate item) throws IOException {
		// Get the phone number parameter from the request
		if (item.getUser() != null && item.getUser().getPhoneNumber() != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			String phoneNumber = item.getUser().getPhoneNumber();
			// Construct the WhatsApp URL
			String url = "https://api.whatsapp.com/send?phone=" + phoneNumber.replaceAll("\\D+", "");
			// Navigate to the URL
			externalContext.redirect(url);
		}
	}

	// navigate to real estate Card
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
	// navigate to real chalet Card
	public void navigate(Chalet item) {
		// Build the URL with the parameter values
		String url = "chalet-card.xhtml?id=" + item.getId();
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

		realLazyModel.setBathRoom(bathRoom);
		realLazyModel.setUser(user != null ? user : null);
		realLazyModel.setBathRoomEq(bathRoomEq);
		realLazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		realLazyModel.setGovernorate(
				selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		realLazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);
		realLazyModel.setBedRoom(bedRoom);
		realLazyModel.setBedRoomEq(false);
		realLazyModel.setMaxPrice(maxPrice);
		realLazyModel.setMinPrice(minPrice);
		realLazyModel.setPostType(selectPostType == null ? null : selectPostType.toString());
		// RealLazyModel.setTotalCount(totalCount);

		Utility.addSuccessMessage("search_complete");
	}
	
	public void chaletSearch() {
		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max");
			return;
		}
		chaletLazyModel.setChimney(hasChimney);
		chaletLazyModel.setMaxPrice(maxPrice);
		chaletLazyModel.setMinPrice(minPrice);
		chaletLazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		chaletLazyModel.setGovernorate(selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		chaletLazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);
		chaletLazyModel.setPool(hasPool);
		
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

	public RealEstateLazyDataModel getRealLazyModel() {
		return realLazyModel;
	}

	public void setRealLazyModel(RealEstateLazyDataModel realLazyModel) {
		this.realLazyModel = realLazyModel;
	}

	public ChaletLazyDataModel getChaletLazyModel() {
		return chaletLazyModel;
	}

	public void setChaletLazyModel(ChaletLazyDataModel chaletLazyModel) {
		this.chaletLazyModel = chaletLazyModel;
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

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public PropertyKindEnum getPropertyKind() {
		return propertyKind;
	}

	public void setPropertyKind(PropertyKindEnum propertyKind) {
		this.propertyKind = propertyKind;
	}

	public Boolean getHasChimney() {
		return hasChimney;
	}

	public void setHasChimney(Boolean hasChimney) {
		this.hasChimney = hasChimney;
	}

	public Boolean getHasPool() {
		return hasPool;
	}

	public void setHasPool(Boolean hasPool) {
		this.hasPool = hasPool;
	}

}
