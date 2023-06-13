package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class AppratmentRentFacade extends AbstractFacade<AppratmentRent> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public AppratmentRentFacade() {
		super(AppratmentRent.class);
	}

	public AppratmentRent mangmentSavePost(AppratmentRent obj, List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImage(inputParts);
		obj.setImages(imagesUrl);
		return this.save(obj);

	}
}
