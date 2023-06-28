package org.RealEstate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_user")
@NamedQueries({
		// change to enum accepted
		@NamedQuery(name = User.LOGIN_USER, query = "SELECT user FROM User user WHERE  user.userName= :userName and user.passowrd = :password "),
		@NamedQuery(name = User.USER_PROFILE_PICTURE_FALSE, query = "SELECT user FROM User user WHERE  user.showProfilePicture =  false"),

})
public class User extends MainEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String LOGIN_USER = "USER.LOGIN";

	public static final String USER_PROFILE_PICTURE_FALSE = "USER.USER_PROFILE_PICTURE_FALSE";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	private long id;
	@Expose
	private String firstName;
	@Expose
	private String lastName;
	@Expose
	private String middleName;
	@Expose
	private String profileImageUrl;

	@Expose
	// because on every change image we need to agree by admin
	private boolean showProfilePicture;

	@Expose
	@Column(unique = true)
	private String userName;
	@Expose(serialize = false)
	private String passowrd;
	@Expose
	private boolean freezed;

	@Expose
	@Pattern(regexp = "^(03|71|76|81)\\d{6}$", message = "Invalid format Phone Number")
	private String phoneNumber;

	// el post li howe mnzlon
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
///	@Expose
	private List<RealEstate> readStateList = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
//	@Expose
	private List<Chalet> chales = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "realestate_fav_user", joinColumns = { @JoinColumn(name = "user_id") },

			inverseJoinColumns = { @JoinColumn(name = "state_id") }

	)
	@Expose
	private List<RealEstate> readStateFavoriteList = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassowrd() {
		return passowrd;
	}

	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}

	public boolean isFreezed() {
		return freezed;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	public List<RealEstate> getReadStateList() {
		return readStateList;
	}

	public void setReadStateList(List<RealEstate> readStateList) {
		this.readStateList = readStateList;
	}

	public List<RealEstate> getReadStateFavoriteList() {
		return readStateFavoriteList;
	}

	public void setReadStateFavoriteList(List<RealEstate> readStateFavoriteList) {
		this.readStateFavoriteList = readStateFavoriteList;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Chalet> getChales() {
		return chales;
	}

	public void setChales(List<Chalet> chales) {
		this.chales = chales;
	}

	public boolean isShowProfilePicture() {
		return showProfilePicture;
	}

	public void setShowProfilePicture(boolean showProfilePicture) {
		this.showProfilePicture = showProfilePicture;
	}

}
 