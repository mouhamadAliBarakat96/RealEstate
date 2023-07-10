
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
import javax.persistence.Transient;

import org.RealEstate.enumerator.BoostEnum;
import org.RealEstate.enumerator.PostStatus;
import org.RealEstate.enumerator.PostType;

import com.google.gson.annotations.Expose;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
@NamedQueries({
		// change to enum accepted
		@NamedQuery(name = RealEstate.FING_NB_POST_FOR_USER_ACTIVE, query = "SELECT COUNT(realEstate.id) FROM RealEstate realEstate WHERE realEstate.user.id =:userId and  realEstate.postStatus = org.RealEstate.enumerator.PostStatus.ACCEPTED "),

		@NamedQuery(name = RealEstate.UPDATE_POST_TO_EXPIRY_DATE, query = "UPDATE RealEstate realEstate SET realEstate.postStatus = org.RealEstate.enumerator.PostStatus.EXPIRED WHERE realEstate.postDate < :thresholdDate"),
		@NamedQuery(name = RealEstate.UPDATE_POST_BOOST, query = "UPDATE RealEstate realEstate SET realEstate.boostedUntil = null , realEstate.isBoosted = true  WHERE realEstate.boostedUntil < :todayDate"),

		
		
		@NamedQuery(name = RealEstate.FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING, query = "SELECT COUNT(realEstate.id) FROM RealEstate realEstate WHERE realEstate.user.id =:userId and  (realEstate.postStatus = org.RealEstate.enumerator.PostStatus.ACCEPTED or realEstate.postStatus = org.RealEstate.enumerator.PostStatus.PENDING ) "),
		@NamedQuery(name = RealEstate.FIND_COUNT_POST_BY_STATUS, query = "SELECT COUNT(realEstate.id) FROM RealEstate realEstate WHERE  realEstate.postStatus= :postStatus "),
		@NamedQuery(name = RealEstate.FIND_COUNT_POST_BY_TYPE, query = "SELECT COUNT(realEstate.id) FROM RealEstate realEstate WHERE  realEstate.postType= :postType "),
		@NamedQuery(name = RealEstate.FIND_POSTS_BY_USER_ID, query = "SELECT realEstate  FROM RealEstate realEstate WHERE  realEstate.user= :user ")

})

public abstract class RealEstate extends MainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING = "RealEstate.FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING";
	public static final String FING_NB_POST_FOR_USER_ACTIVE = "RealEstate.FING_NB_POST_FOR_USER_ACTIVE";

	public static final String FIND_COUNT_POST_BY_STATUS = "RealEstate.FIND_COUNT_POST_BY_STATUS";
	public static final String FIND_COUNT_POST_BY_TYPE = "RealEstate.FIND_COUNT_POST_BY_TYPE";

	public static final String FIND_POSTS_BY_USER_ID = "RealEstate.FIND_POSTS_BY_USER_ID";

	public static final String UPDATE_POST_TO_EXPIRY_DATE = "RealEstate.UPDATE_POST_TO_EXPIRY_DATE";

	public static final String UPDATE_POST_BOOST = "RealEstate.UPDATE_POST_BOOST" ;
	
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

	// hon leh nrafad
	@Column(length = 1000)
	@Expose
	private String reffuseCause;

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
	@Expose
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();

	@Expose
	private int views;
	@Expose
	private int liked;

	@Expose
	private int numberOfCall;

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

	@Expose
	private boolean isVerfied;

	@Expose
	private boolean isBoosted;

	@Expose
	@Temporal(TemporalType.TIMESTAMP)
	private Date boostedUntil;

	@Transient
	private BoostEnum boostEnum;

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

	public BoostEnum getBoostEnum() {
		return boostEnum;
	}

	public void setBoostEnum(BoostEnum boostEnum) {
		this.boostEnum = boostEnum;
	}

	public String getReffuseCause() {
		return reffuseCause;
	}

	public void setReffuseCause(String reffuseCause) {
		this.reffuseCause = reffuseCause;
	}

	public void addToImages(List<String> images) {
		this.images.addAll(images);
	}

	public boolean isPricePublic() {
		return pricePublic;
	}

	public void setPricePublic(boolean pricePublic) {
		this.pricePublic = pricePublic;
	}

	public int getNumberOfCall() {
		return numberOfCall;
	}

	public void setNumberOfCall(int numberOfCall) {
		this.numberOfCall = numberOfCall;
	}

	public boolean isVerfied() {
		return isVerfied;
	}

	public boolean isBoosted() {
		return isBoosted;
	}

	public void setBoosted(boolean isBoosted) {
		this.isBoosted = isBoosted;
	}

	public Date getBoostedUntil() {
		return boostedUntil;
	}

	public void setBoostedUntil(Date boostedUntil) {
		this.boostedUntil = boostedUntil;
	}

	public void setVerfied(boolean isVerfied) {
		this.isVerfied = isVerfied;
	}

}
