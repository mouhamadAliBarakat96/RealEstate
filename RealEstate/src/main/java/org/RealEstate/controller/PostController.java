package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
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

	@PostConstruct
	public void init() {

		if (id < 0) {
			CommonUtility.addMessageToFacesContext("Id should > 0  ", "error");
		} else {
			realEstate = realEstateFacade.find(id);

			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");
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

	public void save() {

		try {
			if ((realEstate.getPostStatus().equals(PostStatus.REFFUSED.toString())
					|| realEstate.getPostStatus().equals(PostStatus.TO_REVIEUX_BY_USER.toString()))
					&& realEstate.getReffuseCause().isEmpty()) {

				CommonUtility.addMessageToFacesContext("refuse cause  should not be empty  ", "error");

			} else {
				realEstateFacade.save(realEstate);

				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");

			changeUrl();
			}
		} catch (Exception e) {
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
