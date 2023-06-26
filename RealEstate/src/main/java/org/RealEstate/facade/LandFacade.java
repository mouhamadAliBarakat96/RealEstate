package org.RealEstate.facade;


import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Land;
import org.RealEstate.model.ShopRent;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class LandFacade extends AbstractFacade<Land> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	public LandFacade() {
		super(Land.class);
	}
	
	
	

	public Land mangmentSavePost(Land obj , List<InputPart> inputParts ) throws Exception {
		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
		obj.setImages(imagesUrl);
		return this.save(obj);
	}
}
