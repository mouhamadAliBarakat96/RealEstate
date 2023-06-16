
package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;

import com.google.gson.annotations.Expose;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
@NamedQueries({
		@NamedQuery(name = RealEstate.FING_NB_POST_FOR_USER, query = "SELECT COUNT(realEstate.id) FROM RealEstate realEstate WHERE realEstate.user.id =:userId and realEstate.freezed=false ") })

public abstract class RealEstate extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FING_NB_POST_FOR_USER = "RealEstate.FING_NB_POST_FOR_USER";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	protected long id;
	/**
	 * 
	 */

	@Column(length = 1000)
	@Expose
	private String tittle;

	@Column(length = 3000)
	@Expose
	private String subTittle;

	@Column(length = 1000)
	@Expose
	private String reffuseCause;

	@Column(length = 1000)
	@Expose
	private String reviuexCause;

	@Expose
	private boolean negotiable;
	@Expose
	private int space;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id")
	@Expose
	private Village village;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@Expose
	private User user;
	@Expose
	private double price;
	@Embedded
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();
	@Expose
	private int views;
	@Expose
	private int liked;

	@Expose
	private boolean freezed;
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date postDate;

	@ElementCollection(targetClass = String.class)
	@Expose
	private List<String> images = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Expose
	private PostStatus postStatus;

	@Enumerated(EnumType.STRING)
	@Expose
	private PostType postType;

	@Expose
	private boolean pricePublic;

	public RealEstate() {
		// TODO Auto-generated constructor stub
	}

	public RealEstate(String tittle, String subTittle, int space, Village village, double price) {
		super();
		this.tittle = tittle;
		this.subTittle = subTittle;
		this.space = space;
		this.village = village;
		this.price = price;
	}

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

	public PostStatus getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(PostStatus postStatus) {
		this.postStatus = postStatus;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
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

	public boolean isPricePublic() {
		return pricePublic;
	}

	public void setPricePublic(boolean pricePublic) {
		this.pricePublic = pricePublic;
	}

}
