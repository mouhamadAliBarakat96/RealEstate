package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.RealEstate.enumerator.BoostEnum;
import org.RealEstate.enumerator.PostStatus;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_chalet")
@DiscriminatorValue("chalet")
@NamedQueries({
		@NamedQuery(name = Chalet.FIND_POSTS_BY_USER_ID, query = "SELECT chalet  FROM Chalet chalet WHERE  chalet.user= :user "),
		@NamedQuery(name = Chalet.UPDATE_CHALET_TO_EXPIRY_DATE, query = "UPDATE Chalet chalet SET chalet.postStatus = org.RealEstate.enumerator.PostStatus.EXPIRED WHERE chalet.postDate < :thresholdDate"),
		@NamedQuery(name = Chalet.FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING, query = "SELECT COUNT(chalet.id) FROM Chalet chalet WHERE chalet.user.id =:userId and  (chalet.postStatus = org.RealEstate.enumerator.PostStatus.ACCEPTED or chalet.postStatus = org.RealEstate.enumerator.PostStatus.PENDING ) "),
		@NamedQuery(name = Chalet.UPDATE_POST_BOOST, query = "UPDATE Chalet chalet SET chalet.boostedUntil = null , chalet.isBoosted = true  WHERE chalet.boostedUntil < :todayDate"), })

public class Chalet extends MainEntity implements Serializable {

	public static final String FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING = "Chalet.FING_NB_POST_FOR_USER_ACTIVE_OR_PENDING";

	public static final String UPDATE_CHALET_TO_EXPIRY_DATE = "Chalet.UPDATE_CHALET_TO_EXPIRY_DATE";
	public static final String UPDATE_POST_BOOST = "Chalet.UPDATE_POST_BOOST";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FIND_POSTS_BY_USER_ID = "Chalet.FIND_POSTS_BY_USER_ID";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	private long id;

	@Expose
	private String name;

	@Expose
	private boolean pool;
	@Expose
	private boolean chimney;
	@Expose
	@Column(length = 3000)
	private String descrption;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id")
	@Expose
	private Village village;
	@Expose
	private double weekdays;
	@Expose
	private double weekenddays;

	@Embedded
	@Expose
	private GoogleMapAttribute addressEmbeddable = new GoogleMapAttribute();
	@Expose
	private int views;
	@Expose
	private int liked;
	@Expose
	private int space;

	@Temporal(TemporalType.TIMESTAMP)
	@Expose
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@Expose
	private User user;

	@Expose
	private int numberOfCall;

	@ElementCollection(targetClass = String.class)
	@Expose
	private List<String> images = new ArrayList<>();

	@Expose
	private boolean isVerfied;

	@Expose
	private boolean isBoosted;

	@Expose
	@Temporal(TemporalType.TIMESTAMP)
	private Date boostedUntil;

	@Transient
	private BoostEnum boostEnum;

	@Expose
	private boolean pendingBoost;
	
	@Expose
	private boolean needVerified;

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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public User getUser() {
		return user;
	}

	public int getNumberOfCall() {
		return numberOfCall;
	}

	public void setNumberOfCall(int numberOfCall) {
		this.numberOfCall = numberOfCall;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isVerfied() {
		return isVerfied;
	}

	public void setVerfied(boolean isVerfied) {
		this.isVerfied = isVerfied;
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

	public BoostEnum getBoostEnum() {
		return boostEnum;
	}

	public void setBoostEnum(BoostEnum boostEnum) {
		this.boostEnum = boostEnum;
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

	public void addToImages(List<String> images) {
		this.images.addAll(images);
	}

	public void removeFromPhotos(String image) {
		this.images.remove(image);
	}

	public boolean isPendingBoost() {
		return pendingBoost;
	}

	public void setPendingBoost(boolean pendingBoost) {
		this.pendingBoost = pendingBoost;
	}

	public boolean isNeedVerified() {
		return needVerified;
	}

	public void setNeedVerified(boolean needVerified) {
		this.needVerified = needVerified;
	}

}
