package org.RealEstate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "tbl_token")
@NamedQueries({
		@NamedQuery(name = TokenEntity.FIND_BY_USER, query = "select t from TokenEntity t where t.user = :pUser"),
		@NamedQuery(name = TokenEntity.FIND_BY_TOKEN, query = "select t from TokenEntity t where t.tokenValue = :pTokenValue") })
public class TokenEntity {

	public final static String FIND_BY_USER = "TokenEntity.FIND_BY_USER";

	public static final String FIND_BY_TOKEN = "TokenEntity.FIND_BY_TOKEN";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Expose
	private long id;

	@Column(name = "token_value")
	private String tokenValue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration_date")
	private Date expirationDate;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public TokenEntity() {

	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
