package org.RealEstate.facade;



import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class OfficeRentFacade extends AbstractFacade<OfficeRent> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public OfficeRentFacade() {
		super(OfficeRent.class);
	}
	
	public OfficeRent mangmentSavePost(OfficeRent obj , List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
			obj.setImages(imagesUrl);
		return this.save(obj);
	
	}
}

