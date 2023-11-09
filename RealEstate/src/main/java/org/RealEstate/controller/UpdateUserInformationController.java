package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import org.RealEstate.enumerator.Country;
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
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;

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
	private ImageDto imageDto;
	@Inject
	private UploadImagesMultiPart uploadImagesMultiPart;

	private String newPassword;
	private String verNewPassword;

	private String currentPassword;
	private String lastPhoneNumber;

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
			lastPhoneNumber=user.getPhoneNumber();
			fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
					.concat(Constants.PROFILE_IMAGE_DIR_NAME).concat("/");
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

	public boolean validate() {
		boolean isValid = true;

		if (StringUtils.isBlank(user.getFirstName())) {
			Utility.addErrorMessage("first_name_equried", sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getLastName())) {
			Utility.addErrorMessage("last_name_equried", sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getPhoneNumber())) {
			Utility.addErrorMessage("phone_no_required", sessionLanguage.getLocale());
			isValid = false;
		}

		if (StringUtils.isBlank(user.getUserName())) {
			Utility.addErrorMessage("user_name_required", sessionLanguage.getLocale());
			isValid = false;
		}

		return isValid;
	}

	public void updateUserInformation() {
		try {
			if (!validate()) {
				return;
			}
			
			Response r = userService.updateUserInforamtion(user);
			if (r.getStatus() == Status.ACCEPTED.getStatusCode()) {
				Utility.addSuccessMessage("update_sucess", sessionLanguage.getLocale());
				changeUrl();
			} else {
				user.setPhoneNumber(lastPhoneNumber.replaceAll("\\s+", ""));
				Utility.addErrorMessage(r.getEntity().toString(), sessionLanguage.getLocale());
			}
		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getMessage(), "error");
			e.printStackTrace();
		}

	}

	public void changePassowrd() {
		try {

			boolean hasEmpty = false;

			if (StringUtils.isBlank(currentPassword)) {
				Utility.addErrorMessage("please_fill_current_password", sessionLanguage.getLocale());
				hasEmpty = true;
			}
			if (!Utils.validatePassword(newPassword)) {
				Utility.addErrorMessage("INVALID_PASSWORD", sessionLanguage.getLocale());
				hasEmpty = true;
			}
			if (!Utils.sha256(currentPassword).equals(user.getPassowrd())) {
				Utility.addErrorMessage("wrong_password", sessionLanguage.getLocale());
				hasEmpty = true;
			}
			if (!newPassword.equals(verNewPassword)) {
				Utility.addErrorMessage("password_do_not_match", sessionLanguage.getLocale());
				hasEmpty = true;
			}

			if (hasEmpty) {
				return;
			}

			user.setPassowrd(Utils.sha256(newPassword));
			user = userFacade.save(user);
			Ajax.oncomplete("PF('dlgChangePassword').hide();");
			Utility.addSuccessMessage("change_success", sessionLanguage.getLocale());
		
		} catch (Exception e) {
			e.printStackTrace();
			Utility.addErrorMessage("user_name_required", sessionLanguage.getLocale());
		}

	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns(), appSinglton.getMode());

		try {

			Faces.redirect(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importFile(FileUploadEvent event) {
		imageDto = new ImageDto(event.getFile().getFileName(), event.getFile().getContent());
		try {
			String imageDbName = uploadImagesMultiPart.uploadImageUserProfileFrontEnd(imageDto.getContent(),
					imageDto.getName());
			user.setShowProfilePicture(false);
			user.setProfileImageUrl(imageDbName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String displayProfileInforamtion() {
		if (!StringUtils.isBlank(user.getProfileImageUrl())) {
			return fullUrl.concat(user.getProfileImageUrl());
		} else {
			return "/resources/makaan/img/profile.png";
		}
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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getVerNewPassword() {
		return verNewPassword;
	}

	public void setVerNewPassword(String verNewPassword) {
		this.verNewPassword = verNewPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

}
