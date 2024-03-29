package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.dto.ImageDto;
import org.RealEstate.facade.AdsFacade;
import org.RealEstate.model.Ads;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.service.UploadImagesMultiPart;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;

@ViewScoped
@Named
public class AdsController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private AdsFacade adsFacade;
	private List<Ads> pageItems = new ArrayList<>();
	private List<Ads> filteredList;

	private Ads adsToSave = new Ads();

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	private ImageDto imageDto;

	private String fullUrl = "";

	private String ipAddressWithPort;

	@javax.inject.Inject
	private AppSinglton appSinglton;

	@PostConstruct
	public void init() {
		pageItems = adsFacade.findAll();

	}

	public void selectForImageUrl(Ads ads) {
		fullUrl = "";
		fullUrl = fullUrl.concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES).concat("/")
				.concat(Constants.ADS_IMAGE_DIR_NAME).concat("/").concat(ads.getUrl() == null ? "" : ads.getUrl());

		Ajax.oncomplete("PF('dlg3').show()");
		Ajax.update("image");
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

	public void handleFileUploadRequestInformation(FileUploadEvent event) {

		imageDto = new ImageDto(event.getFile().getFileName(), event.getFile().getContent());

		FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void save() {
		try {

			if (StringUtils.isBlank(adsToSave.getUrlToOpen())) {
				CommonUtility.addMessageToFacesContext("Dont miss To Add Url To Open  ", "error");
return ;
			}

			if (imageDto == null) {
				CommonUtility.addMessageToFacesContext("upload the image", "error");
				return ;
			}

			String url = uploadImagesMultiPart.uploadImageAdsFrontEnd(imageDto);

			adsToSave.setUrl(url);

			adsFacade.save(adsToSave);
			adsFacade.getEm().detach(adsToSave);
			adsToSave = new Ads();
			imageDto = new ImageDto();
			// pageItems = adsFacade.findAll();
			// Ajax.oncomplete("PF('tableWidget').filter()");
			// CommonUtility.addMessageToFacesContext(" save_success ", "success");

			changeUrl();

		} catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getMessage(), "error");

			e.printStackTrace();
		}

	}

	private void changeUrl() {

		try {
			FacesContext context = FacesContext.getCurrentInstance();

			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String url = request.getRequestURL().toString();
			url = Utils.replaceHost(url, appSinglton.getRealDns(), appSinglton.getMode());
			Faces.redirect(url);

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void remove(Ads item) {
		try {
			adsFacade.remove(item);
			pageItems = adsFacade.findAll();
			CommonUtility.addMessageToFacesContext("Delete Success ", "success");

		} catch (Exception e) {
			e.printStackTrace();
			CommonUtility.addMessageToFacesContext("Error on delete caused by childs ", "error");

		}
	}

	public List<Ads> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Ads> pageItems) {
		this.pageItems = pageItems;
	}

	public List<Ads> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<Ads> filteredList) {
		this.filteredList = filteredList;
	}

	public Ads getAdsToSave() {
		return adsToSave;
	}

	public void setAdsToSave(Ads adsToSave) {
		this.adsToSave = adsToSave;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
