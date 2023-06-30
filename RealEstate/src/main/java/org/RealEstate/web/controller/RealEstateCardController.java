package org.RealEstate.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
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
	private RealEstate item;
	private List<ResponsiveOption> responsiveOptions1;
	private int activeIndex = 0;

	@Inject
	private RealEstateFacade facade;

	private String fullUrl = "";
	private String ipAddressWithPort;

	@PostConstruct
	public void init() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

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

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		activeIndex = Integer.valueOf(params.get("index"));
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
}
