package org.RealEstate.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_land")
public class Land extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean greenBond;
	private boolean blockNb;
	private int nbOfShares;
	private boolean road;
	private boolean water;
	private boolean electricity;

	public Land() {
		// TODO Auto-generated constructor stub
	}

	public Land(int nbOfShares, boolean road, boolean water,Village village,List<String> images) {
		super("Land 1", "this land is for you", 3500, village, 87000);
		this.nbOfShares = nbOfShares;
		this.road = road;
		this.water = water;
		super.setImages(images);
	}

	public boolean isGreenBond() {
		return greenBond;
	}

	public void setGreenBond(boolean greenBond) {
		this.greenBond = greenBond;
	}

	public boolean isRoad() {
		return road;
	}

	public void setRoad(boolean road) {
		this.road = road;
	}

	public boolean isWater() {
		return water;
	}

	public void setWater(boolean water) {
		this.water = water;
	}

	public boolean isElectricity() {
		return electricity;
	}

	public void setElectricity(boolean electricity) {
		this.electricity = electricity;
	}

	public int getNbOfShares() {
		return nbOfShares;
	}

	public void setNbOfShares(int nbOfShares) {
		this.nbOfShares = nbOfShares;
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
		Land other = (Land) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
