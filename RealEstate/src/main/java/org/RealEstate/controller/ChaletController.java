package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named

public class ChaletController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	@Param(name = "id")
	private long id;

	private Chalet chalet;
	private String ipAddressWithPort;
	@EJB
	private ChaletFacade chaletFacade;
	private String fullUrl = "";

	private final String REQUEST_PARAM = "id";

	@PostConstruct
	public void init() {

		if (id < 0) {
			CommonUtility.addMessageToFacesContext("Id should > 0  ", "error");
		} else {
			chalet = chaletFacade.find(id);

			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			if (flash.containsKey("update-card")) {

				flash.setKeepMessages(true);
				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");
			}

		}

	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;

		return ipAddressWithPort;
	}

	public void save() {

		try {
			if ((chalet.getPostStatus().equals(PostStatus.REFFUSED.toString())
					|| chalet.getPostStatus().equals(PostStatus.TO_REVIEUX_BY_USER.toString()))
					&& chalet.getReffuseCause().isEmpty()) {

				CommonUtility.addMessageToFacesContext("refuse cause  should not be empty  ", "error");

			} else {
				
				mangmentBoost();
				chaletFacade.save(chalet);

				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");

				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("update-card", "true");

				changeUrl();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void mangmentBoost() {
		if (chalet.getBoostEnum() != null) {
			switch (chalet.getBoostEnum()) {
			case BOOST_FOR_3_DAYS:
				chalet.setBoostedUntil(Utils.addDaysToCurrentDate(3));
				chalet.setBoosted(true);
				break;
			case BOOST_FOR_1_WEEK:
				chalet.setBoostedUntil(Utils.addDaysToCurrentDate(7));
				chalet.setBoosted(true);
				break;
			case BOOST_FOR_2_WEEKS:
				chalet.setBoostedUntil(Utils.addDaysToCurrentDate(14));
				chalet.setBoosted(true);
				break;
			case BOOST_FOR_1_MONTH:
				chalet.setBoostedUntil(Utils.addDaysToCurrentDate(30));
				chalet.setBoosted(true);
				break;

			}
		}
	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {

			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", id + "");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Chalet getChalet() {
		return chalet;
	}

	public void setChalet(Chalet chalet) {
		this.chalet = chalet;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
