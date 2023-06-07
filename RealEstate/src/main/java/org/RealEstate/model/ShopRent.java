package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.RealEstate.enumerator.WaterResources;

@Entity
@Table(name = "tbl_shop_rent")
public class ShopRent extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private boolean electricElevator;
	private int nbOfCarPark;

	@ElementCollection(targetClass = WaterResources.class)
	@Enumerated(EnumType.STRING)
	private List<WaterResources> waterResources = new ArrayList<>();

	private int storefronts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isElectricElevator() {
		return electricElevator;
	}

	public void setElectricElevator(boolean electricElevator) {
		this.electricElevator = electricElevator;
	}

	public int getNbOfCarPark() {
		return nbOfCarPark;
	}

	public void setNbOfCarPark(int nbOfCarPark) {
		this.nbOfCarPark = nbOfCarPark;
	}

	public List<WaterResources> getWaterResources() {
		return waterResources;
	}

	public void setWaterResources(List<WaterResources> waterResources) {
		this.waterResources = waterResources;
	}

	public int getStorefronts() {
		return storefronts;
	}

	public void setStorefronts(int storefronts) {
		this.storefronts = storefronts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopRent other = (ShopRent) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
