package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.WaterResources;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_appratment_rent")
public class AppratmentRent extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private int nbRoom;
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
	private boolean garden;
	@Expose
	private int nbBathRoom;

	public AppratmentRent() {
		// TODO Auto-generated constructor stub
	}

	public AppratmentRent(int nbRoom, int floor, boolean garden, int nbBathRoom, Village village,List<String> images) {
		super("Appratment Rent", "this Appratment is for you", 3500, village, 200000);
		this.nbRoom = nbRoom;
		this.floor = floor;
		this.garden = garden;
		this.nbBathRoom = nbBathRoom;
		super.setImages(images);
		super.setPostType(PostType.APPRATMENT_RENT);
	}

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
