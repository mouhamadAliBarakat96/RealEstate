package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;



@Stateless

public class ShopSellFacade extends AbstractFacade<ShopSell> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public ShopSellFacade() {
		super(ShopSell.class);
	}
	
	public ShopSell mangmentSavePost(ShopSell obj , List<InputPart> inputParts) throws Exception {
		List<String> imagesUrl = uploadImagesMultiPart.uploadImage(inputParts);
		obj.setImages(imagesUrl);
		return this.save(obj);
	}
}
