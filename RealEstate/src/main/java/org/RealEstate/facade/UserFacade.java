package org.RealEstate.facade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import org.RealEstate.model.User;
import org.RealEstate.utils.Constants;

@Stateless
public class UserFacade extends AbstractFacade<User> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserFacade() {
		super(User.class);
	}

	public User findWithExcption(Long id) throws Exception {
		User user = this.find(id);
		if (user != null) {
			return user;
		} else {
			throw new Exception(Constants.USER_NOT_EXISTS);
		}
	}

	public User findUserByUserNameAndPassword(String userName, String password) throws Exception {

		List<User> users = getEntityManager().createNamedQuery(User.LOGIN_USER, User.class)
				.setParameter("userName", userName).setParameter("password", password).getResultList();

		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}

	}

	public User findUserByUserName(String userName) throws Exception {

		List<User> users = getEntityManager().createNamedQuery(User.FIND_USER_BY_USER_NAME, User.class)
				.setParameter("userName", userName).getResultList();

		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}

	}
	
	public User findUserByFbId(String fbId) throws Exception {

		List<User> users = getEntityManager().createNamedQuery(User.FIND_USER_BY_FB_ID, User.class)
				.setParameter("fbId", fbId).getResultList();

		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}

	}

	public List<User> findUserProfilePicture(boolean param) {

		return getEntityManager().createNamedQuery(User.USER_PROFILE_PICTURE_FALSE, User.class)
				.setParameter("param", param).getResultList();

	}

	public User findUserByPhoneNumber(String phoneNumber) {

		List<User> users = getEntityManager().createNamedQuery(User.FIND_USER_BY_PHONE_NUMBER, User.class)
				.setParameter("phoneNumber", phoneNumber).getResultList();

		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
	}

}
