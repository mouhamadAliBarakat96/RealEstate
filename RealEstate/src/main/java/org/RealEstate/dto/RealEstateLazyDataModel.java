package org.RealEstate.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.User;
import org.RealEstate.model.Village;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

public class RealEstateLazyDataModel extends LazyDataModel<RealEstate> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<RealEstate> pageItems;

	private RealEstateFacade facade;

	private User user;
	private String postType;
	private int minPrice;
	private int maxPrice;
	private Village village;
	private AtomicLong totalCount = new AtomicLong();
	private int bedRoom;
	private boolean bedRoomEq;
	private int bathRoom;
	private boolean bathRoomEq;
	private District district;
	private Governorate governorate;

	public RealEstateLazyDataModel(RealEstateFacade facade) {
		// this.pageItems = pageItems;
		this.facade = facade;
	}

	@Override
	public int count(Map<String, FilterMeta> arg0) {

		try {
			return Math.toIntExact(facade.countRealSatateWithFilter(user, postType, minPrice, maxPrice, village,
					totalCount, bedRoom, bedRoomEq, bathRoom, bathRoomEq, district, governorate));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<RealEstate> load(int first, int pageSize, Map<String, SortMeta> sortMap,
			Map<String, FilterMeta> filterMap) {
		try {
			pageItems = facade.findAllRealSatateWithFilter(user, postType, minPrice, maxPrice, village,
					(first / pageSize) + 1, pageSize, totalCount, bedRoom, bedRoomEq, bathRoom, bathRoomEq, district,
					governorate);
			
			
			setRowCount(Math.toIntExact(totalCount.get()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pageItems;
	}

	public List<RealEstate> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<RealEstate> pageItems) {
		this.pageItems = pageItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
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

	public int getBedRoom() {
		return bedRoom;
	}

	public void setBedRoom(int bedRoom) {
		this.bedRoom = bedRoom;
	}

	public boolean isBedRoomEq() {
		return bedRoomEq;
	}

	public void setBedRoomEq(boolean bedRoomEq) {
		this.bedRoomEq = bedRoomEq;
	}

	public int getBathRoom() {
		return bathRoom;
	}

	public void setBathRoom(int bathRoom) {
		this.bathRoom = bathRoom;
	}

	public boolean isBathRoomEq() {
		return bathRoomEq;
	}

	public void setBathRoomEq(boolean bathRoomEq) {
		this.bathRoomEq = bathRoomEq;
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

}
