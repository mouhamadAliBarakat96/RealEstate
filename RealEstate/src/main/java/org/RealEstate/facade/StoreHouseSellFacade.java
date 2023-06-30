package org.RealEstate.facade;



import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.model.StoreHouseSell;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class StoreHouseSellFacade extends AbstractFacade<StoreHouseSell> implements Serializable {

	/**
	 * 
	 */
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	private static final long serialVersionUID = 1L;

	public StoreHouseSellFacade() {
		super(StoreHouseSell.class);
	}
	
	public StoreHouseSell mangmentSavePost(StoreHouseSell obj , List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
			obj.setImages(imagesUrl);
		return this.save(obj);
	}
	
}
	
