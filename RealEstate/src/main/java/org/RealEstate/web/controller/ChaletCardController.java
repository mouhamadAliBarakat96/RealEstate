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

import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.PostService;
import org.RealEstate.utils.Constants;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;
import org.primefaces.model.ResponsiveOption;

@Named
@ViewScoped
public class ChaletCardController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String REQUEST_PARAM = "id";
	private final String NO_PHOTO = "nophoto.jpg";

	@Inject
	private ChaletFacade facade;

	private Chalet item;

	private String fullUrl = "";
	private String ipAddressWithPort;
	private int activeIndex = 0;

	private String fullUrlProfilePicture = "";
	private List<ResponsiveOption> responsiveOptions1;

	@EJB
	private PostService postService;

	@Inject 
	private AppSinglton appSinglton ;
	
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
					fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/")
							.concat(Constants.IMAGES).concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

					fullUrlProfilePicture = fullUrlProfilePicture.concat(getIpAddressWithPort())
							.concat("/").concat(Constants.IMAGES).concat("/").concat(Constants.PROFILE_IMAGE_DIR_NAME)
							.concat("/");
					if (item.getUser().getProfileImageUrl() != null) {
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

	public void addCallNumber_chalet() {
		try {
			Response response = postService.updateChaletCall(item.getId());
			System.out.println(response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void navigateToWhatsApp_chalet() throws IOException {
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

	
	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		String ipAddress = request.getRemoteAddr();

		if (appSinglton.getMode().equals(Constants.DEVELOPMENT)) {
			ipAddressWithPort = "http://" + ipAddress +  ":" + request.getLocalPort() ;
		} else {
			ipAddressWithPort = "https://" + ipAddress ;
		}

		return ipAddressWithPort;
	}
	

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		activeIndex = Integer.valueOf(params.get("index"));
	}

	public Chalet getItem() {
		return item;
	}

	public void setItem(Chalet item) {
		this.item = item;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public void setIpAddressWithPort(String ipAddressWithPort) {
		this.ipAddressWithPort = ipAddressWithPort;
	}

	public String getFullUrlProfilePicture() {
		return fullUrlProfilePicture;
	}

	public void setFullUrlProfilePicture(String fullUrlProfilePicture) {
		this.fullUrlProfilePicture = fullUrlProfilePicture;
	}

	public String displayFirstImage() {
		if (item != null && !item.getImages().isEmpty()) {
			return fullUrl.concat(item.getImages().get(0));
		} else {
			return fullUrl.concat(NO_PHOTO);
		}
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

}
