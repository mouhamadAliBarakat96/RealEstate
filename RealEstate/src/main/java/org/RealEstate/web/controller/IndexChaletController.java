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
import org.RealEstate.enumerator.Country;
import org.RealEstate.enumerator.PropertyKindEnum;
import org.RealEstate.enumerator.YesNoEnum;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.DistrictFacade;
import org.RealEstate.facade.GovernorateFacade;
import org.RealEstate.facade.VillageFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.PostService;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;

@Named
@ViewScoped
public class IndexChaletController implements Serializable {

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
	private PostService postService;
	@Inject
	private ChaletFacade chaletFacade;

	private PropertyKindEnum propertyKind = PropertyKindEnum.CHALET;

	private List<Governorate> governorates = new ArrayList<>();

	private List<Village> villages = new ArrayList<>();

	private List<District> districts = new ArrayList<>();

	private List<Village> allVillages = new ArrayList<>();

	// Search Bar Filters
	private User user;
	private String postType;
	private AtomicLong totalCount = new AtomicLong();

	private Governorate selecteGovernorate = new Governorate();
	private District selecteDistrict = new District();

	private ChaletLazyDataModel chaletLazyModel;
	// Search Bar Filters
	private Village selecteVillage = new Village();
	private YesNoEnum poolYesNoEnum = null;
	private YesNoEnum chimneyYesNoEnum = null;
	private int minPrice;
	private int maxPrice = 100000000;

	private String fullUrl = "";
	private String ipAddressWithPort;

	@Inject
	private AppSinglton appSinglton;

	@PostConstruct
	public void init() {
		governorates = governorateFacade.findAll();
		allVillages = villageFacade.findAll();
		villages = allVillages;

		chaletLazyModel = new ChaletLazyDataModel(chaletFacade);

		fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
				.concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

		propertyKind = PropertyKindEnum.CHALET;
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

	public void addViewsAfterClick_chalet(Chalet item) {
		try {
			Response response = postService.updateChaletViews(item.getId());
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
			String url = "https://api.whatsapp.com/send?phone=" + phoneNumber.replaceAll("\\D+", "") + "&text="
					+ message;
			// Navigate to the URL
			externalContext.redirect(url);
		}
	}

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
		} else {
			districts = new ArrayList<>();
			villages = allVillages;
		}

	}

	public void listenerSelectDistrict() {
		selecteVillage = null;
		if (selecteDistrict != null) {
			// villageFacade.findByDisctrict(selecteDistrict.getId());
			villages = allVillages.stream().filter(x -> x.getDistrict().equals(selecteDistrict))
					.collect(Collectors.toList());
		} else {
			villages = allVillages;
		}
	}

	public void listenerSelectVillages() {
		if (selecteVillage != null) {
			selecteDistrict = selecteVillage.getDistrict();
			selecteGovernorate = selecteDistrict.getGovernorate();
		}
	}

	public void chaletSearch() {
		if (maxPrice != 0 && minPrice > maxPrice) {
			Utility.addErrorMessage("min_price_mut_be _less_than_max", sessionLanguage.getLocale());
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

		Utility.addSuccessMessage("search_complete", sessionLanguage.getLocale());

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

	public int random() {
		Random random = new Random();
		int randomValue = random.nextInt(3) + 1;
		return randomValue;
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

	public int numberOfChalet() {
		return chaletFacade.count();
	}

	public String displayFirstImageChalet(Chalet item) {
		if (item != null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		} else {
			return Utility.NO_PHOTO;
		}
	}

}
