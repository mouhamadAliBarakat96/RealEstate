package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.RealEstate.enumerator.WaterResources;

@Entity
@Table(name = "tbl_appratment_rent")
public class AppratmentRent extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int nbRoom;
	private boolean electricElevator;
	private int nbOfCarPark;
	private int floor;

	@ElementCollection(targetClass = WaterResources.class)
	@Enumerated(EnumType.STRING)
	private List<WaterResources> waterResources = new ArrayList<>();

	private boolean garden;

	private int nbBathRoom;

	public int getNbRoom() {
		return nbRoom;
	}

	public void setNbRoom(int nbRoom) {
		this.nbRoom = nbRoom;
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

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public List<WaterResources> getWaterResources() {
		return waterResources;
	}

	public void setWaterResources(List<WaterResources> waterResources) {
		this.waterResources = waterResources;
	}

	public boolean isGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public int getNbBathRoom() {
		return nbBathRoom;
	}

	public void setNbBathRoom(int nbBathRoom) {
		this.nbBathRoom = nbBathRoom;
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
		AppratmentRent other = (AppratmentRent) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
