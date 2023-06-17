package org.RealEstate.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_village")
@NamedQueries({
		@NamedQuery(name = Village.FING_BY_DISTRICT, query = "SELECT village FROM Village village WHERE village.district.id = :districtId") })

public class Village extends MainEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FING_BY_DISTRICT = "Village.FING_BY_DISTRICT";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	private long id;

	@Size(min = 1)
	@Column(unique = true)
	@NotEmpty
	@Expose
	private String name;

	@NotEmpty
	@Expose
	@Column(unique = true)
	private String nameAr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id")
	@NotNull
	@Expose
	private District district;

	public Village() {
		// TODO Auto-generated constructor stub
	}

	public Village(@Size(min = 1) @NotEmpty String name) {
		super();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public District getDistrict() {
		return district;
	}

	public String getNameAr() {
		return nameAr;
	}

	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}

	public void setDistrict(District district) {
		this.district = district;
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
		Village other = (Village) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String fullAddress() {
		return this.name + "/ " + this.getDistrict().getName() + "/ " + this.getDistrict().getGovernorate().getName();
	}

}
