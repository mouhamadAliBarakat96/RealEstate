package org.RealEstate.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.UserCategory;
import org.RealEstate.enumerator.WaterResources;
import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.service.AppSinglton;
import org.RealEstate.utils.CommonUtility;
import org.RealEstate.utils.Constants;
import org.RealEstate.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class PostController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	@Param(name = "id")
	private long id;

	private RealEstate realEstate;
	private String ipAddressWithPort;
	@EJB
	private RealEstateFacade realEstateFacade;
	@EJB
	private ChaletFacade chaletFacade;

	private String fullUrl = "";

	private final String REQUEST_PARAM = "id";

	private Long nbOfActivePostByThisUser;

	@EJB
	private AppSinglton appSinglton;

	private User user;

	private PostStatus postStatus;

	@PostConstruct
	public void init() {

		if (id <= 0) {
			CommonUtility.addMessageToFacesContext("Id should > 0  ", "error");
		} else {
			realEstate = realEstateFacade.find(id);
			user = realEstate.getUser();
			nbOfActivePostByThisUser = realEstateFacade.findUserCountPostPendingOrActive(user.getId())
					+ chaletFacade.findUserCountPostPendingOrActive(user.getId());
			if (realEstate.getPostStatus() == PostStatus.ACCEPTED) {
				nbOfActivePostByThisUser -= 1;
			}

			fullUrl = fullUrl.concat("http://").concat(getIpAddressWithPort()).concat("/").concat(Constants.IMAGES)
					.concat("/").concat(Constants.POST_IMAGE_DIR_NAME).concat("/");

			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			postStatus = realEstate.getPostStatus();
			if (flash.containsKey("update-card")) {

				flash.setKeepMessages(true);
				CommonUtility.addMessageToFacesContext("Update successfully   ", "success");
			}

		}

	}

	public String waterResouces(List<WaterResources> list) {
		list.size();
		String result = "";
		for (WaterResources var : list) {
			result = result.concat(var.toString().toLowerCase()) + ",";
		}
		if (!result.isEmpty()) {
			result = StringUtils.substring(result, 0, result.length() - 1);
		}
		return result;
	}

	public String getIpAddressWithPort() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ipAddress = request.getRemoteAddr();
		int port = request.getLocalPort();
		ipAddressWithPort = ipAddress + ":" + port;

		return ipAddressWithPort;
	}

	public void mangmentPoostStatus() throws Exception {

		if ((realEstate.getPostStatus().equals(PostStatus.REFFUSED)
				|| realEstate.getPostStatus().equals(PostStatus.TO_REVIEUX_BY_USER))
				&& realEstate.getReffuseCause().isEmpty()) {

			CommonUtility.addMessageToFacesContext("refuse cause  should not be empty  ", "error");
			throw new Exception("refuse cause  should not be empty");
		} else if (realEstate.getPostStatus().equals(PostStatus.ACCEPTED)) {

			int nbOfPostAllowed = 0;
			if (user.isBroker()) {
				nbOfPostAllowed = appSinglton.getBrokerNbOfPost();
			}

			if (user.getUserCategory() == UserCategory.REGULAR) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getFreeNbOfPost();

			} else if (user.getUserCategory() == UserCategory.MEDUIM) {

				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getMeduimAccountNbOfPost();

			} else if (user.getUserCategory() == UserCategory.PREMIUM) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getPremuimAccountNbOfPost();
			}

			if (nbOfActivePostByThisUser >= nbOfPostAllowed) {
				// realEstateFacade.getEm().detach(realEstate);
				CommonUtility.addMessageToFacesContext(Constants.EXCEEDED_POST_LIMIT_FOR_THIS_USER, "error");
				throw new Exception(Constants.EXCEEDED_POST_LIMIT_FOR_THIS_USER);

			}

			// hon if mshe halo
			if (postStatus != PostStatus.ACCEPTED) {
				realEstate.setPostDate(new Date());

			}

		}

	}

	private void mangmentBoost() {
		if (realEstate.getBoostEnum() != null) {
			switch (realEstate.getBoostEnum()) {
			case BOOST_FOR_3_DAYS:
				realEstate.setBoostedUntil(Utils.addDaysToCurrentDate(3));
				realEstate.setBoosted(true);
				break;
			case BOOST_FOR_1_WEEK:
				realEstate.setBoostedUntil(Utils.addDaysToCurrentDate(7));
				realEstate.setBoosted(true);
				break;
			case BOOST_FOR_2_WEEKS:
				realEstate.setBoostedUntil(Utils.addDaysToCurrentDate(14));
				realEstate.setBoosted(true);
				break;
			case BOOST_FOR_1_MONTH:
				realEstate.setBoostedUntil(Utils.addDaysToCurrentDate(30));
				realEstate.setBoosted(true);
				break;

			}
		}
	}

	public void save() throws Exception {
		try {
			mangmentPoostStatus();
			mangmentBoost();

			realEstateFacade.save(realEstate);

			CommonUtility.addMessageToFacesContext("Update successfully   ", "success");

			Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
			flash.put("update-card", "true");

			changeUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void changeUrl() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		try {

			Faces.redirect(url + "?" + REQUEST_PARAM + "=%s", id + "");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RealEstate getRealEstate() {
		return realEstate;
	}

	public void setRealEstate(RealEstate realEstate) {
		this.realEstate = realEstate;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

}
