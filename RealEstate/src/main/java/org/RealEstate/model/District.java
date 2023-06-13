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
@Table(name = "tbl_district")
@NamedQueries({
		@NamedQuery(name = District.FING_BY_GOVERNORATE, query = "SELECT district FROM District district WHERE district.governorate.id = :governorateId") })

// kada2
public class District extends MainEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FING_BY_GOVERNORATE = "District.FING_BY_GOVERNORATE";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	private long id;

	@Size(min = 1)
	@Column(unique = true)
	@NotEmpty
	@Expose
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "governorate_id")
	@NotNull
	@Expose
	private Governorate governorate;

	public District() {
		// TODO Auto-generated constructor stub
	}

	public District(@Size(min = 1) @NotEmpty String name) {
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

	public Governorate getGovernorate() {
		return governorate;
	}

	public void setGovernorate(Governorate governorate) {
		this.governorate = governorate;
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
		District other = (District) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
