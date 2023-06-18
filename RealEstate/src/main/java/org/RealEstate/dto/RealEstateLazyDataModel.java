package org.RealEstate.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.model.RealEstate;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@Named
@ViewScoped
public class RealEstateLazyDataModel extends LazyDataModel<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int first;

	private int pageSize;

	private List<RealEstate> pageItems;

	public RealEstateLazyDataModel() {
		pageItems = new ArrayList<RealEstate>();
	}

	@Override
	public int count(Map<String, FilterMeta> arg0) {
		// TODO Auto-generated method stub
		return pageItems.size();
	}

	@Override
	public List<RealEstate> load(int first, int pageSize, Map<String, SortMeta> sortMap,
			Map<String, FilterMeta> filterMap) {
		this.first = first;
		this.pageSize = pageSize;

		return pageItems;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<RealEstate> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<RealEstate> pageItems) {
		this.pageItems = pageItems;
	}

}
