package org.RealEstate.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_chalet")

@DiscriminatorValue("chalet")
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

	private double weekdays;

	private double weekenddays;

	@Embedded
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();

	private int views;

	private int liked;

	private int space;

	@Temporal(TemporalType.TIMESTAMP)
	private Date postDate;

	@Column(length = 1000)
	@Expose
	private String reffuseCause;

	@Column(length = 1000)
	@Expose
	private String reviuexCause;

	@Enumerated(EnumType.STRING)
	@Expose
	private PostStatus postStatus;

	public Chalet() {
		// TODO Auto-generated constructor stub
	}

	public Chalet(@Size(min = 1) String name, boolean pool, boolean chimney, String descrption, Village village,
			double weekdays, double weekenddays) {
		super();
		this.name = name;
		this.pool = pool;
		this.chimney = chimney;
		this.descrption = descrption;
		this.village = village;
		this.weekdays = weekdays;
		this.weekenddays = weekenddays;
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

	public double getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(double weekdays) {
		this.weekdays = weekdays;
	}

	public double getWeekenddays() {
		return weekenddays;
	}

	public void setWeekenddays(double weekenddays) {
		this.weekenddays = weekenddays;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReffuseCause() {
		return reffuseCause;
	}

	public void setReffuseCause(String reffuseCause) {
		this.reffuseCause = reffuseCause;
	}

	public String getReviuexCause() {
		return reviuexCause;
	}

	public void setReviuexCause(String reviuexCause) {
		this.reviuexCause = reviuexCause;
	}

	public PostStatus getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(PostStatus postStatus) {
		this.postStatus = postStatus;
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
