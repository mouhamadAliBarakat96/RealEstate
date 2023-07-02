package org.RealEstate.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import org.RealEstate.enumerator.Configuration;
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
	@EJB
	private ConfigurationFacade configurationFacade;

	@Resource
	private TimerService timerService;
	@EJB
	private RealEstateFacade realEstateFacade;

	@PostConstruct
	public void init() {
		prepareData();

		// test

		try {

			String timerToCheck = "11-11-2001 23:18";
			Calendar timer = Calendar.getInstance();
			timer.setTime(DateUtils.parseDate(timerToCheck, "dd-MM-yyyy HH:mm"));
			timerService
					.createCalendarTimer(new ScheduleExpression().hour(String.valueOf(timer.get(Calendar.HOUR_OF_DAY)))
							.minute(String.valueOf(timer.get(Calendar.MINUTE))), new TimerConfig("TEST", false));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Timeout
	public void timeOutByConfig(Timer timer) {
		switch (timer.getInfo().toString()) {
		case "TEST":
			realEstateFacade.updatePostToExpiryDate(postExpiryDate);
			break;
		}

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
