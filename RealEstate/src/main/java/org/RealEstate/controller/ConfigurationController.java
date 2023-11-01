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
import org.RealEstate.model.GeneralConfiguration;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;

@ViewScoped
@Named
public class ConfigurationController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ConfigurationFacade configurationFacade;

	private int freeNbOfPost;
	private int postExpiryDate;

	private int meduimAccountPay;
	private int meduimAccountNbOfPost;
	private int premuimAccountPay;
	private int premuimAccountNbOfPost;
	private String phoneNumber;
	private int brokerNbOfPost;
	private int boostDuration;
	private int boostCash;
	private int postBadgePay;

	private GeneralConfiguration generalConfigurationNbFreeNbOFPost;
	private GeneralConfiguration generalConfigurationPostExpiryDate;

	private GeneralConfiguration generalConfigurationMeduimAccountPay;
	private GeneralConfiguration generalConfigurationMeduimAccountNbOfPost;
	private GeneralConfiguration generalConfigurationPremuimAccountPay;
	private GeneralConfiguration generalConfigurationPremuimAccountNbOfPost;
	private GeneralConfiguration generalConfigurationPhoneNumber;
	private GeneralConfiguration generalConfigurationBrokerNbOfPost;
	private GeneralConfiguration generalConfigurationBoostDuration;
	private GeneralConfiguration generalConfigurationBoostCash;
	private GeneralConfiguration generalConfigurationPostBadgePay;

	private List<GeneralConfiguration> listGeneralConfiguration = new ArrayList<>();

	@EJB
	private AppSinglton appSinglton;

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

		/**
		 * 
		 * private int brokerNbOfPost; private int boostDuration; private int boostCash;
		 * private int postBadgePay;
		 */

		// meduimAccountPay
		Optional<GeneralConfiguration> configurationMeduimAccountPay = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.MEDUIM_ACCOUNT_PAY.toString())).findFirst();
		if (configurationMeduimAccountPay.isPresent()) {
			generalConfigurationMeduimAccountPay = configurationMeduimAccountPay.get();
			meduimAccountPay = Integer.valueOf(configurationMeduimAccountPay.get().getValue());
		}

		Optional<GeneralConfiguration> configurationMeduimAccountNbOfPost = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.MEDUIM_ACCOUNT_NB_OF_POST.toString())).findFirst();
		if (configurationMeduimAccountNbOfPost.isPresent()) {
			generalConfigurationMeduimAccountNbOfPost = configurationMeduimAccountNbOfPost.get();
			meduimAccountNbOfPost = Integer.valueOf(configurationMeduimAccountNbOfPost.get().getValue());
		}

		Optional<GeneralConfiguration> configurationPremuimAccountPay = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.PREMIUM_ACCOUNT_PAY.toString())).findFirst();
		if (configurationPremuimAccountPay.isPresent()) {
			generalConfigurationPremuimAccountPay = configurationPremuimAccountPay.get();
			premuimAccountPay = Integer.valueOf(configurationPremuimAccountPay.get().getValue());
		}

		Optional<GeneralConfiguration> configurationPremuimAccountNbOfPost = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.PREMIUM_ACCOUNT_NB_OF_POST.toString())).findFirst();
		if (configurationPremuimAccountNbOfPost.isPresent()) {
			generalConfigurationPremuimAccountNbOfPost = configurationPremuimAccountNbOfPost.get();
			premuimAccountNbOfPost = Integer.valueOf(configurationPremuimAccountNbOfPost.get().getValue());
		}

		Optional<GeneralConfiguration> configurationPhoneNumber = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.PHONE_NUMBER.toString())).findFirst();
		if (configurationPhoneNumber.isPresent()) {
			generalConfigurationPhoneNumber = configurationPhoneNumber.get();
			phoneNumber = (configurationPhoneNumber.get().getValue());
		}

		Optional<GeneralConfiguration> configurationBrokerNbOfPost = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.BROKER_NB_OF_POST.toString())).findFirst();
		if (configurationBrokerNbOfPost.isPresent()) {
			generalConfigurationBrokerNbOfPost = configurationBrokerNbOfPost.get();
			brokerNbOfPost = Integer.valueOf(configurationBrokerNbOfPost.get().getValue());
		}

		Optional<GeneralConfiguration> configurationBoostDuration = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.BOOST_DURATION.toString())).findFirst();
		if (configurationBoostDuration.isPresent()) {
			generalConfigurationBoostDuration = configurationBoostDuration.get();
			boostDuration = Integer.valueOf(configurationBoostDuration.get().getValue());
		}

		Optional<GeneralConfiguration> configurationBostCash = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.BOST_CASH.toString())).findFirst();
		if (configurationBostCash.isPresent()) {
			generalConfigurationBoostCash = configurationBostCash.get();
			boostCash = Integer.valueOf(configurationBostCash.get().getValue());
		}

		Optional<GeneralConfiguration> configurationBadgePay = listGeneralConfiguration.stream()
				.filter(x -> x.getKey().equals(Configuration.POST_BADGE_PAY.toString())).findFirst();
		if (configurationBadgePay.isPresent()) {
			generalConfigurationPostBadgePay = configurationBadgePay.get();
			postBadgePay = Integer.valueOf(configurationBadgePay.get().getValue());
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
			appSinglton.setFreeNbOfPost(freeNbOfPost);

			configurationFacade.save(generalConfigurationNbFreeNbOFPost);

			// post expiryDate
			if (generalConfigurationPostExpiryDate == null) {
				generalConfigurationPostExpiryDate = new GeneralConfiguration();
				generalConfigurationPostExpiryDate.setKey(Configuration.POST_EXPIRY_DATE.toString());
				generalConfigurationPostExpiryDate.setValue(String.valueOf(postExpiryDate));
			} else {
				generalConfigurationPostExpiryDate.setValue(String.valueOf(postExpiryDate));

			}
			appSinglton.setPostExpiryDate(postExpiryDate);
			configurationFacade.save(generalConfigurationPostExpiryDate);

			if (generalConfigurationMeduimAccountPay == null) {
				generalConfigurationMeduimAccountPay = new GeneralConfiguration();
				generalConfigurationMeduimAccountPay.setKey(Configuration.MEDUIM_ACCOUNT_PAY.toString());
				generalConfigurationMeduimAccountPay.setValue(String.valueOf(meduimAccountPay));
			} else {
				generalConfigurationMeduimAccountPay.setValue(String.valueOf(meduimAccountPay));

			}
			appSinglton.setMeduimAccountPay(meduimAccountPay);
			configurationFacade.save(generalConfigurationMeduimAccountPay);

			if (generalConfigurationMeduimAccountNbOfPost == null) {
				generalConfigurationMeduimAccountNbOfPost = new GeneralConfiguration();
				generalConfigurationMeduimAccountNbOfPost.setKey(Configuration.MEDUIM_ACCOUNT_NB_OF_POST.toString());
				generalConfigurationMeduimAccountNbOfPost.setValue(String.valueOf(meduimAccountNbOfPost));
			} else {
				generalConfigurationMeduimAccountNbOfPost.setValue(String.valueOf(meduimAccountNbOfPost));

			}
			appSinglton.setMeduimAccountNbOfPost(meduimAccountNbOfPost);
			configurationFacade.save(generalConfigurationMeduimAccountNbOfPost);

			if (generalConfigurationPremuimAccountPay == null) {
				generalConfigurationPremuimAccountPay = new GeneralConfiguration();
				generalConfigurationPremuimAccountPay.setKey(Configuration.PREMIUM_ACCOUNT_PAY.toString());
				generalConfigurationPremuimAccountPay.setValue(String.valueOf(premuimAccountPay));
			} else {
				generalConfigurationPremuimAccountPay.setValue(String.valueOf(premuimAccountPay));

			}
			appSinglton.setPremuimAccountPay(premuimAccountPay);
			configurationFacade.save(generalConfigurationPremuimAccountPay);

			if (generalConfigurationPremuimAccountNbOfPost == null) {
				generalConfigurationPremuimAccountNbOfPost = new GeneralConfiguration();
				generalConfigurationPremuimAccountNbOfPost.setKey(Configuration.PREMIUM_ACCOUNT_NB_OF_POST.toString());
				generalConfigurationPremuimAccountNbOfPost.setValue(String.valueOf(premuimAccountNbOfPost));
			} else {
				generalConfigurationPremuimAccountPay.setValue(String.valueOf(premuimAccountNbOfPost));

			}
			appSinglton.setPremuimAccountNbOfPost(premuimAccountNbOfPost);
			configurationFacade.save(generalConfigurationPremuimAccountNbOfPost);

			if (generalConfigurationPhoneNumber == null) {
				generalConfigurationPhoneNumber = new GeneralConfiguration();
				generalConfigurationPhoneNumber.setKey(Configuration.PHONE_NUMBER.toString());
				generalConfigurationPhoneNumber.setValue(String.valueOf(phoneNumber));
			} else {
				generalConfigurationPhoneNumber.setValue(String.valueOf(phoneNumber));

			}
			appSinglton.setPhoneNumber(phoneNumber);
			configurationFacade.save(generalConfigurationPhoneNumber);

			if (generalConfigurationBrokerNbOfPost == null) {
				generalConfigurationBrokerNbOfPost = new GeneralConfiguration();
				generalConfigurationBrokerNbOfPost.setKey(Configuration.BROKER_NB_OF_POST.toString());
				generalConfigurationBrokerNbOfPost.setValue(String.valueOf(brokerNbOfPost));
			} else {
				generalConfigurationBrokerNbOfPost.setValue(String.valueOf(brokerNbOfPost));

			}
			appSinglton.setBrokerNbOfPost(brokerNbOfPost);
			configurationFacade.save(generalConfigurationBrokerNbOfPost);

			if (generalConfigurationBoostDuration == null) {
				generalConfigurationBoostDuration = new GeneralConfiguration();
				generalConfigurationBoostDuration.setKey(Configuration.BOOST_DURATION.toString());
				generalConfigurationBoostDuration.setValue(String.valueOf(boostDuration));
			} else {
				generalConfigurationBoostDuration.setValue(String.valueOf(boostDuration));

			}
			appSinglton.setBoostDuration(boostDuration);
			configurationFacade.save(generalConfigurationBrokerNbOfPost);

			if (generalConfigurationBoostCash == null) {
				generalConfigurationBoostCash = new GeneralConfiguration();
				generalConfigurationBoostCash.setKey(Configuration.BOST_CASH.toString());
				generalConfigurationBoostCash.setValue(String.valueOf(boostDuration));
			} else {
				generalConfigurationBoostCash.setValue(String.valueOf(boostCash));

			}
			appSinglton.setBoostCash(boostCash);
			configurationFacade.save(generalConfigurationBoostCash);

			if (generalConfigurationPostBadgePay == null) {
				generalConfigurationPostBadgePay = new GeneralConfiguration();
				generalConfigurationPostBadgePay.setKey(Configuration.POST_BADGE_PAY.toString());
				generalConfigurationPostBadgePay.setValue(String.valueOf(boostDuration));
			} else {
				generalConfigurationPostBadgePay.setValue(String.valueOf(postBadgePay));

			}
			appSinglton.setBoostCash(postBadgePay);
			configurationFacade.save(generalConfigurationPostBadgePay);

			CommonUtility.addMessageToFacesContext(" save_success ", "success");

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

	public ConfigurationFacade getConfigurationFacade() {
		return configurationFacade;
	}

	public void setConfigurationFacade(ConfigurationFacade configurationFacade) {
		this.configurationFacade = configurationFacade;
	}

	public int getMeduimAccountPay() {
		return meduimAccountPay;
	}

	public void setMeduimAccountPay(int meduimAccountPay) {
		this.meduimAccountPay = meduimAccountPay;
	}

	public int getMeduimAccountNbOfPost() {
		return meduimAccountNbOfPost;
	}

	public void setMeduimAccountNbOfPost(int meduimAccountNbOfPost) {
		this.meduimAccountNbOfPost = meduimAccountNbOfPost;
	}

	public int getPremuimAccountPay() {
		return premuimAccountPay;
	}

	public void setPremuimAccountPay(int premuimAccountPay) {
		this.premuimAccountPay = premuimAccountPay;
	}

	public int getPremuimAccountNbOfPost() {
		return premuimAccountNbOfPost;
	}

	public void setPremuimAccountNbOfPost(int premuimAccountNbOfPost) {
		this.premuimAccountNbOfPost = premuimAccountNbOfPost;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getBrokerNbOfPost() {
		return brokerNbOfPost;
	}

	public void setBrokerNbOfPost(int brokerNbOfPost) {
		this.brokerNbOfPost = brokerNbOfPost;
	}

	public int getBoostDuration() {
		return boostDuration;
	}

	public void setBoostDuration(int boostDuration) {
		this.boostDuration = boostDuration;
	}

	public int getBoostCash() {
		return boostCash;
	}

	public void setBoostCash(int boostCash) {
		this.boostCash = boostCash;
	}

	public int getPostBadgePay() {
		return postBadgePay;
	}

	public void setPostBadgePay(int postBadgePay) {
		this.postBadgePay = postBadgePay;
	}

	public GeneralConfiguration getGeneralConfigurationNbFreeNbOFPost() {
		return generalConfigurationNbFreeNbOFPost;
	}

	public void setGeneralConfigurationNbFreeNbOFPost(GeneralConfiguration generalConfigurationNbFreeNbOFPost) {
		this.generalConfigurationNbFreeNbOFPost = generalConfigurationNbFreeNbOFPost;
	}

	public GeneralConfiguration getGeneralConfigurationPostExpiryDate() {
		return generalConfigurationPostExpiryDate;
	}

	public void setGeneralConfigurationPostExpiryDate(GeneralConfiguration generalConfigurationPostExpiryDate) {
		this.generalConfigurationPostExpiryDate = generalConfigurationPostExpiryDate;
	}

	public GeneralConfiguration getGeneralConfigurationMeduimAccountPay() {
		return generalConfigurationMeduimAccountPay;
	}

	public void setGeneralConfigurationMeduimAccountPay(GeneralConfiguration generalConfigurationMeduimAccountPay) {
		this.generalConfigurationMeduimAccountPay = generalConfigurationMeduimAccountPay;
	}

	public GeneralConfiguration getGeneralConfigurationMeduimAccountNbOfPost() {
		return generalConfigurationMeduimAccountNbOfPost;
	}

	public void setGeneralConfigurationMeduimAccountNbOfPost(
			GeneralConfiguration generalConfigurationMeduimAccountNbOfPost) {
		this.generalConfigurationMeduimAccountNbOfPost = generalConfigurationMeduimAccountNbOfPost;
	}

	public GeneralConfiguration getGeneralConfigurationPremuimAccountPay() {
		return generalConfigurationPremuimAccountPay;
	}

	public void setGeneralConfigurationPremuimAccountPay(GeneralConfiguration generalConfigurationPremuimAccountPay) {
		this.generalConfigurationPremuimAccountPay = generalConfigurationPremuimAccountPay;
	}

	public GeneralConfiguration getGeneralConfigurationPremuimAccountNbOfPost() {
		return generalConfigurationPremuimAccountNbOfPost;
	}

	public void setGeneralConfigurationPremuimAccountNbOfPost(
			GeneralConfiguration generalConfigurationPremuimAccountNbOfPost) {
		this.generalConfigurationPremuimAccountNbOfPost = generalConfigurationPremuimAccountNbOfPost;
	}

	public GeneralConfiguration getGeneralConfigurationPhoneNumber() {
		return generalConfigurationPhoneNumber;
	}

	public void setGeneralConfigurationPhoneNumber(GeneralConfiguration generalConfigurationPhoneNumber) {
		this.generalConfigurationPhoneNumber = generalConfigurationPhoneNumber;
	}

	public GeneralConfiguration getGeneralConfigurationBrokerNbOfPost() {
		return generalConfigurationBrokerNbOfPost;
	}

	public void setGeneralConfigurationBrokerNbOfPost(GeneralConfiguration generalConfigurationBrokerNbOfPost) {
		this.generalConfigurationBrokerNbOfPost = generalConfigurationBrokerNbOfPost;
	}

	public GeneralConfiguration getGeneralConfigurationBoostDuration() {
		return generalConfigurationBoostDuration;
	}

	public void setGeneralConfigurationBoostDuration(GeneralConfiguration generalConfigurationBoostDuration) {
		this.generalConfigurationBoostDuration = generalConfigurationBoostDuration;
	}

	public GeneralConfiguration getGeneralConfigurationBoostCash() {
		return generalConfigurationBoostCash;
	}

	public void setGeneralConfigurationBoostCash(GeneralConfiguration generalConfigurationBoostCash) {
		this.generalConfigurationBoostCash = generalConfigurationBoostCash;
	}

	public GeneralConfiguration getGeneralConfigurationPostBadgePay() {
		return generalConfigurationPostBadgePay;
	}

	public void setGeneralConfigurationPostBadgePay(GeneralConfiguration generalConfigurationPostBadgePay) {
		this.generalConfigurationPostBadgePay = generalConfigurationPostBadgePay;
	}

	public List<GeneralConfiguration> getListGeneralConfiguration() {
		return listGeneralConfiguration;
	}

	public void setListGeneralConfiguration(List<GeneralConfiguration> listGeneralConfiguration) {
		this.listGeneralConfiguration = listGeneralConfiguration;
	}

}
