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

import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.WaterResources;

@Entity
@Table(name = "tbl_office_rent")
public class OfficeRent extends RealEstate implements Serializable {

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

	private int nbBathRoom;

	public OfficeRent() {
		// TODO Auto-generated constructor stub
	}
	
	

	public OfficeRent(int nbRoom, boolean electricElevator, int floor, int nbBathRoom,Village vill,List<String> images) {
		super("Office Rent", "the Office is for you", 3500, vill, 150000);
		super.setImages(images);
		super.setPostType(PostType.OFFICE_RENT);
		
		this.nbRoom = nbRoom;
		this.electricElevator = electricElevator;
		this.floor = floor;
		this.nbBathRoom = nbBathRoom;
		 
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
		OfficeRent other = (OfficeRent) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
