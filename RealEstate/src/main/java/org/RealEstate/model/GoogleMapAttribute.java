package org.RealEstate.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.google.gson.annotations.Expose;

@Embeddable
public class GoogleMapAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private double latitude;
	@Expose
	private double longitude;

	public GoogleMapAttribute() {
		super();

	}

	public GoogleMapAttribute(double latitude, double longitude) {

		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
