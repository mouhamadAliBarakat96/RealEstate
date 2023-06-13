package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class ShopRentFacade extends AbstractFacade<ShopRent> implements Serializable {

	/**
	 * 
	 */
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	private static final long serialVersionUID = 1L;

	public ShopRentFacade() {
		super(ShopRent.class);
	}
	
	public ShopRent mangmentSavePost(ShopRent obj , List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImage(inputParts);
			obj.setImages(imagesUrl);
		return this.save(obj);
	}
}
