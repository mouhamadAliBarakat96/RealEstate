package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.RealEstate.utils.Constants;
import org.omnifaces.cdi.Param;

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

	@PostConstruct
	public void init() {
		realEstate = realEstateFacade.find(id);

		fullUrl =fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
				.concat(Constants.POST_IMAGE_DIR_NAME).concat("/");
		System.out.println(fullUrl);
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
