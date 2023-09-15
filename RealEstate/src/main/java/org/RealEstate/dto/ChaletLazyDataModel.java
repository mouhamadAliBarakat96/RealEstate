package org.RealEstate.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.RealEstate.facade.ChaletFacade;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

public class ChaletLazyDataModel extends LazyDataModel<Chalet> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Chalet> pageItems;

	private ChaletFacade facade;

	private User user;
	private int minPrice;
	private int maxPrice;
	private Village village;
	private AtomicLong totalCount = new AtomicLong();
	private District district;
	private Governorate governorate;
	private String name;
	private Boolean pool;
	private Boolean chimney;
	private double weekdays;
	private double weekenddays;

	public ChaletLazyDataModel(ChaletFacade facade) {
		this.facade = facade;
	}

	@Override
	public int count(Map<String, FilterMeta> arg0) {
		try {
			return Math.toIntExact(facade.countChaletWithFilter(user, village, 0, 0, totalCount, district, governorate,
					minPrice, maxPrice, pool, chimney));

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Chalet> load(int first, int pageSize, Map<String, SortMeta> sortMap,
			Map<String, FilterMeta> filterMap) {
		try {
			
			pageItems = facade.findAllChaletWithFilter(user, village, (first / pageSize) + 1, pageSize, totalCount,
					district, governorate, minPrice, maxPrice, pool, chimney);

			setRowCount(Math.toIntExact(totalCount.get()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pageItems;
	}

	public List<Chalet> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Chalet> pageItems) {
		this.pageItems = pageItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public AtomicLong getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(AtomicLong totalCount) {
		this.totalCount = totalCount;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(double weekdays) {
		this.weekdays = weekdays;
	}

	public double getWeekenddays() {
		return weekenddays;
	}

	public void setWeekenddays(double weekenddays) {
		this.weekenddays = weekenddays;
	}

	public Boolean getChimney() {
		return chimney;
	}

	public void setChimney(Boolean chimney) {
		this.chimney = chimney;
	}

	public Boolean getPool() {
		return pool;
	}

	public void setPool(Boolean pool) {
		this.pool = pool;
	}
}
