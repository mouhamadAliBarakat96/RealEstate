package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.RealEstate.facade.ContactUsFacade;
import org.RealEstate.model.ContactUs;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class ContactUsController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String phoneNumber;

	private ContactUs contactUs = new ContactUs();;

	@Inject
	private ContactUsFacade contactUsFacade;

	@Inject
	private AppSinglton appSinglton;

	@Inject
	private HttpServletRequest request;

	private final String RQUEST_FROM = "from";

	private boolean showErrorMessage;

	private boolean showMessageSuccess;

	private User user;

	@PostConstruct
	public void init() {
		showErrorMessage = false;
		phoneNumber = appSinglton.getPhoneNumber();
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

		if (flash.containsKey("message-success")) {

			showMessageSuccess = true;
		}

	}

	public boolean checkAuthentication() throws IOException {
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute(Constants.USER_SESSION);
		return user != null;

	}

	public void save() {

		try {
			boolean userSignIn = checkAuthentication();

			if (userSignIn) {
				contactUs.setUser(user);
				contactUsFacade.save(contactUs);
				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("message-success", "true");

				changeUrl();
			} else {
				showErrorMessage = true;
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void changeUrl() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		url = Utils.replaceHost(url, appSinglton.getRealDns(), appSinglton.getMode());

		Faces.redirect(url);

	}

	public ContactUs getContactUs() {
		return contactUs;
	}

	public void setContactUs(ContactUs contactUs) {
		this.contactUs = contactUs;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isShowErrorMessage() {
		return showErrorMessage;
	}

	public void setShowErrorMessage(boolean showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	public boolean isShowMessageSuccess() {
		return showMessageSuccess;
	}

	public void setShowMessageSuccess(boolean showMessageSuccess) {
		this.showMessageSuccess = showMessageSuccess;
	}

}
