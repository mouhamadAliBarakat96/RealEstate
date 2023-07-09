package org.RealEstate.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.RealEstate.enumerator.Configuration;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.ConfigurationFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.GeneralConfiguration;
import org.apache.commons.lang3.time.DateUtils;

@Singleton
@Lock(LockType.READ)
@Startup
public class AppSinglton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@EJB
	private ConfigurationFacade configurationFacade;

	@Resource
	private TimerService timerService;
	@EJB
	private RealEstateFacade realEstateFacade;
	@EJB
	private ChaletFacade chaletFacade;

	@PostConstruct
	public void init() {
		prepareData();

		try {

			String timerPostExpireDate = "11-11-2001 23:00";
			Calendar postExpire = Calendar.getInstance();
			postExpire.setTime(DateUtils.parseDate(timerPostExpireDate, "dd-MM-yyyy HH:mm"));

			timerService
					.createCalendarTimer(
							new ScheduleExpression().hour(String.valueOf(postExpire.get(Calendar.HOUR_OF_DAY)))
									.minute(String.valueOf(postExpire.get(Calendar.MINUTE))),
							new TimerConfig("POST_EXPIRE", false));

			String timerPostBoost = "11-11-2001 00:27";
			Calendar postBoost = Calendar.getInstance();
			postBoost.setTime(DateUtils.parseDate(timerPostBoost, "dd-MM-yyyy HH:mm"));

			timerService
					.createCalendarTimer(
							new ScheduleExpression().hour(String.valueOf(postBoost.get(Calendar.HOUR_OF_DAY)))
									.minute(String.valueOf(postBoost.get(Calendar.MINUTE))),
							new TimerConfig("POST_BOOST", false));

			
			chaletFacade.updatePostBoost();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Timeout
	public void timeOutByConfig(Timer timer) {
		switch (timer.getInfo().toString()) {
		case "POST_EXPIRE":
			realEstateFacade.updatePostToExpiryDate(postExpiryDate);

			chaletFacade.updatePostToExpiryDate(postExpiryDate);

			break;
		case "POST_BOOST":
			realEstateFacade.updatePostBoost();

			chaletFacade.updatePostBoost();

			break;

			
			
			
		}

	}

	private void prepareData() {
		List<GeneralConfiguration> listGeneralConfiguration = configurationFacade.findAll();
		GeneralConfiguration generalConfigurationNbFreeNbOFPost;
		GeneralConfiguration generalConfigurationPostExpiryDate;

		GeneralConfiguration generalConfigurationMeduimAccountPay;
		GeneralConfiguration generalConfigurationMeduimAccountNbOfPost;
		GeneralConfiguration generalConfigurationPremuimAccountPay;
		GeneralConfiguration generalConfigurationPremuimAccountNbOfPost;
		GeneralConfiguration generalConfigurationPhoneNumber;
		GeneralConfiguration generalConfigurationBrokerNbOfPost;
		GeneralConfiguration generalConfigurationBoostDuration;
		GeneralConfiguration generalConfigurationBoostCash;
		GeneralConfiguration generalConfigurationPostBadgePay;
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

}
