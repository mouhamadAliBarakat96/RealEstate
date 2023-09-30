package org.RealEstate.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.controller.AbstractController;
import org.RealEstate.dto.ImageDto;
import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.PropertyTypeEnum;
import org.RealEstate.enumerator.UserCategory;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.GoogleMapAttribute;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

@Named
@ViewScoped
public class UserPostCardController extends AbstractController<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM_ID = "id";
	private final String REQUEST_PARAM_KIND = "kind";
	 

	private PostType postType=PostType.APPRATMENT_RENT;
	private PropertyKindEnum kindEnum = PropertyKindEnum.REALESTATE;
	
	@Inject
	private LanguageController sessionLanguage;
	@Inject
	private ChaletFacade chaletFacade;
	@Inject
	private RealEstateFacade estateFacade;
	@Inject
	private VillageFacade villageFacade;
	@Inject
	private AppSinglton appSinglton;

	private List<Village> villages = new ArrayList<Village>();

	private RealEstate item;
	private Chalet chalet = new Chalet();

	private User user;

	private TabView tabView;
	private int activeIndex = 0;

	@Inject
	private HttpServletRequest request;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	private List<ImageDto> list = new ArrayList<>();
	private UploadedFiles files;
	private String id = null;
	private String kind = null;

	private MapModel mapModel = new DefaultMapModel();
	private String title;
	private double lat;
	private double lng;
	
	private List<ResponsiveOption> responsiveOptions1;
	private int galleriaIndex = 0;
	private String fullUrl = "";
	private String ipAddressWithPort;

	
	private PropertyTypeEnum propertyTypeEnum=PropertyTypeEnum.APPRATMENT;
	private ExchangeRealEstateType exchangeRealEstateType=ExchangeRealEstateType.RENT;
	
	@PostConstruct
	public void init() {

		user = getUser();
		if (user == null) {
			try {
				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext externalContext=context.getExternalContext();
				String url = externalContext.getRequestServletPath();
				externalContext.redirect("/user-login.xhtml?from=" + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");
			villages = villageFacade.findAll();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			if (!facesContext.isPostback()) {
				id = externalContext.getRequestParameterMap().get(REQUEST_PARAM_ID);
				kind = externalContext.getRequestParameterMap().get(REQUEST_PARAM_KIND);
				if (!StringUtils.isBlank(id) && !StringUtils.isBlank(kind))
					readTheParamshValue(id, kind);
				else {
					// add new post
					chalet = new Chalet();
					item = new AppratmentRent();
				}

			}
		}
	}

	public User getUser() {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute(Constants.USER_SESSION);
		return user;
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
	
	
	public void handleFileUploadReal(FileUploadEvent event) {
		String fileName = event.getFile().getFileName();

		if (list.stream().anyMatch(x -> x.getName().equals(fileName))) {
			Utility.addWarningMessage("image_duplicated", sessionLanguage.getLocale());
		} else {
			list.add(new ImageDto(fileName, event.getFile().getContent()));
			item.addToImages(uploadToReal(list));
			Utility.addSuccessMessage("success_upload", sessionLanguage.getLocale());
		}

	}

	public void handleFileUploadChalet(FileUploadEvent event) {
		String fileName = event.getFile().getFileName();

		if (list.stream().anyMatch(x -> x.getName().equals(fileName))) {
			Utility.addWarningMessage("image_duplicated", sessionLanguage.getLocale());

		} else {
			list.add(new ImageDto(event.getFile().getFileName(), event.getFile().getContent()));
			chalet.addToImages(uploadToReal(list));
			Utility.addSuccessMessage("success_upload", sessionLanguage.getLocale());
		}

	}

	public byte[] getByteFromBufferedIamge(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		byte[] imageBytes = baos.toByteArray();
		baos.close();
		return imageBytes;
	}

	public List<String> uploadToReal(List<ImageDto> images) {
		try {
			return uploadImagesMultiPart.uploadImagePostFrontEnd(list);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> uploadToChalet(List<ImageDto> images) {
		try {
			return uploadImagesMultiPart.uploadImagePostFrontEnd(list);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void readTheParamshValue(String id, String kind) {
		if (kind.equals(PropertyKindEnum.REALESTATE.toString())) {// ADD EDIT RealEstate
			if (StringUtils.isBlank(id) || Long.parseLong(id) <= 0) {
				kindEnum = PropertyKindEnum.REALESTATE;
				item = null;
				setActiveIndex(0);
			} else {
				kindEnum = PropertyKindEnum.REALESTATE;
				item = findRealWithId(id);
				if (item == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				postType = item.getPostType();
				setActiveIndex(0);
				setCoordinates(item.getTittle(), item.getAddressEmbeddable().getLatitude(),
						item.getAddressEmbeddable().getLongitude());
			}
		} else if (kind.equals(PropertyKindEnum.CHALET.toString())) {// ADD EDIT Chalet
			if (StringUtils.isBlank(id) || Long.parseLong(id) <= 0) {
				kindEnum = PropertyKindEnum.CHALET;
				chalet = new Chalet();
				setActiveIndex(1);

			} else {
				kindEnum = PropertyKindEnum.CHALET;
				chalet = findChaletWithId(id);
				if (chalet == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				setActiveIndex(0);
				setCoordinates(chalet.getName(), chalet.getAddressEmbeddable().getLatitude(),
						chalet.getAddressEmbeddable().getLongitude());
			}
		}
	}

	public void setCoordinates(String title, double lat, double lng) {
		this.title = title;
		this.lat = lat;
		this.lng = lng;
	}

	public RealEstate findRealWithId(String id) {
		if (id != null && Long.parseLong(id) > 0) {
			return estateFacade.find(Long.parseLong(id));
		}
		return null;
	}

	public Chalet findChaletWithId(String id) {
		if (id != null && Long.parseLong(id) > 0) {
			return chaletFacade.find(Long.parseLong(id));
		}
		return null;
	}

	public void listenerSelectItemType() {
		if (postType != null) {
			item = Utility.initializeRealEstate(postType);
		}
	}

	public void onTabChange(TabChangeEvent event) {
		switch (event.getTab().getId()) {
		case "realestate":
			kindEnum = PropertyKindEnum.REALESTATE;
			list=new ArrayList<>();
			break;
		case "chalet":
			kindEnum = PropertyKindEnum.CHALET;
			list=new ArrayList<>();
			break;
		default:
			break;
		}
		// Ajax.update("myForm:panelCoordinates");
	}

	public void save() {
		if (kindEnum == PropertyKindEnum.REALESTATE) {
			saveRealEstate();
		} else if (kindEnum == PropertyKindEnum.CHALET) {
			saveChalet();
		} else {
			Utility.addErrorMessage("no such tab found to save",sessionLanguage.getLocale());
		}
	}

	public void saveRealEstate() {
		try {

			if (realEstatesHasEmptyFields()) {
				return;
			}

			if (realEstateValidationFields()) {
				return;
			}

			if (getItem().getId() <= 0) {
				item.setPostDate(new Date());
				item.setUser(user);
				item.setPostStatus(PostStatus.PENDING);
				item.setPostType(postType);
				item.setAddressEmbeddable(new GoogleMapAttribute(lat, lng));
				item = getAbstractFacade().save(item);
			} else {
				item.setAddressEmbeddable(new GoogleMapAttribute(lat, lng));
				item.setPostStatus(PostStatus.PENDING);
				item = getAbstractFacade().save(item);
				Utility.addSuccessMessage("save_success", sessionLanguage.getLocale());
			}
			
			changeUrl(item);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean realEstateValidationFields() {
		boolean hasErrorEntries = false;

		if (item.getImages().size() > Constants.NB_IMAGE_IN_POST_ALLOWED) {
			Utility.addErrorMessage("YOU_EXCEED_THE_LIMIT_SIZE_OF_PHOTOS",sessionLanguage.getLocale());
			hasErrorEntries = true;
		}

		if (postType == PostType.APPRATMENT_RENT) {
			if (item.getSpace() < 50) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_50",sessionLanguage.getLocale());
				hasErrorEntries = true;

			}
			if (item.getPrice() < 60) {
				Utility.addErrorMessage("PRICE_OF_RENT_SHOULD_BE_GREATER_60",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}

		} else if (postType == PostType.APPRATMENT_SELL) {

			if (item.getSpace() < 50) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_50",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}

			if (item.getSpace() * 100 > item.getPrice()) {
				Utility.addErrorMessage("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}

		} else if (postType == PostType.LAND) {

			if (item.getSpace() < 200) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_200",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
			if (item.getSpace() * 4 > item.getPrice()) {
				Utility.addErrorMessage("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_4_DOLLARS",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
		} else if (postType == PostType.SHOP_RENT) {
			if (item.getPrice() < 60) {
				Utility.addErrorMessage("PRICE_OF_RENT_SHOULD_BE_GREATER_60",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
			if (item.getSpace() < 40) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_40",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
		} else if (postType == PostType.SHOP_SELL) {

			if (item.getSpace() * 100 > item.getPrice()) {
				Utility.addErrorMessage("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
			if (item.getSpace() < 40) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_40",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
		} else if (postType == PostType.OFFICE_RENT) {

			if (item.getPrice() < 60) {
				Utility.addErrorMessage("PRICE_OF_RENT_SHOULD_BE_GREATER_60",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
			if (item.getSpace() < 40) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_40",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}

		} else if (postType == PostType.OFFICE_SELL) {

			if (item.getSpace() * 100 > item.getPrice()) {
				Utility.addErrorMessage("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
			if (item.getSpace() < 40) {
				Utility.addErrorMessage("SPACE_SHOULD_BE_GREATER_40",sessionLanguage.getLocale());
				hasErrorEntries = true;
			}
		}

		return hasErrorEntries;
	}

	public boolean realEstatesHasEmptyFields() {
		boolean hasEmptyField = false;

		if (StringUtils.isBlank(item.getTittle())) {
			hasEmptyField = true;
			Utility.addWarningMessage("title_is_required",sessionLanguage.getLocale());
		}
		
		if (item.getPrice() == 0) {
			hasEmptyField = true;
			Utility.addWarningMessage("price_is_required",sessionLanguage.getLocale());
		}
		if (item.getSpace() == 0) {
			hasEmptyField = true;
			Utility.addWarningMessage("space_is_required",sessionLanguage.getLocale());
		}
		
		if (item.getVillage() == null) {
			Utility.addWarningMessage("village_is_required", sessionLanguage.getLocale());
			hasEmptyField = true;
		}

		if (StringUtils.isBlank(item.getSubTittle())) {
			hasEmptyField = true;
			Utility.addWarningMessage("subtitle_is_required",sessionLanguage.getLocale());
		}

		if (item.getImages().size() == 0) {
			hasEmptyField = true;
			Utility.addWarningMessage("please_add_at_least_one_photo",sessionLanguage.getLocale());
		}

		return hasEmptyField;
	}

	public void saveChalet() {
		try {

			if (chaletHasEmptyFields()) {
				return;
			}

			if (chaletValidationFields()) {
				return;
			}
			
			if (chalet.getId() <= 0) {
				chalet.setPostDate(new Date());
				chalet.setUser(user);
				chalet.setPostStatus(PostStatus.PENDING);
				chalet.setAddressEmbeddable(new GoogleMapAttribute(lat, lng));
				chalet = chaletFacade.save(chalet);
			} else {
				chalet.setPostStatus(PostStatus.PENDING);
				chalet.setAddressEmbeddable(new GoogleMapAttribute(lat, lng));
				chalet = chaletFacade.save(chalet);
			}
			changeUrl(chalet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean chaletValidationFields() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean chaletHasEmptyFields() {
		boolean emptyFields = false;
		
		if (chalet != null) {
			if (StringUtils.isBlank(chalet.getName())) {
				Utility.addWarningMessage("name_is_required", sessionLanguage.getLocale());
				emptyFields = true;
			}

			if (chalet.getVillage() == null) {
				Utility.addWarningMessage("village_is_required", sessionLanguage.getLocale());
				emptyFields = true;
			}

			if (chalet.getWeekdays() == 0) {
				Utility.addWarningMessage("week_days_price_is_required", sessionLanguage.getLocale());
				emptyFields = true;
			}
			
			if (chalet.getWeekenddays() == 0) {
				Utility.addWarningMessage("week_end_price_is_required", sessionLanguage.getLocale());
				emptyFields = true;
			}

			if (chalet.getImages().size() == 0) {
				Utility.addWarningMessage("please_add_at_least_one_photo", sessionLanguage.getLocale());
				emptyFields = true;
			}
		}

		return emptyFields;
	}

	public boolean hasRoomsBathFloorElevator() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL));
	}

	public boolean hasPark() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL)
				|| postType.equals(PostType.SHOP_RENT) || postType.equals(PostType.SHOP_SELL));
	}

	public boolean hasGarden() {
		return (postType.equals(PostType.APPRATMENT_RENT) || postType.equals(PostType.APPRATMENT_SELL));
	}

	public boolean hasGreenBand() {
		return (postType.equals(PostType.OFFICE_SELL) || postType.equals(PostType.APPRATMENT_SELL)
				|| postType.equals(PostType.SHOP_SELL) || postType.equals(PostType.LAND));
	}

	public boolean hasBlockNo() {
		return (postType.equals(PostType.OFFICE_RENT) || postType.equals(PostType.OFFICE_SELL)
				|| postType.equals(PostType.APPRATMENT_SELL) || postType.equals(PostType.APPRATMENT_RENT)
				|| postType.equals(PostType.SHOP_RENT) || postType.equals(PostType.SHOP_SELL)
				|| postType.equals(PostType.LAND));
	}

	public boolean aShop() {
		return (postType.equals(PostType.SHOP_SELL) || postType.equals(PostType.SHOP_RENT));
	}

	public boolean aLand() {
		return (postType.equals(PostType.LAND));
	}

	private void changeUrl(Chalet chalet) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns() , appSinglton.getMode());
		try {
			Faces.redirect(url + "?id=" + chalet.getId() + "&kind=" + PropertyKindEnum.CHALET);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private void changeUrl(RealEstate estate) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns() , appSinglton.getMode());
		try {
			Faces.redirect(url + "?id=" + estate.getId() + "&kind=" + PropertyKindEnum.REALESTATE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public RealEstate getItem() {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public void setItem(RealEstate item) {
		// TODO Auto-generated method stub
		this.item = item;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return item != null ? item.getId() : -1;
	}

	@Override
	public ICRUDOperations<RealEstate> getAbstractFacade() {
		// TODO Auto-generated method stub
		return estateFacade;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	public List<Village> getVillages() {
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public Chalet getChalet() {
		return chalet;
	}

	public void setChalet(Chalet chalet) {
		this.chalet = chalet;
	}

	public PropertyKindEnum getKindEnum() {
		return kindEnum;
	}

	public void setKindEnum(PropertyKindEnum kindEnum) {
		this.kindEnum = kindEnum;
	}

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public void setActiveTab(int index) {
		setActiveIndex(index);
		tabView.setActiveIndex(index);
	}

	public List<ImageDto> getList() {
		return list;
	}

	public void setList(List<ImageDto> list) {
		this.list = list;
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean disableSelectOneButton() {
		return !StringUtils.isBlank(this.id) || !StringUtils.isBlank(this.kind);
	}

	public boolean hideChaletTab() {
		if (!StringUtils.isBlank(this.id) && !StringUtils.isBlank(this.kind)
				&& this.kind.equals(PropertyKindEnum.REALESTATE.toString())) {
			return true;
		}
		return false;
	}

	public boolean hideRealEsateTab() {
		if (!StringUtils.isBlank(this.id) && !StringUtils.isBlank(this.kind)
				&& this.kind.equals(PropertyKindEnum.CHALET.toString())) {
			return true;
		}
		return false;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel emptyModel) {
		this.mapModel = emptyModel;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addMarker() {
		mapModel.getMarkers().clear();
		Ajax.oncomplete("loadPointOnMap();");
	}
	
	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		this.galleriaIndex = Integer.valueOf(params.get("index"));
	}
	public void addToResponsiveImages() {
		responsiveOptions1 = new ArrayList<ResponsiveOption>();
		responsiveOptions1.add(new ResponsiveOption("1024px", 5));
		responsiveOptions1.add(new ResponsiveOption("768px", 3));
		responsiveOptions1.add(new ResponsiveOption("560px", 1));
	}

	public List<ResponsiveOption> getResponsiveOptions1() {
		return responsiveOptions1;
	}

	public void setResponsiveOptions1(List<ResponsiveOption> responsiveOptions1) {
		this.responsiveOptions1 = responsiveOptions1;
	}

	public int getGalleriaIndex() {
		return galleriaIndex;
	}

	public void setGalleriaIndex(int galleriaIndex) {
		this.galleriaIndex = galleriaIndex;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public void deleteRealPhoto(String image) {
		this.item.removeFromPhotos(image);
	}

	public void deleteChaletPhoto(String image) {
		this.chalet.removeFromPhotos(image);
	}

	public PropertyTypeEnum getPropertyTypeEnum() {
		return propertyTypeEnum;
	}

	public void setPropertyTypeEnum(PropertyTypeEnum propertyTypeEnum) {
		this.propertyTypeEnum = propertyTypeEnum;
	}
	
	
	
	public ExchangeRealEstateType getExchangeRealEstateType() {
		return exchangeRealEstateType;
	}

	public void setExchangeRealEstateType(ExchangeRealEstateType exchangeRealEstateType) {
		this.exchangeRealEstateType = exchangeRealEstateType;
	}

	public void listenerSelectProperty() {
		postType = Utility.findRealEstateType(exchangeRealEstateType, propertyTypeEnum);
		if (postType != null) {
			item = Utility.initializeRealEstate(postType);
			if(postType ==PostType.LAND) {
				exchangeRealEstateType=ExchangeRealEstateType.BUY;
			}
		}
	}
	
	public String displayFirstImageReal() {
		if(item!=null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		}else {
			return Utility.NO_PHOTO;
		}
	}
	
	public String displayFirstImageChalet() {
		if(chalet!=null && !chalet.getImages().isEmpty()) {
			return fullUrl.concat(chalet.getImages().get(0));
		}else {
			return Utility.NO_PHOTO;
		}
	}
	
	public boolean checkUserAccountPosts() {

		int nbOfCreatedPost = 0;// get it by query

		int nbOfPermitPost=0;

		if (user.getUserCategory() == UserCategory.REGULAR) {
			nbOfPermitPost = appSinglton.getFreeNbOfPost();

		} else if (user.getUserCategory() == UserCategory.MEDUIM) {
			nbOfPermitPost = appSinglton.getMeduimAccountNbOfPost();

		} else if (user.getUserCategory() == UserCategory.PREMIUM) {
			nbOfPermitPost = appSinglton.getPremuimAccountNbOfPost();
		}

		if (nbOfCreatedPost >= nbOfPermitPost) {
			return true;
		} else {
			return false;
		}

	}
	
}
