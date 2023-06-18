package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.LazyDataModel.LazyPostCustomerModel;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.RealEstate;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named
public class PostListController implements Serializable {
	private static final long serialVersionUID = 1L;

	private LazyDataModel<RealEstate> lazyModel;

	@Inject
	private RealEstateFacade realEstateFacade;

	// post status
	private Long nmOfPostAccepted;
	private Long nbOfPostPending;
	private Long nbOfPostRevieuxByUser;
	private Long nbOfPostReffused;
	private Long nbOfPostExpired;

	// post type
	private Long numOfPostAppartmentRent;
	private Long numOfPostAppartmentSell;
	private Long numOfPostLand;
	private Long numOfPostShopRent;
	private Long numOfPostShopSell;
	private Long numOfPostOfficeRent;
	private Long numOfPostOfficeSell;

	@PostConstruct
	public void init() {
		lazyModel = new LazyPostCustomerModel(realEstateFacade);
		fillData();
	}

	private void fillData() {
		// post status
		nmOfPostAccepted = realEstateFacade.findUserCountPostByStatus(PostStatus.ACCEPTED);
		nbOfPostPending = realEstateFacade.findUserCountPostByStatus(PostStatus.PENDING);
		nbOfPostRevieuxByUser = realEstateFacade.findUserCountPostByStatus(PostStatus.TO_REVIEUX_BY_USER);
		nbOfPostReffused = realEstateFacade.findUserCountPostByStatus(PostStatus.REFFUSED);
		nbOfPostExpired = realEstateFacade.findUserCountPostByStatus(PostStatus.EXPIRED);
		// post type
		numOfPostAppartmentRent = realEstateFacade.findUserCountPostByType(PostType.APPRATMENT_RENT);

		numOfPostAppartmentSell = realEstateFacade.findUserCountPostByType(PostType.APPRATMENT_SELL);
		numOfPostLand = realEstateFacade.findUserCountPostByType(PostType.LAND);
		numOfPostShopRent = realEstateFacade.findUserCountPostByType(PostType.SHOP_RENT);
		numOfPostShopSell = realEstateFacade.findUserCountPostByType(PostType.SHOP_SELL);
		numOfPostOfficeRent = realEstateFacade.findUserCountPostByType(PostType.OFFICE_RENT);
		numOfPostOfficeSell = realEstateFacade.findUserCountPostByType(PostType.OFFICE_SELL);

	}

	public LazyDataModel<RealEstate> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<RealEstate> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public RealEstateFacade getRealEstateFacade() {
		return realEstateFacade;
	}

	public void setRealEstateFacade(RealEstateFacade realEstateFacade) {
		this.realEstateFacade = realEstateFacade;
	}

	public Long getNmOfPostAccepted() {
		return nmOfPostAccepted;
	}

	public void setNmOfPostAccepted(Long nmOfPostAccepted) {
		this.nmOfPostAccepted = nmOfPostAccepted;
	}

	public Long getNbOfPostPending() {
		return nbOfPostPending;
	}

	public void setNbOfPostPending(Long nbOfPostPending) {
		this.nbOfPostPending = nbOfPostPending;
	}

	public Long getNbOfPostRevieuxByUser() {
		return nbOfPostRevieuxByUser;
	}

	public void setNbOfPostRevieuxByUser(Long nbOfPostRevieuxByUser) {
		this.nbOfPostRevieuxByUser = nbOfPostRevieuxByUser;
	}

	public Long getNbOfPostReffused() {
		return nbOfPostReffused;
	}

	public void setNbOfPostReffused(Long nbOfPostReffused) {
		this.nbOfPostReffused = nbOfPostReffused;
	}

	public Long getNbOfPostExpired() {
		return nbOfPostExpired;
	}

	public void setNbOfPostExpired(Long nbOfPostExpired) {
		this.nbOfPostExpired = nbOfPostExpired;
	}

	public Long getNumOfPostAppartmentRent() {
		return numOfPostAppartmentRent;
	}

	public void setNumOfPostAppartmentRent(Long numOfPostAppartmentRent) {
		this.numOfPostAppartmentRent = numOfPostAppartmentRent;
	}

	public Long getNumOfPostAppartmentSell() {
		return numOfPostAppartmentSell;
	}

	public void setNumOfPostAppartmentSell(Long numOfPostAppartmentSell) {
		this.numOfPostAppartmentSell = numOfPostAppartmentSell;
	}

	public Long getNumOfPostLand() {
		return numOfPostLand;
	}

	public void setNumOfPostLand(Long numOfPostLand) {
		this.numOfPostLand = numOfPostLand;
	}

	public Long getNumOfPostShopRent() {
		return numOfPostShopRent;
	}

	public void setNumOfPostShopRent(Long numOfPostShopRent) {
		this.numOfPostShopRent = numOfPostShopRent;
	}

	public Long getNumOfPostShopSell() {
		return numOfPostShopSell;
	}

	public void setNumOfPostShopSell(Long numOfPostShopSell) {
		this.numOfPostShopSell = numOfPostShopSell;
	}

	public Long getNumOfPostOfficeRent() {
		return numOfPostOfficeRent;
	}

	public void setNumOfPostOfficeRent(Long numOfPostOfficeRent) {
		this.numOfPostOfficeRent = numOfPostOfficeRent;
	}

	public Long getNumOfPostOfficeSell() {
		return numOfPostOfficeSell;
	}

	public void setNumOfPostOfficeSell(Long numOfPostOfficeSell) {
		this.numOfPostOfficeSell = numOfPostOfficeSell;
	}

}
