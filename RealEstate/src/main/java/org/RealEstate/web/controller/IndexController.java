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
import org.RealEstate.enumerator.Country;
import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.PropertyTypeEnum;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.enumerator.YesNoEnum;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
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
	private LanguageController sessionLanguage;
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

	private PostType selectPostType;

	private PropertyKindEnum propertyKind;

	private List<Governorate> governorates = new ArrayList<>();

	private List<Village> villages = new ArrayList<>();

	private List<District> districts = new ArrayList<>();

	private List<Village> allVillages = new ArrayList<>();
	
	// lazy model
	private RealEstateLazyDataModel realLazyModel;
	// Search Bar Filters
	private User user;
	private String postType;
	private int minPrice;
	private int maxPrice=100000000;
	private AtomicLong totalCount = new AtomicLong();
	private List<Integer> bedRooms;
	private List<Integer> bathRooms;
	private boolean bathRoomEq;
	private boolean bedRoomEq;
	private Governorate selecteGovernorate = new Governorate();
	private District selecteDistrict = new District();
	private Village selecteVillage = new Village();

	// chalet lazyModel
	private ChaletLazyDataModel chaletLazyModel;
	// Search Bar Filters
	private YesNoEnum poolYesNoEnum = null;
	private YesNoEnum chimneyYesNoEnum = null;
	private YesNoEnum bathsEqualsEnum = null;
	private YesNoEnum roomsEqualsEnum = null;

	private String fullUrl = "";
	private String ipAddressWithPort;
	

	@Inject
	private  AppSinglton appSinglton ;
	
	private RealEstateTypeEnum realEstateTypeEnum=RealEstateTypeEnum.ALL;
	
	/*use these two filter to get info about real estate type rent or sale*/
	private PropertyTypeEnum propertyTypeEnum=null;
	private ExchangeRealEstateType estateTypeEnum = ExchangeRealEstateType.BUY;
	
	@PostConstruct
	public void init() {
		governorates = governorateFacade.findAll();
		allVillages=villageFacade.findAll();
		villages=allVillages;
		// realEstates = realEstateFacade.findAll();// GET RECOMMEND PROPERTIES LATER
		// totalCount.set(realEstates.size());

		realLazyModel = new RealEstateLazyDataModel(realEstateFacade);
		chaletLazyModel = new ChaletLazyDataModel(chaletFacade);

		fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
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
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		String ipAddress = request.getRemoteAddr();

		if (appSinglton.getMode().equals(Constants.DEVELOPMENT)) {
			ipAddressWithPort = "http://" + ipAddress +  ":" + request.getLocalPort() ;
		} else {
			ipAddressWithPort = "https://" +  appSinglton.getRealDns() ;
		}

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

	public void addViewsAfterClick_chalet(Chalet item) {
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

	public void addCallNumber_chalet(Chalet item) {
		try {
			Response response = postService.updateChaletCall(item.getId());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void navigateToWhatsApp_chalet(Chalet chalet) throws IOException {
		// Get the phone number parameter from the request
		if (chalet.getUser() != null && chalet.getUser().getPhoneNumber() != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			String phoneNumber = Utility.checkPhoneNumber(chalet.getUser().getPhoneNumber(), Country.LEBANON);
			String message = getIpAddressWithPort() + "/chalet-card.xhtml?id=" + chalet.getId();
			// Construct the WhatsApp URL
			String url = "https://api.whatsapp.com/send?phone=" + phoneNumber.replaceAll("\\D+", "")+ "&text=" + message;
			// Navigate to the URL
			externalContext.redirect(url);
		}
	}

	public void navigateToWhatsApp(RealEstate item) throws IOException {
		// Get the phone number parameter from the request
		if (item.getUser() != null && item.getUser().getPhoneNumber() != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			String phoneNumber = Utility.checkPhoneNumber(item.getUser().getPhoneNumber(), Country.LEBANON);
			String message = getIpAddressWithPort() + "/realEstate-card.xhtml?id=" + item.getId();
			// Construct the WhatsApp URL
			String url = "https://api.whatsapp.com/send?phone=" + phoneNumber.replaceAll("\\D+", "")+"&text=" + message;
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
	public void navigate_chalet(Chalet item) {
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
		selecteDistrict = null;
		selecteVillage = null;
		
		if (selecteGovernorate != null) {
			villages = new ArrayList<>();
			districts = districtFacade.findByGovernorate(selecteGovernorate.getId());
		}else {
			districts = new ArrayList<>();
			villages = allVillages;
		}

	}

	public void listenerSelectDistrict() {
		selecteVillage = null;
		if (selecteDistrict != null) {
			//villageFacade.findByDisctrict(selecteDistrict.getId());
			villages = allVillages.stream().filter(x->x.getDistrict().equals(selecteDistrict)).collect(Collectors.toList());
		}else {
			villages = allVillages;
		}
	}
	
	public void listenerSelectVillages() {
		if (selecteVillage != null) {
			 selecteDistrict=selecteVillage.getDistrict();
			 selecteGovernorate=selecteDistrict.getGovernorate();
		}
	}
	
	

	public void search() {

		// boolean searchFeildsError=false;

		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max",sessionLanguage.getLocale());
			return;
		}
		
		if (!contaisRoomsFilter()) {
			bathRooms = new ArrayList<>();
			bedRooms = new ArrayList<>();
		}

		realLazyModel.setBathRooms(bathRooms);
		realLazyModel.setUser(user != null ? user : null);
		realLazyModel.setBathRoomEq(exactValueBaths());
		realLazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		realLazyModel.setGovernorate(
				selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		realLazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);
		realLazyModel.setBedRooms(bedRooms);
		realLazyModel.setBedRoomEq(exactValueRooms());
		realLazyModel.setMaxPrice(maxPrice);
		realLazyModel.setMinPrice(minPrice);
		realLazyModel.setPostType(Utility.findRealEstateClassType(estateTypeEnum, propertyTypeEnum));
		realLazyModel.setExchangeRealEstateType(estateTypeEnum);
		// RealLazyModel.setTotalCount(totalCount);

		Utility.addSuccessMessage("search_complete",sessionLanguage.getLocale());
	}

	
	public void changeValue(ExchangeRealEstateType value) {
		this.estateTypeEnum = value;
	}
	
	public void chaletSearch() {
		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max",sessionLanguage.getLocale());
			return;
		}
		chaletLazyModel.setUser(user != null ? user : null);
		chaletLazyModel.setChimney(chimneyValue());
		chaletLazyModel.setPool(poolValue());
		chaletLazyModel.setMaxPrice(maxPrice);
		chaletLazyModel.setMinPrice(minPrice);
		chaletLazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		chaletLazyModel.setGovernorate(
				selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		chaletLazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);

		Utility.addSuccessMessage("search_complete",sessionLanguage.getLocale());

	}

	private Boolean poolValue() {
		if (poolYesNoEnum == null)
			return null;
		else if (poolYesNoEnum == YesNoEnum.YES)
			return true;
		else
			return false;
	}

	private Boolean chimneyValue() {
		if (chimneyYesNoEnum == null)
			return null;
		else if (chimneyYesNoEnum == YesNoEnum.YES)
			return true;
		else
			return false;
	}

	public boolean apperBathsAndRoomsInSearch(PropertyTypeEnum type) {
		if (type == null) {
			return true;
		} else
			return (type == PropertyTypeEnum.APPRATMENT || type == PropertyTypeEnum.OFFICE);
	}
	
	public boolean hasRoomsAndBathRooms(PostType type) {
		if (type == null) {
			return true;
		} else
			return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
					|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL));
	}
	
	public boolean aLand(PostType type) {
		return (type.equals(PostType.LAND));
	}

	private boolean exactValueBaths() {
		if (bathsEqualsEnum == YesNoEnum.YES)
			return true;
		else
			return false;
	}

	private boolean exactValueRooms() {
		if (roomsEqualsEnum == YesNoEnum.YES)
			return true;
		else
			return false;
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

	 

	public boolean isBedRoomEq() {
		return bedRoomEq;
	}

	public void setBedRoomEq(boolean bedRoomEq) {
		this.bedRoomEq = bedRoomEq;
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

	public YesNoEnum getPoolYesNoEnum() {
		return poolYesNoEnum;
	}

	public void setPoolYesNoEnum(YesNoEnum poolYesNoEnum) {
		this.poolYesNoEnum = poolYesNoEnum;
	}

	public YesNoEnum getChimneyYesNoEnum() {
		return chimneyYesNoEnum;
	}

	public void setChimneyYesNoEnum(YesNoEnum chimneyYesNoEnum) {
		this.chimneyYesNoEnum = chimneyYesNoEnum;
	}

	public long numberOfSellAppartments() {
		return realEstateFacade.findCountWithType(PostType.APPRATMENT_SELL);
	}

	public long numberOfRentAppartments() {
		return realEstateFacade.findCountWithType(PostType.APPRATMENT_SELL);
	}

	public long numberOfLands() {
		return realEstateFacade.findCountWithType(PostType.LAND);
	}

	public long numberOfSellOffices() {
		return realEstateFacade.findCountWithType(PostType.OFFICE_SELL);
	}

	public long numberOfRentOffices() {
		return realEstateFacade.findCountWithType(PostType.OFFICE_RENT);
	}

	public int numberOfChalet() {
		return chaletFacade.count();
	}

	public YesNoEnum getBathsEqualsEnum() {
		return bathsEqualsEnum;
	}

	public void setBathsEqualsEnum(YesNoEnum bedsEqualsEnum) {
		this.bathsEqualsEnum = bedsEqualsEnum;
	}

	public YesNoEnum getRoomsEqualsEnum() {
		return roomsEqualsEnum;
	}

	public void setRoomsEqualsEnum(YesNoEnum roomsEqualsEnum) {
		this.roomsEqualsEnum = roomsEqualsEnum;
	}

	public ExchangeRealEstateType getEstateTypeEnum() {
		return estateTypeEnum;
	}

	public void setEstateTypeEnum(ExchangeRealEstateType estateTypeEnum) {
		this.estateTypeEnum = estateTypeEnum;
	}

	public void changeTypeRealEstateSearch(ExchangeRealEstateType type) {
		this.estateTypeEnum = type;
	}
	
	public String displayFirstImageReal(RealEstate item) {
		if(item!=null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		}else {
			return Utility.NO_PHOTO;
		}
	}
	
	public String displayFirstImageChalet(Chalet item) {
		if(item!=null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		}else {
			return Utility.NO_PHOTO;
		}
	}

	public RealEstateTypeEnum getRealEstateTypeEnum() {
		return realEstateTypeEnum;
	}

	public void setRealEstateTypeEnum(RealEstateTypeEnum realEstateTypeEnum) {
		this.realEstateTypeEnum = realEstateTypeEnum;
	}

	public List<Integer> getBedRooms() {
		return bedRooms;
	}

	public void setBedRooms(List<Integer> bedRooms) {
		this.bedRooms = bedRooms;
	}

	public List<Integer> getBathRooms() {
		return bathRooms;
	}

	public void setBathRooms(List<Integer> bathRooms) {
		this.bathRooms = bathRooms;
	}

	public PropertyTypeEnum getPropertyTypeEnum() {
		return propertyTypeEnum;
	}

	public void setPropertyTypeEnum(PropertyTypeEnum propertyTypeEnum) {
		this.propertyTypeEnum = propertyTypeEnum;
	}
	
	public String currentUrl(ExternalContext externalContext) {
		return externalContext.getRequestContextPath() + externalContext.getRequestServletPath();
	}
	
	public boolean contaisRoomsFilter() {
		return propertyTypeEnum == PropertyTypeEnum.APPRATMENT || propertyTypeEnum == PropertyTypeEnum.OFFICE;
	}
}
