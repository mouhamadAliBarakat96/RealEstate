package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.enumerator.Configuration;
import org.RealEstate.facade.ConfigurationFacade;
import org.RealEstate.interfaces.ICRUDOperations;
import org.RealEstate.model.GeneralConfiguration;
import org.RealEstate.model.Governorate;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class ConfigurationController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ConfigurationFacade configurationFacade;

	private int freeNbOfPost;
	private int postExpiryDate;

	private GeneralConfiguration generalConfigurationNbFreeNbOFPost;
	private GeneralConfiguration generalConfigurationPostExpiryDate;

	private List<GeneralConfiguration> listGeneralConfiguration = new ArrayList<>();

	@PostConstruct
	public void init() {
		prepareData();
	}

	private void prepareData() {
		listGeneralConfiguration = configurationFacade.findAll();

		// find number of post
		Optional<GeneralConfiguration> configurationNbOfPost = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.FREE_NB_OF_POST.toString())).findFirst();
		if (configurationNbOfPost.isPresent()) {
			generalConfigurationNbFreeNbOFPost = configurationNbOfPost.get();
			freeNbOfPost = Integer.valueOf(configurationNbOfPost.get().getValue());
		}

		// find post expiryDate
		Optional<GeneralConfiguration> configurationPostExpiryDate = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.POST_EXPIRY_DATE.toString())).findFirst();
		if (configurationPostExpiryDate.isPresent()) {
			generalConfigurationPostExpiryDate = configurationPostExpiryDate.get();
			postExpiryDate = Integer.valueOf(configurationPostExpiryDate.get().getValue());
		}

	}

	public void save() {

		try {
			// save number of post
			if (generalConfigurationNbFreeNbOFPost == null) {
				generalConfigurationNbFreeNbOFPost = new GeneralConfiguration();
				generalConfigurationNbFreeNbOFPost.setKey(Configuration.FREE_NB_OF_POST.toString());
				generalConfigurationNbFreeNbOFPost.setValue(String.valueOf(freeNbOfPost));
			} else {
				generalConfigurationNbFreeNbOFPost.setValue(String.valueOf(freeNbOfPost));

			}
			configurationFacade.save(generalConfigurationNbFreeNbOFPost);

			// post expiryDate
			if (generalConfigurationPostExpiryDate == null) {
				generalConfigurationPostExpiryDate = new GeneralConfiguration();
				generalConfigurationPostExpiryDate.setKey(Configuration.POST_EXPIRY_DATE.toString());
				generalConfigurationPostExpiryDate.setValue(String.valueOf(postExpiryDate));
			} else {
				generalConfigurationPostExpiryDate.setValue(String.valueOf(postExpiryDate));

			}

			CommonUtility.addMessageToFacesContext(" save_success ", "success");
			configurationFacade.save(generalConfigurationPostExpiryDate);

			prepareData();
		}

		catch (Exception e) {
			CommonUtility.addMessageToFacesContext(e.getCause().getMessage(), "error");
			e.printStackTrace();
		}

	}

	public int getFreeNbOfPost() {
		return freeNbOfPost;
	}

	public void setFreeNbOfPost(int freeNbOfPost) {
		this.freeNbOfPost = freeNbOfPost;
	}

	public int getPostExpiryDate() {
		return postExpiryDate;
	}

	public void setPostExpiryDate(int postExpiryDate) {
		this.postExpiryDate = postExpiryDate;
	}

}
