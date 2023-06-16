package org.RealEstate.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.RealEstate.enumerator.Configuration;
import org.RealEstate.facade.ConfigurationFacade;
import org.RealEstate.model.GeneralConfiguration;

@Singleton
@Lock(LockType.READ)
public class AppSinglton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int freeNbOfPost;
	private int postExpiryDate;
	@EJB
	private ConfigurationFacade configurationFacade;

	@PostConstruct
	public void init() {
		prepareData();
	}

	private void prepareData() {
		List<GeneralConfiguration> listGeneralConfiguration = configurationFacade.findAll();

		// find number of post
		Optional<GeneralConfiguration> configurationNbOfPost = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.FREE_NB_OF_POST.toString())).findFirst();
		if (configurationNbOfPost.isPresent()) {
			freeNbOfPost = Integer.valueOf(configurationNbOfPost.get().getValue());
		}

		// find post expiryDate
		Optional<GeneralConfiguration> configurationPostExpiryDate = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.POST_EXPIRY_DATE.toString())).findFirst();
		if (configurationPostExpiryDate.isPresent()) {
			postExpiryDate = Integer.valueOf(configurationPostExpiryDate.get().getValue());
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
