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

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_office_sell")
public class OfficeSell extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private int nbRoom;
	@Expose
	private int nbBathRoom;
	@Expose
	private boolean electricElevator;
	@Expose
	private int nbOfCarPark;
	@Expose
	private int floor;

	@ElementCollection(targetClass = WaterResources.class)
	@Enumerated(EnumType.STRING)
	@Expose
	private List<WaterResources> waterResources = new ArrayList<>();
	
	@Expose
	private boolean greenBond;
	@Expose
	private boolean blockNb;



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

	public int getNbBathRoom() {
		return nbBathRoom;
	}

	public void setNbBathRoom(int nbBathRoom) {
		this.nbBathRoom = nbBathRoom;
	}

	public boolean isGreenBond() {
		return greenBond;
	}

	public void setGreenBond(boolean greenBond) {
		this.greenBond = greenBond;
	}

	public boolean isBlockNb() {
		return blockNb;
	}

	public void setBlockNb(boolean blockNb) {
		this.blockNb = blockNb;
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
		OfficeSell other = (OfficeSell) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
