package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.service.UploadImagesMultiPart;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Stateless

public class OfficeSellFacade extends AbstractFacade<OfficeSell> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private UploadImagesMultiPart uploadImagesMultiPart;

	public OfficeSellFacade() {
		super(OfficeSell.class);
	}

	public OfficeSell mangmentSavePost(OfficeSell obj , List<InputPart> inputParts) throws Exception {

		List<String> imagesUrl = uploadImagesMultiPart.uploadImagePost(inputParts);
			obj.setImages(imagesUrl);
		return this.save(obj);
	}

}
