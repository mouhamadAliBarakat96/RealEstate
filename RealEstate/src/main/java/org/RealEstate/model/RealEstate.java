package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class RealEstate extends MainEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 1000)
	private String tittle;

	@Column(length = 3000)
	private String subTittle;

	private boolean negotiable;

	private int space;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id")
	private Village village;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private double price;
	@Embedded
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();
	private int views;
	private int liked;

	private boolean freezed;
	@Temporal(TemporalType.TIMESTAMP)

	private Date postDate;

	@ElementCollection(targetClass = String.class)
	private List<String> iamges = new ArrayList<>();

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getSubTittle() {
		return subTittle;
	}

	public void setSubTittle(String subTittle) {
		this.subTittle = subTittle;
	}

	public boolean isNegotiable() {
		return negotiable;
	}

	public void setNegotiable(boolean negotiable) {
		this.negotiable = negotiable;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public List<String> getIamges() {
		return iamges;
	}

	public void setIamges(List<String> iamges) {
		this.iamges = iamges;
	}

}
