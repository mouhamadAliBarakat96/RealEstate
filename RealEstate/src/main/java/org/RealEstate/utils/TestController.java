package org.RealEstate.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.dto.ImageDto;
import org.RealEstate.service.UploadImagesMultiPart;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFiles;

@ViewScoped
@Named
public class TestController implements Serializable {
	private static final long serialVersionUID = 1L;

	private String xx = "1234";

	private List<ImageDto> list = new ArrayList<>();

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	private UploadedFiles files;

	private String lat="";
	private String lang="";
	@PostConstruct
	public void init() {
		System.out.println("123");
	}

	public void handleFileUploadRequestInformation(FileUploadEvent event) {

		list.add(new ImageDto(event.getFile().getFileName(), event.getFile().getContent()));

		FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void uploadToServeer() {
		try {
			System.out.println(files);
			List<String> url = uploadImagesMultiPart.uploadImagePostFrontEnd(list);

			System.out.println(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// HERE FOR PROFILE PICTURE
	private ImageDto imageDtoProfilePicture = new ImageDto();

	public void handleFileUploadProfilePicture(FileUploadEvent event) {

		imageDtoProfilePicture = new ImageDto(event.getFile().getFileName(), event.getFile().getContent());

		FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void uploadToServerProfilePicture() {
		try {

			String url = uploadImagesMultiPart.uploadImageUserProfileJSF(imageDtoProfilePicture);

			System.out.println(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

	public String getXx() {
		return xx;
	}

	public void setXx(String xx) {
		this.xx = xx;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
