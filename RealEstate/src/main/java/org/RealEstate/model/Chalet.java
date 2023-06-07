package org.RealEstate.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tbl_chalet")
public class Chalet extends MainEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	@Size(min = 1)
	private String name;
	private boolean pool;
	private boolean chimney;
	@Column(length = 3000)
	private String descrption;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id")
	private Village village;
	// todo
	private double price;
	@Embedded
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();

	private int views;

	private int liked;

	private int space;

	@Temporal(TemporalType.TIMESTAMP)

	private Date postDate;

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

	public boolean isPool() {
		return pool;
	}

	public void setPool(boolean pool) {
		this.pool = pool;
	}

	public boolean isChimney() {
		return chimney;
	}

	public void setChimney(boolean chimney) {
		this.chimney = chimney;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public GoogleMapAttribute getAddressEmbeddable() {
		return addressEmbeddable;
	}

	public void setAddressEmbeddable(GoogleMapAttribute addressEmbeddable) {
		this.addressEmbeddable = addressEmbeddable;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getLiked() {
		return liked;
	}

	public void setLiked(int liked) {
		this.liked = liked;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
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
		Chalet other = (Chalet) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
