
package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.StoreHouseRent;
import org.RealEstate.model.StoreHouseSell;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class StoreHouseRentFacade extends AbstractFacade<StoreHouseRent> implements Serializable {

	/**
	 * 
	 */
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;
	private static final long serialVersionUID = 1L;

	public StoreHouseRentFacade() {
		super(StoreHouseRent.class);
	}

	public StoreHouseRent mangmentSavePost(StoreHouseRent obj, List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
		obj.setImages(imagesUrl);
		return this.save(obj);
	}

}
