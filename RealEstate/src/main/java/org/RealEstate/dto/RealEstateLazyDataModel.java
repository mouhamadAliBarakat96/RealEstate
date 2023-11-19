package org.RealEstate.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.facade.RealEstateFacade;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
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
	private List<Integer> bedRooms;
	private boolean bedRoomEq;
	private List<Integer> bathRooms;
	private boolean bathRoomEq;
	private District district;
	private Governorate governorate;
	private ExchangeRealEstateType exchangeRealEstateType;

	public RealEstateLazyDataModel(RealEstateFacade facade) {
		// this.pageItems = pageItems;
		this.facade = facade;
	}

	@Override
	public int count(Map<String, FilterMeta> arg0) {

		try {
			return Math.toIntExact(facade.countRealSatateWithFilter(user,false, postType, minPrice, maxPrice, village,
					totalCount, bedRooms, bathRooms, district, governorate, exchangeRealEstateType , new ArrayList<String>()));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<RealEstate> load(int first, int pageSize, Map<String, SortMeta> sortMap,
			Map<String, FilterMeta> filterMap) {
		try {
			pageItems = facade.findAllRealSatateWithFilter(user, false ,postType, minPrice, maxPrice, village,
					(first / pageSize) + 1, pageSize, totalCount, bedRooms, bathRooms, district, governorate,
					exchangeRealEstateType , new ArrayList<String>());
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

	public boolean isBedRoomEq() {
		return bedRoomEq;
	}

	public void setBedRoomEq(boolean bedRoomEq) {
		this.bedRoomEq = bedRoomEq;
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

	public void listenerSelect(RealEstateTypeEnum type) {

		if (type == RealEstateTypeEnum.FORRENT) {
			pageItems = pageItems.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_RENT)
							|| x.getPostType().equals(PostType.OFFICE_RENT)
							|| x.getPostType().equals(PostType.SHOP_RENT))
					.collect(Collectors.toList());

		} else if (type == RealEstateTypeEnum.FORSELL) {
			pageItems = pageItems.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_SELL)
							|| x.getPostType().equals(PostType.OFFICE_SELL)
							|| x.getPostType().equals(PostType.SHOP_SELL) || x.getPostType().equals(PostType.LAND))
					.collect(Collectors.toList());

		}
	}

	public ExchangeRealEstateType getExchangeRealEstateType() {
		return exchangeRealEstateType;
	}

	public void setExchangeRealEstateType(ExchangeRealEstateType exchangeRealEstateType) {
		this.exchangeRealEstateType = exchangeRealEstateType;
	}

	public List<Integer> getBedRooms() {
		return bedRooms;
	}

	public void setBedRooms(List<Integer> bedRooms) {
		this.bedRooms = bedRooms;
	}

	public List<Integer> getBathRooms() {
		return bathRooms;
	}

	public void setBathRooms(List<Integer> bathRooms) {
		this.bathRooms = bathRooms;
	}

}
