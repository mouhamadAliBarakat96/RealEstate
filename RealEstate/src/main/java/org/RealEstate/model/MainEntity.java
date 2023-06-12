package org.RealEstate.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@MappedSuperclass
@EntityListeners(MainEntityListener.class)
public class MainEntity extends ApplicationEntity implements Serializable, Comparable<MainEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;

	@Temporal(TemporalType.TIMESTAMP)
@Expose
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date LastUpdate;

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub

	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getId());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainEntity other = (MainEntity) obj;
		if (Double.doubleToLongBits(getId()) != Double.doubleToLongBits(other.getId()))
			return false;
		return true;
	}

	@Override
	public int compareTo(MainEntity mainEntity) {
		// TODO Auto-generated method stub

		return mainEntity.getLastUpdate().compareTo(this.getLastUpdate());
	}

}
