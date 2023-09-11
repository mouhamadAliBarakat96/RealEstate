package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.RealEstate.dto.RealEstateLazyDataModel;
import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.enumerator.YesNoEnum;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.PostService;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.omnifaces.cdi.Param;

@Named
@ViewScoped
public class UserPostVieuxController implements Serializable{
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

	private PostType selectPostType;

	private PropertyKindEnum propertyKind;

	private List<Governorate> governorates = new ArrayList<>();

	private List<Village> villages = new ArrayList<>();

	private List<District> districts = new ArrayList<>();

	// lazy model
	private RealEstateLazyDataModel realLazyModel;

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

	// Search Bar Filters
	private YesNoEnum poolYesNoEnum = null;
	private YesNoEnum chimneyYesNoEnum = null;
	private YesNoEnum bathsEqualsEnum = null;
	private YesNoEnum roomsEqualsEnum = null;

	private String fullUrl = "";
	private String ipAddressWithPort;
	private ExchangeRealEstateType estateTypeEnum = null;
	private String fullUrlProfilePicture = "";

	@Inject
	@Param(name = "id")
	private long id;

	@Inject
	private AppSinglton appSinglton ;
	
	@EJB
	private UserFacade userFacade;

	private User user;

	private RealEstateTypeEnum realEstateTypeEnum=RealEstateTypeEnum.ALL;
	
	@PostConstruct
	public void init() {
		governorates = governorateFacade.findAll();

		// realEstates = realEstateFacade.findAll();// GET RECOMMEND PROPERTIES LATER
		// totalCount.set(realEstates.size());

		realLazyModel = new RealEstateLazyDataModel(realEstateFacade);

		fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
				.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

	
		fullUrlProfilePicture = fullUrlProfilePicture.concat("http://").concat(getIpAddressWithPort()).concat("/")
				.concat(Constants.IMAGES).concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME).concat("/");
		if (id > 0) {
			user = userFacade.find(id);

		}
		if (user.getProfileImageUrl() != null) {
			fullUrlProfilePicture = fullUrlProfilePicture.concat(user.getProfileImageUrl());
		}

		propertyKind = parameterPropertyKind();
	}

	private PropertyKindEnum parameterPropertyKind() {
	

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

	public void addCallNumber(RealEstate item) {
		try {
			Response response = postService.updateCallPost(item.getId(), item.getPostType().toString());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
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
	private final String NO_PHOTO = "nophoto.jpg";

	public String displayFirstImageReal(RealEstate item) {
		if (item != null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		} else {
			return fullUrl.concat(NO_PHOTO);
		}
	}

	public void listenerSelectGovernate() {
		villages = new ArrayList<>();
		districts = new ArrayList<>();
		if (selecteGovernorate != null) {
			districts = districtFacade.findByGovernorate(selecteGovernorate.getId());
		}

	}

	public void listenerSelectDistrict() {
		villages = new ArrayList<>();
		if (selecteDistrict != null) {
			villages = villageFacade.findByDisctrict(selecteDistrict.getId());
		}
	}

	public void search() {

		// boolean searchFeildsError=false;

		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max",sessionLanguage.getLocale());
			return;
		}

		realLazyModel.setBathRoom(bathRoom);
		realLazyModel.setUser(user != null ? user : null);
		realLazyModel.setBathRoomEq(exactValueBaths());
		realLazyModel.setDistrict(selecteDistrict != null && selecteDistrict.getId() > 0 ? selecteDistrict : null);
		realLazyModel.setGovernorate(
				selecteGovernorate != null && selecteGovernorate.getId() > 0 ? selecteGovernorate : null);
		realLazyModel.setVillage(selecteVillage != null && selecteVillage.getId() > 0 ? selecteVillage : null);
		realLazyModel.setBedRoom(bedRoom);
		realLazyModel.setBedRoomEq(exactValueRooms());
		realLazyModel.setMaxPrice(maxPrice);
		realLazyModel.setMinPrice(minPrice);
		realLazyModel.setPostType(selectPostType == null ? null : selectPostType.toString());
		realLazyModel.setExchangeRealEstateType(estateTypeEnum);
		// RealLazyModel.setTotalCount(totalCount);

		Utility.addSuccessMessage("search_complete",sessionLanguage.getLocale());
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

	public String getFullUrlProfilePicture() {
		return fullUrlProfilePicture;
	}

	public void setFullUrlProfilePicture(String fullUrlProfilePicture) {
		this.fullUrlProfilePicture = fullUrlProfilePicture;
	}

	public RealEstateTypeEnum getRealEstateTypeEnum() {
		return realEstateTypeEnum;
	}

	public void setRealEstateTypeEnum(RealEstateTypeEnum realEstateTypeEnum) {
		this.realEstateTypeEnum = realEstateTypeEnum;
	}

}
