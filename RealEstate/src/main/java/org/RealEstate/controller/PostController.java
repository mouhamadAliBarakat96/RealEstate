package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.WaterResources;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class PostController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	@Param(name = "id")
	private long id;

	private RealEstate realEstate;
	private String ipAddressWithPort;
	@EJB
	private RealEstateFacade realEstateFacade;
	private String fullUrl = "";

	private final String REQUEST_PARAM = "id";

	private Long nbOfActivePostByThisUser;

	@EJB
	private AppSinglton appSinglton;

	@PostConstruct
	public void init() {

		if (id < 0) {
			CommonUtility.addMessageToFacesContext("Id should > 0  ", "error");
		} else {
			realEstate = realEstateFacade.find(id);
			nbOfActivePostByThisUser = realEstateFacade.findUserCountPostActive(realEstate.getUser().getId());

			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			if (flash.containsKey("update-card")) {

				flash.setKeepMessages(true);
				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");
			}

		}

	}

	public String waterResouces(List<WaterResources> list) {
		list.size();
		String result = "";
		for (WaterResources var : list) {
			result = result.concat(var.toString().toLowerCase()) + ",";
		}
		if (!result.isEmpty()) {
			result = StringUtils.substring(result, 0, result.length() - 1);
		}
		return result;
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
			if ((realEstate.getPostStatus().equals(PostStatus.REFFUSED)
					|| realEstate.getPostStatus().equals(PostStatus.TO_REVIEUX_BY_USER))
					&& realEstate.getReffuseCause().isEmpty()) {

				CommonUtility.addMessageToFacesContext("refuse cause  should not be empty  ", "error");

			} else if (realEstate.getPostStatus().equals(PostStatus.ACCEPTED)
					&& nbOfActivePostByThisUser >= appSinglton.getFreeNbOfPost()) {
				// naaml check lal active 3ndo

				CommonUtility.addMessageToFacesContext("EXCEEDED_POST_LIMIT for this user  ", "error");

			} else {
				realEstateFacade.save(realEstate);

				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");

				Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
				flash.put("update-card", "true");

				changeUrl();
			}

		} catch (	Exception e) {
			e.printStackTrace();
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

	public RealEstate getRealEstate() {
		return realEstate;
	}

	public void setRealEstate(RealEstate realEstate) {
		this.realEstate = realEstate;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
