package org.RealEstate.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.dto.ImageDto;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.UserCategory;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.GoogleMapAttribute;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

@Named
@ViewScoped
public class UserChaletCardController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM_ID = "id";

	private PropertyKindEnum kindEnum = PropertyKindEnum.CHALET;

	@Inject
	private UserService userService;

	@Inject
	private LanguageController sessionLanguage;

	@Inject
	private ChaletFacade chaletFacade;

	@Inject
	private VillageFacade villageFacade;

	@Inject
	private AppSinglton appSinglton;

	@Inject
	private HttpServletRequest request;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	private List<Village> villages = new ArrayList<Village>();

	private Chalet chalet = new Chalet();

	private User user;

	private TabView tabView;
	private int activeTabIndex = 0;

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

	private boolean lockPage = false;

	@PostConstruct
	public void init() {

		user = getUser();
		if (user == null) {
			try {
				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext externalContext = context.getExternalContext();
				String url = externalContext.getRequestServletPath();
				externalContext.redirect("/user-login.xhtml?from=" + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
					.concat(Constants.POST_IMAGE_DIR_NAME).concat("/");
			villages = villageFacade.findAll();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			if (!facesContext.isPostback()) {
				id = externalContext.getRequestParameterMap().get(REQUEST_PARAM_ID);
				if (!StringUtils.isBlank(id)) {
					readTheParamshValue(id);
					lockPage = true;
				} else {
					// add new post
					chalet = new Chalet();
					lockPage = false;
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
			ipAddressWithPort = "http://" + ipAddress + ":" + request.getLocalPort();
		} else {
			ipAddressWithPort = "https://" + appSinglton.getRealDns();
		}

		return ipAddressWithPort;
	}

	public void handleFileUploadChalet(FileUploadEvent event) {
		String fileName = event.getFile().getFileName().toLowerCase();
		if (list.stream().noneMatch(x -> x.getName().equals(fileName)))
			list.add(new ImageDto(fileName, event.getFile().getContent()));
	}

	public byte[] getByteFromBufferedIamge(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		byte[] imageBytes = baos.toByteArray();
		baos.close();
		return imageBytes;
	}

	public List<String> uploadToChalet(List<ImageDto> images) {
		try {
			return uploadImagesMultiPart.uploadImagePostFrontEnd(list);
		} catch (IOException e) {
			e.printStackTrace();
			Utility.addErrorMessage("error_in_upload_images", sessionLanguage.getLocale());
			return new ArrayList<>();
		}
	}

	public void readTheParamshValue(String id) {
		if (StringUtils.isBlank(id) || Long.parseLong(id) <= 0) {
			chalet = new Chalet();
			setActiveTabIndex(1);
		} else {
			chalet = findChaletWithId(id);
			if (chalet == null) {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			setActiveTabIndex(0);
			setCoordinates(chalet.getName(), chalet.getAddressEmbeddable().getLatitude(),
					chalet.getAddressEmbeddable().getLongitude());
		}
	}

	public void setCoordinates(String title, double lat, double lng) {
		this.title = title;
		this.lat = lat;
		this.lng = lng;
	}

	public Chalet findChaletWithId(String id) {
		if (id != null && Long.parseLong(id) > 0) {
			return chaletFacade.find(Long.parseLong(id));
		}
		return null;
	}

	public void save() {
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
			}

			chalet.addToImages(uploadToChalet(list));
			chalet.setAddressEmbeddable(new GoogleMapAttribute(lat, lng));
			chalet.setPostStatus(PostStatus.PENDING);
			chalet = chaletFacade.save(chalet);

			changeUrl(chalet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean chaletValidationFields() {
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

			if (list.size() == 0 && chalet.getImages().size() == 0) {
				Utility.addWarningMessage("please_add_at_least_one_photo", sessionLanguage.getLocale());
				emptyFields = true;
			}
		}

		return emptyFields;
	}

	private void changeUrl(Chalet chalet) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns(), appSinglton.getMode());
		try {
			Faces.redirect(url + "?id=" + chalet.getId());// + "&kind=" + PropertyKindEnum.CHALET);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeIndex) {
		this.activeTabIndex = activeIndex;
	}

	public void setActiveTab(int index) {
		setActiveTabIndex(index);
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

	/*
	 * public void changeActiveGalleriaIndex() { Map<String, String> params =
	 * FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap
	 * (); this.galleriaIndex = Integer.valueOf(params.get("index")); }
	 */

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

	public void deleteChaletPhoto(String image) {
		this.chalet.removeFromPhotos(image);
	}

	public String displayFirstImageChalet() {
		if (chalet != null && !chalet.getImages().isEmpty()) {
			return fullUrl.concat(chalet.getImages().get(0));
		} else {
			return Utility.NO_PHOTO;
		}
	}

	public boolean canAddNewPost() {
		try {
			if (chalet != null && chalet.getId() > 0)
				return true;
			else
				return user != null && userService.findNumberOfPostForUser(user) > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public int totalPermitPost() {
		int nbOfPermitPost = 0;
		if (user == null || user.getUserCategory() == null) {
			return 0;
		} else {

			if (user.getUserCategory() == UserCategory.REGULAR) {
				nbOfPermitPost = appSinglton.getFreeNbOfPost();

			} else if (user.getUserCategory() == UserCategory.MEDUIM) {
				nbOfPermitPost = appSinglton.getMeduimAccountNbOfPost();

			} else if (user.getUserCategory() == UserCategory.PREMIUM) {
				nbOfPermitPost = appSinglton.getPremuimAccountNbOfPost();
			}

			return nbOfPermitPost;
		}

	}

	public int userPermitPost() {
		try {
			return ((Number) userService.findNumberOfPostForUser(user)).intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean showChaletStatus() {
		return chalet != null && chalet.getId() > 0 && chalet.getPostStatus() != PostStatus.REFFUSED
				&& chalet.getPostStatus() != PostStatus.TO_REVIEUX_BY_USER;
	}

	public void unlcokPage() {
		lockPage = false;
	}

	public boolean isLockPage() {
		return lockPage;
	}

	public void setLockPage(boolean lockPage) {
		this.lockPage = lockPage;
	}

	public boolean photosChaletAvailable() {
		if (chalet != null && chalet.getImages().size() >= Constants.NB_IMAGE_IN_POST_ALLOWED) {
			return false;
		} else {
			return true;
		}

	}

	public int nbPhotosChaletAvailable() {
		if (chalet == null) {
			return 0;
		} else {
			return Constants.NB_IMAGE_IN_POST_ALLOWED - chalet.getImages().size();
		}
	}

}
