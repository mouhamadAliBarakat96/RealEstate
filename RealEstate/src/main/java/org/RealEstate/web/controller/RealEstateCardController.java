package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.RealEstate.service.PostService;
import org.RealEstate.utils.Constants;
import org.omnifaces.util.Faces;
import org.primefaces.model.ResponsiveOption;

@Named
@ViewScoped
public class RealEstateCardController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String REQUEST_PARAM = "id";
	private final String NO_PHOTO = "nophoto.jpg";
	private RealEstate item;
	private List<ResponsiveOption> responsiveOptions1;
	private int activeIndex = 0;

	@Inject
	private RealEstateFacade facade;

	private String fullUrl = "";
	private String ipAddressWithPort;

	private String fullUrlProfilePicture = "";

	@EJB
	private PostService postService;

	@PostConstruct
	public void init() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		addToResponsiveImages();
		if (!facesContext.isPostback()) {
			String id = externalContext.getRequestParameterMap().get(REQUEST_PARAM);

			if (id != null && Long.parseLong(id) > 0) {
				// find item by id

				item = facade.find(Long.parseLong(id));

				if (item == null) {
					try {
						Faces.redirect("/error.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/")
							.concat(Constants.IMAGES).concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

					fullUrlProfilePicture = fullUrlProfilePicture.concat("http://").concat(getIpAddressWithPort())
							.concat("/").concat(Constants.IMAGES).concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME)
							.concat("/");
					if (item.getUser() != null && item.getUser().getProfileImageUrl() != null) {
						fullUrlProfilePicture = fullUrlProfilePicture.concat(item.getUser().getProfileImageUrl());
					}

				}
			} else {
				try {
					Faces.redirect("/error.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		this.activeIndex = Integer.valueOf(params.get("index"));
	}

	public void addToResponsiveImages() {
		responsiveOptions1 = new ArrayList<ResponsiveOption>();
		responsiveOptions1.add(new ResponsiveOption("1024px", 5));
		responsiveOptions1.add(new ResponsiveOption("768px", 3));
		responsiveOptions1.add(new ResponsiveOption("560px", 1));
	}

	public void navigateToWhatsApp() throws IOException {
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

	public void addCallNumber() {
		try {
			Response response = postService.updateCallPost(item.getId(), item.getPostType().toString());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;
		System.out.println(ipAddressWithPort);
		return ipAddressWithPort;
	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {
			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", item.getId() + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RealEstate getItem() {
		return item;
	}

	public void setItem(RealEstate item) {
		this.item = item;
	}

	public List<ResponsiveOption> getResponsiveOptions1() {
		return responsiveOptions1;
	}

	public void setResponsiveOptions1(List<ResponsiveOption> responsiveOptions1) {
		this.responsiveOptions1 = responsiveOptions1;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public boolean hasRoomsAndBathRoomsAndFloor(PostType type) {
		return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
				|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL));
	}

	public boolean hasElevator(PostType type) {// why not shop sell ?
		return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
				|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL));
	}

	public boolean hasPark(PostType type) {
		return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL)
				|| type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL)
				|| type.equals(PostType.SHOP_RENT) || type.equals(PostType.SHOP_SELL));
	}

	public boolean hasGarden(PostType type) {
		return (type.equals(PostType.APPRATMENT_RENT) || type.equals(PostType.APPRATMENT_SELL));
	}

	public boolean hasGreenBand(PostType type) {
		return (type.equals(PostType.OFFICE_SELL) || type.equals(PostType.APPRATMENT_SELL)
				|| type.equals(PostType.SHOP_SELL) || type.equals(PostType.LAND));
	}

	public boolean hasBlockNo(PostType type) {// why not appart Rent
		return (type.equals(PostType.OFFICE_RENT) || type.equals(PostType.OFFICE_SELL)
				|| type.equals(PostType.APPRATMENT_SELL) || type.equals(PostType.SHOP_RENT)
				|| type.equals(PostType.SHOP_SELL) || type.equals(PostType.LAND));
	}

	public boolean aShop(PostType type) {
		return (type.equals(PostType.SHOP_SELL) || type.equals(PostType.SHOP_RENT));
	}

	public boolean aLand(PostType type) {
		return (type.equals(PostType.LAND));
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getFullUrlProfilePicture() {
		return fullUrlProfilePicture;
	}

	public void setFullUrlProfilePicture(String fullUrlProfilePicture) {
		this.fullUrlProfilePicture = fullUrlProfilePicture;
	}

	public String displayFirstImage() {
		if(item!=null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		}else {
			return fullUrl.concat(NO_PHOTO);
		}
	}
}
