package org.RealEstate.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Chalet;
import org.RealEstate.model.District;
import org.RealEstate.model.Governorate;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.Village;

@Named
@ViewScoped
public class IndexController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<RealEstate> realEstates = new ArrayList<>();

	private List<RealEstate> filteredRealEstates = new ArrayList<>();

	private List<Chalet> chaletList = new ArrayList<>();

	private PostType selectPostType = PostType.APPRATMENT_RENT;

//	private List<Governorate> governorates = new ArrayList<>();
//	
//	private Governorate selecteGovernorate=new Governorate();
	
	
	private List<Village> villages = new ArrayList<>();
	private Village selecteVillage=new Village();
	
	
//	private List<District> districts = new ArrayList<>();

	@PostConstruct
	public void init() {

		Governorate governorate = new Governorate("Janoub");

		District district = new District("Tyre");
		district.setGovernorate(governorate);

		Village vill = new Village("Chehour");
		vill.setDistrict(district);

		List<String> imagesUrl = Arrays.asList("property-1.jpg", "property-2.jpg", "property-3.jpg", "property-4.jpg");

		addToRealEstate(new AppratmentRent(5, 2, true, 1, vill, imagesUrl));
		addToRealEstate(new AppratmentRent(5, 2, true, 1, vill, imagesUrl));

		addToRealEstate(new AppratmentSell(3, 1, false, 1, vill, imagesUrl));
		addToRealEstate(new AppratmentSell(3, 1, false, 1, vill, imagesUrl));

		addToRealEstate(new Land(5, false, true, vill, imagesUrl));
		addToRealEstate(new Land(5, false, true, vill, imagesUrl));
		addToRealEstate(new Land(5, false, true, vill, imagesUrl));
		addToRealEstate(new Land(5, false, true, vill, imagesUrl));

		addToRealEstate(new OfficeRent(3, false, 1, 1, vill, imagesUrl));

		addToChalet(new Chalet("Joulie", true, false, "for couples", vill, 100, 150));
		addToChalet(new Chalet("Corner", true, false, "over 10 person", vill, 100, 150));
		addToChalet(new Chalet("Zoz", true, false, "for families", vill, 100, 150));

		filteredRealEstates.addAll(realEstates);

	}

	public void listenerSelect(RealEstateTypeEnum type) {

		if (type == RealEstateTypeEnum.FORRENT) {
			filteredRealEstates = realEstates.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_RENT)
							|| x.getPostType().equals(PostType.OFFICE_RENT)
							|| x.getPostType().equals(PostType.SHOP_RENT))
					.collect(Collectors.toList());

		} else if (type == RealEstateTypeEnum.FORSELL) {
			filteredRealEstates = realEstates.stream()
					.filter(x -> x.getPostType().equals(PostType.APPRATMENT_SELL)
							|| x.getPostType().equals(PostType.OFFICE_SELL)
							|| x.getPostType().equals(PostType.SHOP_SELL) || x.getPostType().equals(PostType.LAND))
					.collect(Collectors.toList());

		} else {
			filteredRealEstates = realEstates;
		}
	}

	public List<RealEstate> getRealEstates() {
		return realEstates;
	}

	public void setRealEstates(List<RealEstate> realEstates) {
		this.realEstates = realEstates;
	}

	public List<Chalet> getChaletList() {
		return chaletList;
	}

	public void setChaletList(List<Chalet> chaletList) {
		this.chaletList = chaletList;
	}

	public boolean hasRoomsAndBathRooms(RealEstate item) {
		return (item.getPostType().equals(PostType.APPRATMENT_RENT)
				|| item.getPostType().equals(PostType.APPRATMENT_SELL)
				|| item.getPostType().equals(PostType.OFFICE_RENT) || item.getPostType().equals(PostType.OFFICE_SELL));
	}

	public int numberOfSellAppartments() {
		return realEstates.stream().filter(x -> x instanceof AppratmentRent).collect(Collectors.toList()).size();
	}

	public int numberOfRentAppartments() {
		return realEstates.stream().filter(x -> x instanceof AppratmentSell).collect(Collectors.toList()).size();
	}

	public int numberOfLands() {
		return realEstates.stream().filter(x -> x instanceof Land).collect(Collectors.toList()).size();
	}

	public void addToRealEstate(RealEstate item) {
		realEstates.add(item);
	}

	public void addToChalet(Chalet item) {
		this.chaletList.add(item);
	}

	public int random() {
		Random random = new Random();
		int randomValue = random.nextInt(3) + 1;
		return randomValue;
	}

	public List<RealEstate> getFilteredRealEstates() {
		return filteredRealEstates;
	}

	public void setFilteredRealEstates(List<RealEstate> filteredRealEstates) {
		this.filteredRealEstates = filteredRealEstates;
	}

	public PostType getSelectPostType() {
		return selectPostType;
	}

	public void setSelectPostType(PostType selectPostType) {
		this.selectPostType = selectPostType;
	}

	 
	public List<Village> getVillages() {
		return villages;
	}

	public void setVillages(List<Village> villages) {
		this.villages = villages;
	}

	public Village getSelecteVillage() {
		return selecteVillage;
	}

	public void setSelecteVillage(Village selecteVillage) {
		this.selecteVillage = selecteVillage;
	}
 
}
