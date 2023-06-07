package org.RealEstate.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class GoogleMapAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long latitude;
	private Long longitude;

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

}
