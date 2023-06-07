package org.RealEstate.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_land")
public class Land extends RealEstate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	private boolean greenBond;
	private boolean blockNb;
	private int nbOfShares;
	private boolean road;
	private boolean water;
	private boolean electricity;



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
