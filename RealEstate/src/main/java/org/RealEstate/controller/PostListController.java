package org.RealEstate.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.LazyDataModel.LazyPostCustomerModel;
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
	
	@PostConstruct
	public void init() {
		lazyModel = new LazyPostCustomerModel(realEstateFacade);
	}

	public LazyDataModel<RealEstate> getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<RealEstate> lazyModel) {
		this.lazyModel = lazyModel;
	}

}
