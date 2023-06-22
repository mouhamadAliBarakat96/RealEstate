package org.RealEstate.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.facade.UserFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

@ViewScoped
@Named
public class PostUserListController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private UserFacade userFacade;

	private User user = new User();;
	private List<RealEstate> realEstateList = new ArrayList<>();
	private List<RealEstate> realEstateFilters;

	private List<Chalet> chaletList = new ArrayList<>();
	private List<Chalet> chaletFilters;

	@Inject
	@Param(name = "id")
	private long id;
	private final String REQUEST_PARAM = "id";

	@PostConstruct
	public void init() {
		try {
			if (id > 0) {
				user = userFacade.find(id);
				if (user != null) {
					user.getChales().size();
					user.getReadStateList().size();
					realEstateList = user.getReadStateList();
					chaletList = user.getChales();
				} else {
					Faces.redirect("exception");
				}
			} else {

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public List<RealEstate> getRealEstateList() {
		return realEstateList;
	}

	public void setRealEstateList(List<RealEstate> realEstateList) {
		this.realEstateList = realEstateList;
	}

	public List<RealEstate> getRealEstateFilters() {
		return realEstateFilters;
	}

	public void setRealEstateFilters(List<RealEstate> realEstateFilters) {
		this.realEstateFilters = realEstateFilters;
	}

	public List<Chalet> getChaletList() {
		return chaletList;
	}

	public void setChaletList(List<Chalet> chaletList) {
		this.chaletList = chaletList;
	}

	public List<Chalet> getChaletFilters() {
		return chaletFilters;
	}

	public void setChaletFilters(List<Chalet> chaletFilters) {
		this.chaletFilters = chaletFilters;
	}

}
