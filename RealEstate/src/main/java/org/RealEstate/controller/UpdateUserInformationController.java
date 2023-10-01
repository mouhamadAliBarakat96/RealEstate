package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.RealEstate.dto.ImageDto;
import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.service.UserService;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utility;
import org.RealEstate.utils.Utils;
import org.RealEstate.web.controller.LanguageController;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.EscapeUtils;

@ViewScoped
@Named
public class UpdateUserInformationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UserFacade userFacade;

	@EJB
	private UserService userService;

	@Inject
	private HttpServletRequest request;

	@Inject
	private AppSinglton appSinglton;

	private User user;

	@Inject
	private LanguageController sessionLanguage;

	private String fullUrl = "";
	private String ipAddressWithPort;

	private boolean haveProfilePicture;
	private ImageDto imageDto ;
	@Inject
	private UploadImagesMultiPart uploadImagesMultiPart;

	@PostConstruct
	public void init() {
		HttpSession session = request.getSession(true);

		user = (User) session.getAttribute(Constants.USER_SESSION);
		if (user == null) {

			try {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			if (user.getProfileImageUrl() == null) {
				haveProfilePicture = false;
			} else {
				fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
						.concat(Constants.PROFILE_IMAGE_DIR_NAME).concat("/");

				fullUrl = fullUrl.concat(user.getProfileImageUrl());
				haveProfilePicture = true;

			}

		}

	}

	public boolean isHaveProfilePicture() {
		return haveProfilePicture;
	}

	public void setHaveProfilePicture(boolean haveProfilePicture) {
		this.haveProfilePicture = haveProfilePicture;
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

	public void updateUserInformation() {
		try {
			if (imageDto != null) {
				String imageDbName = uploadImagesMultiPart.uploadImageUserProfileFrontEnd(imageDto.getContent(),
						imageDto.getName());

				user.setShowProfilePicture(false);
				user.setProfileImageUrl(imageDbName);
			}

			Response r = userService.updateUserInforamtion(user);
			if (r.getStatus() == Status.ACCEPTED.getStatusCode()) {
				Utility.addSuccessMessage("update_sucess", sessionLanguage.getLocale());
				changeUrl();
			} else {
				 Utility.addErrorMessage(r.getEntity().toString(), sessionLanguage.getLocale());
			}
		}
		catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getMessage(), "error");
			e.printStackTrace();
		}

	}
	
	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns()  , appSinglton.getMode());

		try {

			Faces.redirect(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void importFile(FileUploadEvent event) {
		imageDto = new ImageDto(event.getFile().getFileName(),  event.getFile().getContent());
		
	
		 
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
